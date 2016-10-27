package OnChain.Core;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import OnChain.*;
import OnChain.IO.*;

public class AgencyTransaction extends Transaction
{
	public UInt256 assetId;
	public UInt256 valueAssetId;
	public UInt160 agent;
	public Order[] orders;
	public SplitOrder splitOrder;
	
	public AgencyTransaction()
	{
		super(TransactionType.AgencyTransaction);
	}
	
	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException
	{
        try
        {
			assetId = reader.readSerializable(UInt256.class);
	        valueAssetId = reader.readSerializable(UInt256.class);
	        if (assetId.equals(valueAssetId)) throw new IOException();
	        agent = reader.readSerializable(UInt160.class);
	        orders = new Order[(int)reader.readVarInt(0x10000000)];
	        for (int i = 0; i < orders.length; i++)
	        {
	            orders[i] = new Order();
	            orders[i].deserializeInTransaction(reader, this);
	        }
	        if (reader.readVarInt(1) == 0)
	        {
	            splitOrder = null;
	        }
	        else
	        {
	            splitOrder = reader.readSerializable(SplitOrder.class);
	        }
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
        	throw new IOException(ex);
		}
	}
	
	@Override
	public Stream<TransactionInput> getAllInputs()
	{
		return Stream.concat(Arrays.stream(orders).flatMap(p -> Arrays.stream(p.inputs)), super.getAllInputs());
	}
	
	@Override
	public UInt160[] getScriptHashesForVerifying()
	{
		//TODO
//        HashSet<UInt160> hashes = new HashSet<UInt160>();
//        foreach (var group in Inputs.GroupBy(p => p.PrevHash))
//        {
//            Transaction tx = Blockchain.Default.GetTransaction(group.Key);
//            if (tx == null) throw new InvalidOperationException();
//            AgencyTransaction tx_agency = tx as AgencyTransaction;
//            if (tx_agency?.SplitOrder == null || tx_agency.AssetId != AssetId || tx_agency.ValueAssetId != ValueAssetId || tx_agency.Agent != Agent)
//            {
//                hashes.UnionWith(group.Select(p => tx.Outputs[p.PrevIndex].ScriptHash));
//            }
//            else
//            {
//                hashes.UnionWith(group.Select(p => tx.Outputs[p.PrevIndex].ScriptHash).Where(p => p != tx_agency.SplitOrder.Client));
//            }
//        }
//        hashes.Add(Agent);
//        return hashes.OrderBy(p => p).ToArray();
		return super.getScriptHashesForVerifying();
	}
	
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException
	{
        writer.writeSerializable(assetId);
        writer.writeSerializable(valueAssetId);
        writer.writeSerializable(agent);
        writer.writeVarInt(orders.length);
        for (int i = 0; i < orders.length; i++)
        {
            orders[i].serializeInTransaction(writer);
        }
        if (splitOrder == null)
        {
            writer.writeVarInt(0);
        }
        else
        {
            writer.writeVarInt(1);
            writer.writeSerializable(splitOrder);
        }
	}
	
	@Override
	public boolean verify()
	{
		//TODO
		return super.verify();
	}
}
