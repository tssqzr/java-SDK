package OnChain.IO.Caching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNode<TValue>
{
    private List<TreeNode<TValue>> _children = new ArrayList<TreeNode<TValue>>();

    private TValue item;
    private int height;
    private TreeNode<TValue> parent;
    
    public TreeNode(TValue item, TreeNode<TValue> parent)
    {
        this.item = item;
        this.height = parent != null ? parent.height + 1 : 0;
        this.parent = parent;
        if (parent != null)
            parent._children.add(this);
    }


    public TValue getItem() {
        return item;
    }


    public void setItem(TValue item) {
        this.item = item;
    }


    public int getHeight() {
        return height;
    }


    void setHeight(int height) {
        this.height = height;
    }


    public TreeNode<TValue> getParent() {
        return parent;
    }


    void setParent(TreeNode<TValue> parent) {
        this.parent = parent;
    }


//  public IReadOnlyCollection<TreeNode<TValue>> Children => _children;
    public List<TreeNode<TValue>> getChildren() {
        return Collections.unmodifiableList(_children);
    }
}
