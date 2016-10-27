package OnChain.Core;

import java.io.IOException;

import OnChain.*;
import OnChain.IO.*;

/**
 *  部分成交的订单
 */
public class SplitOrder implements Serializable
{
    /**
     *  买入或卖出的数量
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
    
	@Override
	public void serialize(BinaryWriter writer) throws IOException
	{
        writer.writeSerializable(amount);
        writer.writeSerializable(price);
        writer.writeSerializable(client);
	}
	
	@Override
	public void deserialize(BinaryReader reader) throws IOException
	{
        try
        {
			amount = reader.readSerializable(Fixed8.class);
	        if (amount.equals(Fixed8.ZERO)) throw new IOException();
	        if (amount.getData() % 10000 != 0) throw new IOException();
	        price = reader.readSerializable(Fixed8.class);
	        if (price.compareTo(Fixed8.ZERO) <= 0) throw new IOException();
	        if (price.getData() % 10000 != 0) throw new IOException();
	        client = reader.readSerializable(UInt160.class);
		}
        catch (InstantiationException | IllegalAccessException ex)
        {
        	throw new IOException();
		}
	}
}
