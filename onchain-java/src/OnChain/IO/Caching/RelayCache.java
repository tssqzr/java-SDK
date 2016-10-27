package OnChain.IO.Caching;

import OnChain.UInt256;
import OnChain.Network.Inventory;

public class RelayCache extends FIFOCache<UInt256, Inventory>
{
    public RelayCache(int max_capacity)
    {
        super(max_capacity);
    }

    @Override
    protected UInt256 GetKeyForItem(Inventory item)
    {
        // TODO
        //return item.Hash;
        return new UInt256();
    }
}