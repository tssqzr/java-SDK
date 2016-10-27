package OnChain.Network;

import OnChain.UInt256;
import OnChain.Core.Signable;
import OnChain.Cryptography.Digest;

public abstract class Inventory implements Signable
{
    //[NonSerialized]
    private UInt256 _hash = null;
    public UInt256 hash()
    {
        if (_hash == null)
        {
			_hash = new UInt256(Digest.hash256(getHashData()));
        }
        return _hash;
    }

    public abstract InventoryType inventoryType();

    public abstract boolean verify();
}
