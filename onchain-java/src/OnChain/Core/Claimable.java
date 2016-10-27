package OnChain.Core;

import OnChain.Fixed8;

public class Claimable
{
    public TransactionOutput output;
    public int startHeight;
    public int endHeight;

    public Fixed8 value()
    {
    	return output.value;
    }
}
