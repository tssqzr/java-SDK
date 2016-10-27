package OnChain.Core;

import java.io.IOException;
import java.util.Arrays;

import OnChain.*;
import OnChain.Core.Scripts.Script;
import OnChain.IO.*;

/**
 *  订单
 */
public class Order implements Signable
{
    /**
     *  资产编号
     */
    public UInt256 assetId;
    /**
     *  货币编号
     */
    public UInt256 valueAssetId;
    /**
     *  代理人的合约散列
     */
    public UInt160 agent;
    /**
     *  买入或卖出的数量，正数表示买入，负数表示卖出
     */
    public Fixed8 amount;
    /**
     *  价格
     */
    public Fixed8 price;
    /**
     *  委托人的合约散列
     */
    public UInt160 client;
    /**
     *  输入列表
     */
    public TransactionInput[] inputs;
    /**
     *  用于验证该订单的脚本列表
     */
    public Script[] scripts;

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
        	throw new IOException(ex);
		}
    }

    void deserializeInTransaction(BinaryReader reader, AgencyTransaction tx) throws IOException
    {
        deserializeUnsignedInternal(reader, tx.assetId, tx.valueAssetId, tx.agent);
        try
        {
			scripts = reader.readSerializableArray(Script.class);
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
			throw new IOException(ex);
		}
    }

    @Override
    public void deserializeUnsigned(BinaryReader reader) throws IOException
    {
    	try
    	{
	        UInt256 asset_id = reader.readSerializable(UInt256.class);
	        UInt256 value_asset_id = reader.readSerializable(UInt256.class);
	        if (asset_id.equals(value_asset_id)) throw new IOException();
	        UInt160 agent = reader.readSerializable(UInt160.class);
	        deserializeUnsignedInternal(reader, asset_id, value_asset_id, agent);
    	}
    	catch (InstantiationException | IllegalAccessException ex)
    	{
    		throw new IOException(ex);
    	}
    }

    private void deserializeUnsignedInternal(BinaryReader reader, UInt256 asset_id, UInt256 value_asset_id, UInt160 agent) throws IOException
    {
    	try
    	{
	    	this.assetId = asset_id;
	        this.valueAssetId = value_asset_id;
	        this.agent = agent;
	        this.amount = reader.readSerializable(Fixed8.class);
	        if (amount.equals(Fixed8.ZERO)) throw new IOException();
	        if (amount.getData() % 10000 != 0) throw new IOException();
	        this.price = reader.readSerializable(Fixed8.class);
	        if (price.compareTo(Fixed8.ZERO) <= 0) throw new IOException();
	        if (price.getData() % 10000 != 0) throw new IOException();
	        this.client = reader.readSerializable(UInt160.class);
	        this.inputs = reader.readSerializableArray(TransactionInput.class);
	        if (Arrays.stream(inputs).distinct().count() != inputs.length)
	            throw new IOException();
    	}
    	catch (InstantiationException | IllegalAccessException ex)
    	{
    		throw new IOException(ex);
    	}
    }

    @Override
    public UInt160[] getScriptHashesForVerifying()
    {
//        HashSet<UInt160> hashes = new HashSet<UInt160>();
//        RegisterTransaction asset = Blockchain.Default.GetTransaction(AssetId) as RegisterTransaction;
//        if (asset == null) throw new InvalidOperationException();
//        if (asset.AssetType == AssetType.Share)
//        {
//            hashes.Add(Client);
//        }
//        foreach (var group in Inputs.GroupBy(p => p.PrevHash))
//        {
//            Transaction tx = Blockchain.Default.GetTransaction(group.Key);
//            if (tx == null) throw new InvalidOperationException();
//            hashes.UnionWith(group.Select(p => tx.Outputs[p.PrevIndex].ScriptHash));
//        }
//        return hashes.OrderBy(p => p).ToArray();
        return new UInt160[0];
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException
    {
        serializeUnsigned(writer);
        writer.writeSerializableArray(scripts);
    }

    void serializeInTransaction(BinaryWriter writer) throws IOException
    {
        writer.writeSerializable(amount);
        writer.writeSerializable(price);
        writer.writeSerializable(client);
        writer.writeSerializableArray(inputs);
        writer.writeSerializableArray(scripts);
    }

    @Override
    public void serializeUnsigned(BinaryWriter writer) throws IOException
    {
        writer.writeSerializable(assetId);
        writer.writeSerializable(valueAssetId);
        writer.writeSerializable(agent);
        writer.writeSerializable(amount);
        writer.writeSerializable(price);
        writer.writeSerializable(client);
        writer.writeSerializableArray(inputs);
    }
}