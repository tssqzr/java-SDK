package OnChain.Core;

import java.util.*;

import OnChain.*;

public class IssueTransaction extends Transaction
{	
	public IssueTransaction()
	{
		super(TransactionType.IssueTransaction);
	}
	
	@Override
	public UInt160[] getScriptHashesForVerifying()
	{
        HashSet<UInt160> hashes = new HashSet<UInt160>(Arrays.asList(super.getScriptHashesForVerifying()));
        for (TransactionResult result : Arrays.stream(getTransactionResults()).filter(p -> p.amount.compareTo(Fixed8.ZERO) < 0).toArray(TransactionResult[]::new))
        {
            Transaction tx;
			try
			{
				tx = Blockchain.current().getTransaction(result.assetId);
			}
			catch (Exception ex)
			{
				throw new IllegalStateException(ex);
			}
            if (tx == null || !(tx instanceof RegisterTransaction)) throw new IllegalStateException();
            RegisterTransaction asset = (RegisterTransaction)tx;
            hashes.add(asset.admin);
        }
        return hashes.stream().sorted().toArray(UInt160[]::new);
	}
	
	@Override
	public Fixed8 systemFee()
	{
        if (Arrays.stream(outputs).allMatch(p -> p.assetId.equals(Blockchain.ANTSHARE.hash()) || p.assetId.equals(Blockchain.ANTCOIN.hash())))
            return Fixed8.ZERO;
        return Fixed8.fromLong(500);
	}
	
	@Override
	public boolean verify()
	{
		//TODO
		return super.verify();
	}
}
