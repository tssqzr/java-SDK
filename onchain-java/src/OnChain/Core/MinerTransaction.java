package OnChain.Core;

import java.io.IOException;
import java.util.Arrays;

import OnChain.IO.*;
import OnChain.IO.Json.*;

public class MinerTransaction extends Transaction
{
	public int nonce; // unsigned int
	
	public MinerTransaction()
	{
		super(TransactionType.MinerTransaction);
	}
	
	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException
	{
		nonce = reader.readInt();
	}
	
	@Override
    public JObject json()
    {
        JObject json = super.json();
        json.set("nonce", new JNumber(Integer.toUnsignedLong(nonce)));
        return json;
    }
	
	@Override
	protected void onDeserialized() throws IOException
	{
        super.onDeserialized();
        if (inputs.length != 0)
            throw new IOException();
        if (Arrays.stream(outputs).anyMatch(p -> p.assetId.equals(Blockchain.ANTCOIN.hash())))
            throw new IOException();
	}
	
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException
	{
		writer.writeInt(nonce);
	}
}
