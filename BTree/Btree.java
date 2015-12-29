/**
 * Created by yuriyarabskyy on 18/12/15.
 */
public class Btree {

    public static final int sideSize = 4;

    private Node root;
    //maximal number of elements in a section
    private int n = sideSize;

    Btree(int n) {
        this.n = n;
        root = new Node(n, null);
    }

    Btree() {
        root = new Node(n, null);
    }

    public static void main(String[] args) {

        Btree tree = new Btree();

        for (int i = 0; i < 50; i++) {

            tree.insert((int)(Math.random()*1000));

        }

        tree.traverse();

    }

    void insert(int elem) {

        Node current = root;

        Pair pair = new Pair(elem);

        while (true) {
            //if the element is already in the tree, stop the insertion
            if (current.find(pair) != -1) return;

            int index = current.findPlace(pair);

           // if (pair.compareTo(current.pairs[index]) > 0 && index > 0 && current.pairs[index].pointer == null) index++;
            if (current.pairs[index].pointer == null) {
                current.insert(pair);
                break;
            }
            else current = current.pairs[index].pointer;

        }

    }

    public void traverse() {
        traverse(root);
        System.out.println();
    }
    //end of the node class

    private void traverse(Node node) {

        if (node.pairs[0].pointer != null) traverse(node.pairs[0].pointer);

        for (int i = 1; i < node.count; i++) {

            System.out.print(node.pairs[i].index + " ");
            if (node.pairs[i].pointer != null) traverse(node.pairs[i].pointer);

        }

    }

    public class Pair implements Comparable<Pair> {
        public   int index;
        public   Node pointer = null;
        Pair(int index, Node pointer) {
            this.index = index;
            this.pointer = pointer;
        }
        Pair() {
            index = 0;
        }
        Pair(int index) {
            this.index = index;
        }
        public boolean equals(Pair other) {
            if (other.index == index) return true;
            return false;
        }
        @Override
        public int compareTo(Pair other) {
            if (index > other.index) return 1;
            if (index < other.index) return -1;
            return 0;
        }
    }

    //****************************************************************
    //Node class doesn't take care of the pointer array, BTree does it
    public class Node {

        Pair[] pairs;

        Node parent = null;

        //how many elements
        int count = 1;

        Node(int n, Node parent) {

            //first element is a phony element
            //pointer attached to a pairs points to the right
            n++;
            pairs = new Pair[n];
            this.parent = parent;
            pairs[0] = new Pair(-1);

        }

        //binary search, if not found return -1
        int find(Pair elem) {

            int index = findPlace(elem);

            if (pairs[index].equals(elem)) return index;
            else return -1;

        }

        //returns the place the the element is supposed to be
        int findPlace(Pair elem) {

            int left = 0;

            int right = count - 1;

            int middle = (left + right) / 2;

            while (right >= left) {

                if (elem.compareTo(pairs[middle]) > 0) left = middle + 1;
                else if (elem.compareTo(pairs[middle]) < 0) right = middle - 1;
                else return middle;

                middle = (left + right) / 2;

            }

            return middle;

        }

        void insert(Pair elem) {

            int index = findPlace(elem);

            //if the element already exists, stop insertion
            if (pairs[index].equals(elem) && count > index) return;

            if (pairs[index].compareTo(elem) < 0 && count > index) index++;

            Pair temp;

            if (index == n) {
                temp = elem;
                count++;
            } else {

                temp = pairs[index];

                pairs[index++] = elem;

                //plus one element
                count++;

                while (index < count) {

                    if (index == n) {
                        break;
                    }

                    Pair secondTemp = pairs[index];

                    pairs[index++] = temp;

                    temp = secondTemp;

                }
            }

            //if the side is full, we need to split it
            if (count == n + 1) {

                //index of the middle
                int middle = count / 2;
                //creating a new node
                Node newNode = new Node(sideSize, parent);

                for (int i = middle + 1; i < count - 1; i++) {
                    newNode.insert(pairs[i]);
                    if (pairs[i].pointer != null)
                        pairs[i].pointer.parent = newNode;
                    pairs[i] = null;
                }
                newNode.insert(temp);
                if (temp.pointer != null)
                    temp.pointer.parent = newNode;

                if (pairs[middle].pointer != null) {
                    newNode.pairs[0].pointer = pairs[middle].pointer;
                    pairs[middle].pointer.parent = newNode;
                }

                pairs[middle].pointer = newNode;

                count = middle;

                if (parent == null) {
                    parent = new Node(sideSize, null);
                    root = parent;
                    parent.pairs[0].pointer = this;
                }

                newNode.parent = parent;

                //now I insert the element
                parent.insert(pairs[middle]);
                pairs[middle] = null;
            }
        }
    }

}
