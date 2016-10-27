package OnChain.IO.Caching;

import java.util.*;

abstract class Cache<TKey, TValue> implements Collection<TValue> // TODO, IDisposable
{
    protected class CacheItem
    {
        public TKey Key;
        public TValue Value;
        public Date Time;

        public CacheItem(TKey key, TValue value)
        {
            this.Key = key;
            this.Value = value;
            this.Time = new Date();
        }
    }

    public final Object SyncRoot = new Object();
    protected final Map<TKey, CacheItem> InnerDictionary = new HashMap<TKey, CacheItem>();
    private final int max_capacity;

    public TValue get(TKey key)
    {
        synchronized (SyncRoot)
        {
            if (!InnerDictionary.containsKey(key)) return null;
            CacheItem item = InnerDictionary.get(key);
            OnAccess(item);
            return item.Value;
        }
    }

    public int count()
    {
        synchronized (SyncRoot)
        {
            return InnerDictionary.size();
        }
    }

    public boolean IsReadOnly()
    {
        return false;
    }

    public Cache(int max_capacity)
    {
        this.max_capacity = max_capacity;
    }

    public void Add(TValue item)
    {
        TKey key = GetKeyForItem(item);
        synchronized (SyncRoot)
        {
            if (InnerDictionary.containsKey(key))
            {
                OnAccess(InnerDictionary.get(key));
            }
            else
            {
                if (InnerDictionary.size() >= max_capacity)
                {
                    //TODO: 对PLINQ查询进行性能测试，以便确定此处使用何种算法更优（并行或串行）
//                    foreach (CacheItem item_del in InnerDictionary.Values.AsParallel().OrderBy(p => p.Time).Take(InnerDictionary.Count - max_capacity + 1))
//                    {
//                        RemoveInternal(item_del);
//                    }
                }
                // TODO
                //InnerDictionary.add(key, new CacheItem(key, item));
            }
        }
    }

    public void Clear()
    {
        synchronized (SyncRoot)
        {
            for (CacheItem item_del : InnerDictionary.values())
            {
                RemoveInternal(item_del);
            }
        }
    }

    public boolean ContainsKey(TKey key)
    {
        synchronized (SyncRoot)
        {
            if (!InnerDictionary.containsKey(key)) return false;
            OnAccess(InnerDictionary.get(key));
            return true;
        }
    }

    public boolean ContainsValue(TValue item)
    {
        return ContainsKey(GetKeyForItem(item));
    }

    public void CopyTo(TValue[] array, int arrayIndex)
    {
        if (array == null) throw new NullPointerException();
        if (arrayIndex < 0) throw new IndexOutOfBoundsException();
        if (arrayIndex + InnerDictionary.size() > array.length) throw new IllegalArgumentException();
        for (TValue item : this)
        {
            array[arrayIndex++] = item;
        }
    }

    public void Dispose()
    {
        Clear();
    }

    // TODO
//    public IEnumerator<TValue> GetEnumerator()
//    {
//        lock (SyncRoot)
//        {
//            foreach (TValue item in InnerDictionary.Values.Select(p => p.Value))
//            {
//                yield return item;
//            }
//        }
//    }
// TODO
//    IEnumerator IEnumerable.GetEnumerator()
//    {
//        return GetEnumerator();
//    }

    protected abstract TKey GetKeyForItem(TValue item);

    public boolean Remove(TKey key)
    {
        synchronized(SyncRoot)
        {
            if (!InnerDictionary.containsKey(key)) return false;
            RemoveInternal(InnerDictionary.get(key));
            return true;
        }
    }

    protected abstract void OnAccess(CacheItem item);

    public boolean RemoveValue(TValue item)
    {
        return Remove(GetKeyForItem(item));
    }

    private void RemoveInternal(CacheItem item)
    {
        InnerDictionary.remove(item.Key);
// TODO
//        IDisposable disposable = item.Value as IDisposable;
//        if (disposable != null)
//        {
//            disposable.Dispose();
//        }
    }

    @Override
    public boolean add(TValue e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends TValue> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<TValue> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    // TODO
//    public boolean TryGet(TKey key, out TValue item)
//    {
//        lock (SyncRoot)
//        {
//            if (InnerDictionary.ContainsKey(key))
//            {
//                OnAccess(InnerDictionary[key]);
//                item = InnerDictionary[key].Value;
//                return true;
//            }
//        }
//        item = default(TValue);
//        return false;
//    }
}
