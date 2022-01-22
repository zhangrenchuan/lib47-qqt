package cn.kizzzy.vfs.tree;


import cn.kizzzy.qqt.QqtFile;
import cn.kizzzy.qqt.QqtIdx;
import cn.kizzzy.vfs.ITree;
import cn.kizzzy.vfs.Separator;

public class QqtTreeBuilder extends TreeBuilder {
    
    private final QqtIdx idx;
    
    public QqtTreeBuilder(QqtIdx idx, IdGenerator idGenerator) {
        super(Separator.BACKSLASH_SEPARATOR_LOWERCASE, idGenerator);
        this.idx = idx;
    }
    
    public ITree build() {
        Root root = new Root(idGenerator.getId(), idx.path);
        for (QqtFile file : idx.fileKvs.values()) {
            listImpl(root, root, file);
        }
        return new Tree(root, separator);
    }
    
    private void listImpl(Root root, Node parent, QqtFile item) {
        String[] names = separator.split(item.path);
        for (String name : names) {
            Node child = parent.children.get(name);
            if (child == null) {
                if (name.contains(".")) {
                    Leaf leaf = new Leaf(idGenerator.getId(), name, root.name, item.path, item);
                    root.fileKvs.put(leaf.path, leaf);
                    child = leaf;
                } else {
                    child = new Node(idGenerator.getId(), name);
                }
                root.folderKvs.put(child.id, child);
                parent.children.put(name, child);
            }
            parent = child;
        }
    }
}
