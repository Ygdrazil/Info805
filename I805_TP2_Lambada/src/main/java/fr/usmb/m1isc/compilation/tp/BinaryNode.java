package fr.usmb.m1isc.compilation.tp;

public class BinaryNode {
    private String nodeValue;

    private BinaryNode leftChild;
    private BinaryNode rightChild;

    private TerminalType terminalType;

    public BinaryNode(String nodeValue, TerminalType terminalType) {
        this.nodeValue = nodeValue;
        this.leftChild = null;
        this.rightChild = null;
        this.terminalType = terminalType;
    }

    public BinaryNode(String nodeValue, BinaryNode leftChild, BinaryNode rightChild) {
        this.nodeValue = nodeValue;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        terminalType = TerminalType.NONE;
    }

    public BinaryNode(String nodeValue, BinaryNode leftChild) {
        this.nodeValue = nodeValue;
        this.leftChild = leftChild;
        this.rightChild = null;
        terminalType = TerminalType.NONE;
    }

    @Override
    public String toString() {
        if(leftChild == null)
            return nodeValue;

        if(rightChild == null)
            return "(" + nodeValue + " " + leftChild + ")";

        return "(" + nodeValue + " " + leftChild + " " + rightChild + ")";
    }

    public void toAsm() {

        // Noeud sans fils
        if(this.terminalType != TerminalType.NONE ) {
            switch (this.terminalType) {
                case ENTIER -> System.out.println("mov eax, " + nodeValue);
                case VARIABLE -> System.out.println("mov "+nodeValue+", eax");
                case NIL -> System.err.println("nil");
                case INPUT -> System.err.println("input");
            }
        }

        if(this.leftChild != null) {
            // Noeud avec un seul fils
            if(this.rightChild == null) {
                switch (nodeValue) {
                    case "-" -> System.out.println("-"+this.leftChild);
                    case "OUTPUT" -> System.err.println("OUTPUT");
                    case "()" -> this.leftChild.toAsm();
                }
            }
            // Noeud avec deux fils
            else {
                switch (nodeValue) {
                    case "+"-> {
                        this.leftChild.toAsm();
                        System.out.println("push eax");
                        this.rightChild.toAsm();
                        System.out.println("pop ebx");

                        System.out.println("add eax, ebx");
                    }
                    case "-" -> {
                        this.leftChild.toAsm();
                        System.out.println("push eax");
                        this.rightChild.toAsm();
                        System.out.println("pop ebx");

                        System.out.println("sub eax, ebx");
                    }
                    case "*" -> {
                        this.leftChild.toAsm();
                        System.out.println("push eax");
                        this.rightChild.toAsm();
                        System.out.println("pop ebx");

                        System.out.println("mul eax, ebx");
                    }
                    case "/" -> {
                        this.leftChild.toAsm();
                        System.out.println("push eax");
                        this.rightChild.toAsm();
                        System.out.println("pop ebx");

                        System.out.println("div ebx, eax");
                        System.out.println("mov eax, ebx");
                    }
                    case "LET" -> {
                        this.rightChild.toAsm();
                        this.leftChild.toAsm();
                        System.out.println("mov eax, "+ this.leftChild.nodeValue);
                        System.out.println("push eax");
                    }
                }
            }
        }
    }

    enum TerminalType {
        NONE, ENTIER, VARIABLE, NIL, INPUT
    }
}
