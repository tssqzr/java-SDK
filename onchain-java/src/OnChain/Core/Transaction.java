package OnChain.Core;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import OnChain.*;
import OnChain.Core.Scripts.Script;
import OnChain.IO.*;
import OnChain.IO.Json.*;
import OnChain.Network.*;

public abstract class Transaction extends Inventory
{
	public final TransactionType type;
	public static final byte version = 0;
	public TransactionAttribute[] attributes;
	public TransactionInput[] inputs;
	public TransactionOutput[] outputs;
	public Script[] scripts;
	
	protected Transaction(TransactionType type)
	{
		this.type = type;
	}
	
	@Override
	public void deserialize(BinaryReader reader) throws IOException
	{
		deserializeUnsigned(reader);
		try
		{
			scripts = reader.readSerializableArray(Script.class);
		}
		catch (InstantiationException | IllegalAccessException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException
	{
	}
	
	public static Transaction deserializeFrom(byte[] value) throws IOException
	{
		return deserializeFrom(value, 0);
	}
	
	public static Transaction deserializeFrom(byte[] value, int offset) throws IOException
	{
		try (ByteArrayInputStream ms = new ByteArrayInputStream(value, offset, value.length - offset))
		{
			try (BinaryReader reader = new BinaryReader(ms))
			{
				return deserializeFrom(reader);
			}
		}
	}

	static Transaction deserializeFrom(BinaryReader reader) throws IOException
	{
        try
        {
            TransactionType type = TransactionType.valueOf(reader.readByte());
            String typeName = "OnChain.Core." + type.toString();
            Transaction transaction = (Transaction)Class.forName(typeName).newInstance();
            transaction.deserializeUnsignedWithoutType(reader);
			transaction.scripts = reader.readSerializableArray(Script.class);
			return transaction;
		}
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
			throw new IOException(ex);
		}
	}
	
	@Override
	public void deserializeUnsigned(BinaryReader reader) throws IOException
	{
        if (type.value() != reader.readByte())
            throw new IOException();
        deserializeUnsignedWithoutType(reader);
	}

	private void deserializeUnsignedWithoutType(BinaryReader reader) throws IOException
	{
        try
        {
            if (reader.readByte() != version)
                throw new IOException();
            deserializeExclusiveData(reader);
			attributes = reader.readSerializableArray(TransactionAttribute.class);
	        if (Arrays.stream(attributes).map(p -> p.usage).distinct().count() != attributes.length)
	            throw new IOException();
	        inputs = reader.readSerializableArray(TransactionInput.class);
	        TransactionInput[] inputs_all = getAllInputs().toArray(TransactionInput[]::new);
	        for (int i = 1; i < inputs_all.length; i++)
	            for (int j = 0; j < i; j++)
	                if (inputs_all[i].prevHash == inputs_all[j].prevHash && inputs_all[i].prevIndex == inputs_all[j].prevIndex)
	                    throw new IOException();
	        outputs = reader.readSerializableArray(TransactionOutput.class);
	        if (outputs.length > 65536) throw new IOException();
	        if (Blockchain.ANTSHARE != null)
	            for (TransactionOutput output : Arrays.stream(outputs).filter(p -> p.assetId.equals(Blockchain.ANTSHARE.hash())).toArray(TransactionOutput[]::new))
	                if (output.value.getData() % 100000000 != 0)
	                    throw new IOException();
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
			throw new IOException(ex);
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null) return false;
		if (!(obj instanceof Transaction)) return false;
		Transaction tx = (Transaction)obj;
		return hash().equals(tx.hash());
	}
	
	public Stream<TransactionInput> getAllInputs()
	{
		return Arrays.stream(inputs);
	}
	
	@Override
	public UInt160[] getScriptHashesForVerifying()
	{
        if (references() == null) throw new IllegalStateException();
        HashSet<UInt160> hashes = new HashSet<UInt160>(Arrays.stream(inputs).map(p -> references().get(p).scriptHash).collect(Collectors.toList()));
        for (Entry<UInt256, List<TransactionOutput>> group : Arrays.stream(outputs).collect(Collectors.groupingBy(p -> p.assetId)).entrySet())
        {
            Transaction tx;
			try
			{
				tx = Blockchain.current().getTransaction(group.getKey());
			}
			catch (Exception ex)
			{
				throw new IllegalStateException(ex);
			}
            if (tx == null || !(tx instanceof RegisterTransaction)) throw new IllegalStateException();
            RegisterTransaction asset = (RegisterTransaction)tx;
            if ((asset.assetType.value() & AssetType.DutyFlag.value()) > 0)
            {
                hashes.addAll(group.getValue().stream().map(p -> p.scriptHash).collect(Collectors.toList()));
            }
        }
        return hashes.stream().sorted().toArray(UInt160[]::new);
	}
	
    public TransactionResult[] getTransactionResults()
    {
        if (references() == null) return null;
        Stream<TransactionResult> in = references().values().stream().map(p -> new TransactionResult(p.assetId, p.value));
        Stream<TransactionResult> out = Arrays.stream(outputs).map(p -> new TransactionResult(p.assetId, p.value.negate()));
        Map<UInt256, Fixed8> results = Stream.concat(in, out).collect(Collectors.toMap(p -> p.assetId, p -> p.amount, (a, b) -> a.add(b)));
        return results.entrySet().stream().filter(p -> !p.getValue().equals(Fixed8.ZERO)).map(p -> new TransactionResult(p.getKey(), p.getValue())).toArray(TransactionResult[]::new);
    }
	
	@Override
	public int hashCode()
	{
		return hash().hashCode();
	}

	@Override
	public final InventoryType inventoryType()
	{
		return InventoryType.TX;
	}
	
	public JObject json()
	{
        JObject json = new JObject();
        json.set("txid", new JString(hash().toString()));
        json.set("type", new JString(type.toString()));
        json.set("version", new JNumber(version));
        json.set("attributes", new JArray(Arrays.stream(attributes).map(p -> p.json()).toArray(JObject[]::new)));
        json.set("vin", new JArray(Arrays.stream(inputs).map(p -> p.json()).toArray(JObject[]::new)));
        json.set("vout", new JArray(IntStream.range(0, outputs.length).boxed().map(i -> outputs[i].json(i)).toArray(JObject[]::new)));
        json.set("scripts", new JArray(Arrays.stream(scripts).map(p -> p.json()).toArray(JObject[]::new)));
        return json;
	}
	
	protected void onDeserialized() throws IOException
	{
	}
	
    //[NonSerialized]
    private Map<TransactionInput, TransactionOutput> _references = null;
    public Map<TransactionInput, TransactionOutput> references()
	{
        if (_references == null)
        {
        	Map<TransactionInput, TransactionOutput> map = new HashMap<TransactionInput, TransactionOutput>();
            for (Entry<UInt256, List<TransactionInput>> entry : getAllInputs().collect(Collectors.groupingBy(p -> p.prevHash)).entrySet())
            {
                Transaction tx;
				try
				{
					tx = Blockchain.current().getTransaction(entry.getKey());
				}
				catch (Exception ex)
				{
					return null;
				}
                if (tx == null) return null;
                for (TransactionInput input : entry.getValue())
                {
                    map.put(input, tx.outputs[input.prevIndex]);
                }
            }
            _references = map;
        }
        return _references;
	}
	
	@Override
	public void serialize(BinaryWriter writer) throws IOException
	{
        serializeUnsigned(writer);
        writer.writeSerializableArray(scripts);
	}
	
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException
	{
	}
	
	@Override
	public void serializeUnsigned(BinaryWriter writer) throws IOException
	{
        writer.writeByte(type.value());
        writer.writeByte(version);
        serializeExclusiveData(writer);
        writer.writeSerializableArray(attributes);
        writer.writeSerializableArray(inputs);
        writer.writeSerializableArray(outputs);
	}
	
	public Fixed8 systemFee()
	{
		return Fixed8.ZERO;
	}
	
	@Override
	public boolean verify()
	{
//        if (Blockchain.Default.ContainsTransaction(Hash)) return true;
//        if (!Blockchain.Default.Ability.HasFlag(BlockchainAbility.UnspentIndexes) || !Blockchain.Default.Ability.HasFlag(BlockchainAbility.TransactionIndexes))
//            return false;
//        if (Blockchain.Default.IsDoubleSpend(this))
//            return false;
//        foreach (UInt256 hash in Outputs.Select(p => p.AssetId).Distinct())
//            if (!Blockchain.Default.ContainsAsset(hash))
//                return false;
//        TransactionResult[] results = GetTransactionResults()?.ToArray();
//        if (results == null) return false;
//        TransactionResult[] results_destroy = results.Where(p => p.Amount > Fixed8.Zero).ToArray();
//        if (results_destroy.Length > 1) return false;
//        if (results_destroy.Length == 1 && results_destroy[0].AssetId != Blockchain.AntCoin.Hash)
//            return false;
//        if (SystemFee > Fixed8.Zero && (results_destroy.Length == 0 || results_destroy[0].Amount < SystemFee))
//            return false;
//        TransactionResult[] results_issue = results.Where(p => p.Amount < Fixed8.Zero).ToArray();
//        switch (Type)
//        {
//            case TransactionType.MinerTransaction:
//            case TransactionType.ClaimTransaction:
//                if (results_issue.Any(p => p.AssetId != Blockchain.AntCoin.Hash))
//                    return false;
//                break;
//            case TransactionType.IssueTransaction:
//                if (results_issue.Any(p => p.AssetId == Blockchain.AntCoin.Hash))
//                    return false;
//                break;
//            default:
//                if (results_issue.Length > 0)
//                    return false;
//                break;
//        }
//        TransactionAttribute script = Attributes.FirstOrDefault(p => p.Usage == TransactionAttributeUsage.Script);
//        if (script != null)
//        {
//            ScriptEngine engine = new ScriptEngine(new Script
//            {
//                StackScript = new byte[0],
//                RedeemScript = script.Data
//            }, this);
//            if (!engine.Execute()) return false;
//        }
//        return this.VerifySignature();
		return false;
	}
}
