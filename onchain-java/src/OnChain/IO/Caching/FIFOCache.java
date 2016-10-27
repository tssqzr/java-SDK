package OnChain.IO.Caching;

abstract class FIFOCache<TKey, TValue> extends Cache<TKey, TValue>
{
    public FIFOCache(int max_capacity)
    {
        super(max_capacity);
    }

    @Override
    protected void OnAccess(CacheItem item)
    {
    }
}
