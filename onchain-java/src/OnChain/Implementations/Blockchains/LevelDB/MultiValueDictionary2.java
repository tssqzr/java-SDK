package OnChain.Implementations.Blockchains.LevelDB;

import java.util.HashMap;
import java.util.Map;

public class MultiValueDictionary2<TKey, TValueKey, TValueData> {
    private Map<TKey, Map<TValueKey, TValueData>> innerMap = new HashMap<TKey, Map<TValueKey, TValueData>>();

    public boolean Add(TKey key, TValueKey itemKey, TValueData itemData) {
        EnsureKey(key);
        if (innerMap.get(key).containsKey(itemKey))
            return false;
        innerMap.get(key).put(itemKey, itemData);
        return true;
    }

    public boolean AddEmpty(TKey key) {
        if (innerMap.containsKey(key))
            return false;
        innerMap.put(key, new HashMap<TValueKey, TValueData>());
        return true;
    }

    private void EnsureKey(TKey key) {
        if (!innerMap.containsKey(key)) {
            innerMap.put(key, new HashMap<TValueKey, TValueData>());
        }
    }

    public boolean Remove(TKey key, TValueKey itemKey) {
        EnsureKey(key);
        return innerMap.get(key).remove(itemKey) != null;
    }
}