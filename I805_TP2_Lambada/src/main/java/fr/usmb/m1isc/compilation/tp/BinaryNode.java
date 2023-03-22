package fr.usmb.m1isc.compilation.tp;

public class BinaryNode<T> {
    private T nodeValue;

    private BinaryNode leftChild;
    private BinaryNode rightChild;

    public BinaryNode(T nodeValue) {
        this.nodeValue = nodeValue;
        this.leftChild = null;
        this.rightChild = null;
    }

    public BinaryNode(T nodeValue, BinaryNode leftChild, BinaryNode rightChild) {
        this.nodeValue = nodeValue;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public BinaryNode(T nodeValue, BinaryNode leftChild) {
        this.nodeValue = nodeValue;
        this.leftChild = leftChild;
        this.rightChild = null;
    }



    public T getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(T nodeValue) {
        this.nodeValue = nodeValue;
    }

    public BinaryNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BinaryNode leftChild) {
        this.leftChild = leftChild;
    }

    public BinaryNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(BinaryNode rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public String toString() {
        if(leftChild == null)
            return nodeValue.toString();

        if(rightChild == null)
            return "(" + nodeValue + " " + leftChild + ")";

        return "(" + nodeValue + " " + leftChild + " " + rightChild + ")";
    }
}
