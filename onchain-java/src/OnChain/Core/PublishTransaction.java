package OnChain.Core;

import java.io.IOException;
import java.util.Arrays;

import OnChain.*;
import OnChain.IO.*;
import OnChain.IO.Json.*;

public class PublishTransaction extends Transaction
{
	public byte[][] contracts;
	
	@Override
	public Fixed8 systemFee()
	{
        return Fixed8.fromLong(500 * contracts.length);
	}
	
	public PublishTransaction()
	{
		super(TransactionType.PublishTransaction);
	}
	
	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException
	{
		contracts = new byte[reader.readByte()][];
		if (contracts.length == 0)
			throw new IOException();
		for (int i = 0; i < contracts.length; i++)
			contracts[i] = reader.readVarBytes();
	}
	
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException
	{
        writer.writeByte((byte)contracts.length);
        for (int i = 0; i < contracts.length; i++)
            writer.writeVarBytes(contracts[i]);
	}
	
	@Override
    public JObject json()
    {
        JObject json = super.json();
        json.set("contracts", new JArray(Arrays.stream(contracts).map(p -> new JString(Helper.toHexString(p))).toArray(JString[]::new)));
        return json;
    }
}
