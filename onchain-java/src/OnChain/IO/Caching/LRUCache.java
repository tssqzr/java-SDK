package OnChain.IO.Caching;

import java.util.Date;

abstract class LRUCache<TKey, TValue> extends Cache<TKey, TValue>
{
    public LRUCache(int max_capacity)
    {
        super(max_capacity);
    }

    @Override
    protected void OnAccess(CacheItem item)
    {
        item.Time = new Date();
    }
}
