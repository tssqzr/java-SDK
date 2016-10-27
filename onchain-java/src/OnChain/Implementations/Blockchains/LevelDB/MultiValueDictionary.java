package OnChain.Implementations.Blockchains.LevelDB;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiValueDictionary<TKey, TValue> {
    private Map<TKey, Set<TValue>> innerMap = new HashMap<TKey, Set<TValue>>();

    public boolean Add(TKey key, TValue item) {
        EnsureKey(key);
        return innerMap.get(key).add(item);
    }

    public boolean AddEmpty(TKey key) {
        if (innerMap.containsKey(key))
            return false;
        innerMap.put(key, new HashSet<TValue>());
        return true;
    }

    public void AddRange(TKey key, Collection<TValue> items) {
        EnsureKey(key);
        innerMap.get(key).addAll(items);
    }

    private void EnsureKey(TKey key) {
        if (!innerMap.containsKey(key)) {
            innerMap.put(key, new HashSet<TValue>());
        }
    }

    public boolean Remove(TKey key, TValue item) {
        EnsureKey(key);
        return innerMap.get(key).remove(item);
    }
}
