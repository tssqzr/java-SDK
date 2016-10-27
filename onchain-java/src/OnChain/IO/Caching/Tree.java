package OnChain.IO.Caching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tree<TKey, TValue>
{
    private Map<TKey, TreeNode<TValue>> _nodes = new HashMap<TKey, TreeNode<TValue>>();
    private Map<TKey, TreeNode<TValue>> _leaves = new HashMap<TKey, TreeNode<TValue>>();

    public TValue get(TKey key)
    {
        TreeNode<TValue> node = _nodes.get(key);
        return node == null ? null : node.getItem();
    }
    public void set(TKey key, TValue value)
    {
        // TODO
//        TreeNode<TValue> node = _nodes.get(key);
//      _nodes[key].Item = value;
    }
    

// TODO
//    public IEnumerable<TreeNode<TValue>> Leaves => _leaves.Values;
//    public IReadOnlyDictionary<TKey, TreeNode<TValue>> Nodes => _nodes;

    
    private TreeNode<TValue> root;
    public TreeNode<TValue> getRoot() { return root; }

    public Tree(TKey key, TValue rootValue)
    {
        this.root = new TreeNode<TValue>(rootValue, null);
        _nodes.put(key, this.root);
        _leaves.put(key, this.root);
    }

    public TreeNode<TValue> Add(TKey key, TValue item, TKey parent)
    {
        if (_nodes.containsKey(key)) throw new IllegalArgumentException();
        TreeNode<TValue> node_parent = _nodes.get(parent);
        TreeNode<TValue> node = new TreeNode<TValue>(item, node_parent);
        _nodes.put(key, node);
        _leaves.put(key, node);
        if (_leaves.containsKey(parent)) _leaves.remove(parent);
        return node;
    }

    public TreeNode<TValue> FindCommonNode(TreeNode<TValue>[] nodes)
    {
        if (nodes.length == 0) throw new IllegalArgumentException();
        List<TreeNode<TValue>> nodes2 = new ArrayList<TreeNode<TValue>>();
        nodes2.addAll(Arrays.asList(nodes));

        int minHeight = nodes2.stream().min((l, r)->l.getHeight() - r.getHeight()).get().getHeight();

        for (int i = 0; i < nodes2.size(); i++)
        {
            while (nodes2.get(i).getHeight() > minHeight)
            {
                nodes2.set(i, nodes2.get(i).getParent());
            }
        }
        
        for (nodes2 = nodes2.stream().distinct().collect(Collectors.toList());
                nodes2.size() > 1; nodes2 = nodes2.stream().distinct().collect(Collectors.toList()))
        {
            for (int i = 0; i < nodes2.size(); i++)
            {
                nodes2.set(i, nodes2.get(i).getParent());
            }
        }
        return nodes2.get(0);
    }
}
