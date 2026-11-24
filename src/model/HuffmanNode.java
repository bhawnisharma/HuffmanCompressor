
package model;

public class HuffmanNode implements Comparable<HuffmanNode> {

private char ch;
private int freq;

private HuffmanNode left;
private HuffmanNode right;

private int order;

    // Constructor for a character node
    public HuffmanNode(char ch, int freq, int order) {
        this.ch = ch;
        this.freq = freq;
        this.order = order;
    }

    // Constructor for a parent node
    public HuffmanNode(HuffmanNode left, HuffmanNode right, int order) {

        this.ch = '\0';

        this.left = left;
        this.right = right;

        this.freq = left.freq + right.freq;
        this.order = order;
    }

    // Check whether this node is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }
    @Override
public int compareTo(HuffmanNode other) {

    if (this.freq != other.freq) {
        return Integer.compare(this.freq, other.freq);
    }

    return Integer.compare(this.order, other.order);
}
public char getCh() {
    return ch;
}

public int getFreq() {
    return freq;
}

public HuffmanNode getLeft() {
    return left;
}

public HuffmanNode getRight() {
    return right;
}

public int getOrder() {
    return order;
}
}


