package me.laudwilliam.resumeprojects.datastructures;

@SuppressWarnings("rawtypes")
public class Tree<T> {
    public static boolean isLeaf(Tree.Node node) {
        return node.left == null && node.right == null;
    }

    public static Tree.Node getParentOfLeftMostLeaf(Tree.Node tree) {
        while (!Tree.isLeaf(tree.getLeft()))
            tree = tree.left;
        return tree;
    }

    public static Tree.Node getParentOfRightMostLeaf(Tree.Node tree) {
        while (!Tree.isLeaf(tree.getRight()))
            tree = tree.right;
        return tree;
    }

    /**
     * <h1> Display Beachline in Console</h1>
     * The display function takes the nodes of the beachline
     * and formats them into a string structure, that makes
     * visualizing the tree much easier
     *
     * @return a StringBuilder, that contains the tree structure in a string format
     */
    StringBuilder display(Node root) {
        StringBuilder results = new StringBuilder();
        traversePreOrder(results, "", "", root);
        return results;
    }

    /**
     * @param sb      the string builder to append out put to
     * @param padding padding for tree
     * @param pointer shape of the pointer
     * @param node    the root node of tree to traverse
     * @author Baeldung.com
     * @see <a href="https://www.baeldung.com/java-print-binary-tree-diagram">
     * How to print a binary tree Java</a>
     */
    private void traversePreOrder(StringBuilder sb, String padding, String pointer, Node node) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.data.toString());
            sb.append("\n");

            String paddingForBoth = padding + "|  ";
            String pointerForRight = "└──";
            String pointerForLeft = (node.right != null) ? "├──" : "└──";

            traversePreOrder(sb, paddingForBoth, pointerForLeft, node.left);
            traversePreOrder(sb, paddingForBoth, pointerForRight, node.right);
        }
    }

    public static class Node {
        private Object data;
        private Node left;
        private Node right;

        public Node(Object data) {
            this.data = data;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
