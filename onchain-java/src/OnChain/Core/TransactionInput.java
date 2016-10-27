package OnChain.Core;

import java.io.IOException;

import OnChain.UInt256;
import OnChain.IO.*;
import OnChain.IO.Json.*;

/**
 *  交易输入
 */
public class TransactionInput implements Serializable
{
    /**
     *  引用交易的散列值
     */
    public UInt256 prevHash;
    /**
     *  引用交易输出的索引
     */
    public short prevIndex; // unsigned short

    /**
     *  比较当前对象与指定对象是否相等
     *  <param name="obj">要比较的对象</param>
     *  <returns>返回对象是否相等</returns>
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (null == obj) return false;
        if (!(obj instanceof TransactionInput)) return false;
        TransactionInput other = (TransactionInput) obj;
        return prevHash.equals(other.prevHash) && prevIndex == other.prevIndex;
    }

    /**
     *  获得对象的HashCode
     *  <returns>返回对象的HashCode</returns>
     */
    @Override
    public int hashCode()
    {
        return prevHash.hashCode() + prevIndex;
    }

	@Override
	public void serialize(BinaryWriter writer) throws IOException
	{
		writer.writeSerializable(prevHash);
		writer.writeShort(prevIndex);
	}

	@Override
	public void deserialize(BinaryReader reader) throws IOException
	{
		try
		{
			prevHash = reader.readSerializable(UInt256.class);
			prevIndex = reader.readShort();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
		}
	}

    /**
     *  将交易输入转变为json对象
     *  <returns>返回json对象</returns>
     */
    public JObject json()
    {
        JObject json = new JObject();
        json.set("txid", new JString(prevHash.toString()));
        json.set("vout", new JNumber(Short.toUnsignedInt(prevIndex)));
        return json;
    }
}
