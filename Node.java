/**
 * Pojo class that maintains Node(i.e building) data of the red black tree
 */
public class Node {
    public Node left;     //pointer to left subtree
    public Node right;    //pointer to right subtree
    public int key;       //building no
    public Building val;  //associated building metadata
    public boolean color; //color of node

    public Node(int key, Building val, boolean color) {
        this.key = key;
        this.val = val;
        this.color = color;
    }
}
