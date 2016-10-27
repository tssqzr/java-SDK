package OnChain.Core;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.bouncycastle.math.ec.ECPoint;

import OnChain.*;
import OnChain.Core.Scripts.Script;
import OnChain.Cryptography.ECC;
import OnChain.IO.*;
import OnChain.IO.Json.*;
import OnChain.Wallets.*;

public class RegisterTransaction extends Transaction
{
	public AssetType assetType;
	public String name;
	public Fixed8 amount;
	public byte precision;
	public ECPoint issuer;
	public UInt160 admin;
	
	public RegisterTransaction()
	{
		super(TransactionType.RegisterTransaction);
	}
	
	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException
	{
		try
		{
			assetType = AssetType.valueOf(reader.readByte());
	        if (assetType == AssetType.CreditFlag || assetType == AssetType.DutyFlag)
	            throw new IOException();
	        name = reader.readVarString();
	        amount = reader.readSerializable(Fixed8.class);
	        if (amount.equals(Fixed8.ZERO) || amount.compareTo(Fixed8.SATOSHI.negate()) < 0)
	        	throw new IOException();
	        if (assetType == AssetType.Share && amount.compareTo(Fixed8.ZERO) <= 0)
	            throw new IOException();
	        if (assetType == AssetType.Invoice && !amount.equals(Fixed8.SATOSHI.negate()))
	            throw new IOException();
            precision = reader.readByte();
            if (precision < 0 || precision > 8) throw new IOException();
	        issuer = reader.readECPoint();
	        admin = reader.readSerializable(UInt160.class);
		}
		catch (IllegalArgumentException | InstantiationException | IllegalAccessException ex)
		{
			throw new IOException(ex);
		}
	}
	
	public String getName()
	{
		return getName(Locale.getDefault());
	}
	
    //[NonSerialized]
    private Map<Locale, String> _names;
	public String getName(Locale locale)
	{
        if (_names == null)
        {
            JObject name_obj = JObject.parse(name);
            if (name_obj instanceof JString)
            {
                _names = new HashMap<Locale, String>();
                _names.put(Locale.ENGLISH, name_obj.asString());
            }
            else
            {
                _names = ((JArray)JObject.parse(name)).stream().collect(Collectors.toMap(p -> new Locale(p.get("lang").asString()), p -> p.get("name").asString()));
            }
        }
        if (_names.containsKey(locale))
        {
            return _names.get(locale);
        }
        else if (_names.containsKey(Locale.ENGLISH))
        {
            return _names.get(Locale.ENGLISH);
        }
        else
        {
            return _names.values().stream().findFirst().orElse(name);
        }
	}
	
	@Override
	public UInt160[] getScriptHashesForVerifying()
	{
        HashSet<UInt160> hashes = new HashSet<UInt160>(Arrays.asList(super.getScriptHashesForVerifying()));
        hashes.add(Script.toScriptHash(Contract.createSignatureRedeemScript(issuer)));
        hashes.add(admin);
        return hashes.stream().sorted().toArray(UInt160[]::new);
	}
	
	@Override
    public JObject json()
    {
        JObject json = super.json();
        json.set("asset", new JObject());
        json.get("asset").set("type", new JString(assetType.toString()));
        try
        {
        	json.get("asset").set("name", name == "" ? null : JObject.parse(name));
        }
        catch (IllegalArgumentException ex)
        {
        	json.get("asset").set("name", new JString(name));
        }
        json.get("asset").set("amount", new JString(amount.toString()));
        json.get("asset").set("high", new JNumber(amount.getData() >> 32));
        json.get("asset").set("low", new JNumber(amount.getData() & 0xffffffff));
        json.get("asset").set("precision", new JNumber(precision));
        json.get("asset").set("issuer", new JString(ECC.toString(issuer)));
        json.get("asset").set("admin", new JString(Wallet.toAddress(admin)));
        return json;
    }
	
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException
	{
        writer.writeByte(assetType.value());
        writer.writeVarString(name);
        writer.writeSerializable(amount);
        writer.writeByte(precision);
        writer.writeECPoint(issuer);
        writer.writeSerializable(admin);
	}
	
	@Override
	public Fixed8 systemFee()
	{
        if (assetType == AssetType.AntShare || assetType == AssetType.AntCoin)
            return Fixed8.ZERO;
        return Fixed8.fromLong(10000);
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
