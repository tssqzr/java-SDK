package OnChain.Core;

import java.io.IOException;
import java.util.*;

import org.bouncycastle.math.ec.ECPoint;

import OnChain.*;
import OnChain.Cryptography.ECC;
import OnChain.IO.*;
import OnChain.IO.Json.*;
import OnChain.Wallets.Contract;

public class EnrollmentTransaction extends Transaction
{
	public ECPoint publicKey;
	
	public EnrollmentTransaction()
	{
		super(TransactionType.EnrollmentTransaction);
	}
	
	@Override
	protected void deserializeExclusiveData(BinaryReader reader) throws IOException
	{
		publicKey = reader.readECPoint();
	}
	
	@Override
	public UInt160[] getScriptHashesForVerifying()
	{
		HashSet<UInt160> hashes = new HashSet<UInt160>(Arrays.asList(super.getScriptHashesForVerifying()));
		hashes.add(miner());
		return hashes.stream().sorted().toArray(UInt160[]::new);
	}
	
	@Override
    public JObject json()
    {
        JObject json = super.json();
        json.set("pubkey", new JString(ECC.toString(publicKey)));
        return json;
    }
	
	private UInt160 _miner = null;
	public UInt160 miner()
	{
        if (_miner == null)
        {
            _miner = Contract.createSignatureContract(publicKey).scriptHash();
        }
        return _miner;
	}
	
	@Override
	protected void onDeserialized() throws IOException
	{
        super.onDeserialized();
        if (outputs.length == 0 || outputs[0].assetId != Blockchain.ANTCOIN.hash() || outputs[0].scriptHash != miner())
            throw new IOException();
	}
	
	@Override
	protected void serializeExclusiveData(BinaryWriter writer) throws IOException
	{
		writer.writeECPoint(publicKey);
	}
	
	@Override
	public Fixed8 systemFee()
	{
		return Fixed8.fromLong(1000);
	}
}
