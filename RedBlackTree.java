public class RedBlackTree {
    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public RedBlackTree() {
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // check if tree is empty
    public boolean isEmpty() {
        return root == null;
    }

    // returns the building metadata associated with the key i.e building no
    public Building get(int key) {
        return get(root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private Building get(Node x, int key) {
        while (x != null) {
            int cmp = Integer.compare(key, x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }

    // check if red black tree contains building with build no: key
    public boolean contains(int key) {
        return get(key) != null;
    }

    // Insert building into red black tree with building no as key; updates building if key already exists
    public void put(int key, Building val) {
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        root.color = BLACK;
        assert check();
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key, Building val) {
        if (h == null) return new Node(key, val, RED);

        int cmp = Integer.compare(key, h.key);
        if (cmp < 0) h.left = put(h.left, key, val);
        else if (cmp > 0) h.right = put(h.right, key, val);
        else h.val = val;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    // deletes building with specified key i.e building no
    public void delete(int key) {
        if (!contains(key)) return;

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
        assert check();
    }

    // delete the node with the given key rooted at h
    private Node delete(Node h, int key) {
        assert get(h, key) != null;

        if (key < h.key) {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key == h.key && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key == h.key) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                h.right = deleteMin(h.right);
            } else h.right = delete(h.right, key);
        }
        return balance(h);
    }

    // delete the node with the minimum key rooted at h
    private Node deleteMin(Node h) {
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }

    // make left leaning link lean to the right
    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        return x;
    }

    // make right leaning link lean to the left
    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        assert (h != null) && (h.left != null) && (h.right != null);
        assert (!isRed(h) && isRed(h.left) && isRed(h.right))
                || (isRed(h) && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        assert (h != null);
        assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        assert (h != null);
        assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // maintain red black tree invariant property
    private Node balance(Node h) {
        assert (h != null);
        if (isRed(h.right)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    // smallest key in subtree rooted at x
    private Node min(Node x) {
        assert x != null;
        if (x.left == null) return x;
        else return min(x.left);
    }

    // largest key in subtree rooted at x
    private Node max(Node x) {
        assert x != null;
        if (x.right == null) return x;
        else return max(x.right);
    }

    // check if bst and red black invariants have been maintained
    private boolean check() {
        return isBST() && isBalanced();
    }

    // check whether binary tree satisfies symmetric order
    private boolean isBST() {
        return isBST(root, 0, 0);
    }

    private boolean isBST(Node x, int min, int max) {
        if (x == null) return true;
        if (min != 0 && x.key <= min) return false;
        if (max != 0 && x.key >= max) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // check if all paths from root to leaf have same no of black links
    private boolean isBalanced() {
        int black = 0;     // number of black links on path from root to min
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    private boolean isBalanced(Node x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    }

    //print a particular node(building)
    public void print(int key) {
        Building val = get(key);
        if (val == null)
            System.out.println("(0,0,0)"); // output if key doesn't exist
        else
            System.out.println("(" + val.getBuildingNo() + "," + val.getExecutedTime() + "," + val.getTotalTime() + ")");
    }

    //traverse only those subtrees that have keys(building no) in the range of low and hi and print building
    private void print(Node x, int low, int hi) {
        if (x == null)
            return;
        // skip left subtree traversal if key is lesser than low
        if (x.key > low)
            print(x.left, low, hi);
        if (x.key >= low && x.key <= hi) {
            Building val = x.val;
            // check for max element in left subtree is in the given range and print ','
            if (x.left != null && max(x.left).key >= low)
                System.out.print(",");
            System.out.print("(" + val.getBuildingNo() + "," + val.getExecutedTime() + "," + val.getTotalTime() + ")");
            // check for min element in right subtree is in the given range and print ','
            if (x.right != null && min(x.right).key <= hi)
                System.out.print(",");
        }
        // skip right subtree traversal if key is higher than hi
        if (x.key < hi)
            print(x.right, low, hi);
    }

    //prints all nodes(buildings) in the range of low and hi
    public void print(int low, int hi) {
        if (root == null)
            System.out.print("(0,0,0)");
        else
            print(root, low, hi);
    }
}
