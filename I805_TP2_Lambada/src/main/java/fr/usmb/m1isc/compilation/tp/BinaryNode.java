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
                case ENTIER -> Main.ASM.code.add("mov eax, " + nodeValue);
                case VARIABLE -> {
                    Main.ASM.code.add("mov "+nodeValue+", eax");
                    Main.ASM.data.add(nodeValue + " DD");
                }
                case NIL -> Main.ASM.code.add("nil");
                case INPUT -> Main.ASM.code.add("");
                case IDENT -> {
                    Main.ASM.code.add("mov eax, "+ this.nodeValue);
                    Main.ASM.code.add("push eax");
                }
            }
        }

        if(this.leftChild != null) {
            // Noeud avec un seul fils
            //System.out.println("NODE : " + nodeValue);
            if(this.rightChild == null) {
                switch (nodeValue) {
                    case "-" -> Main.ASM.code.add("-"+this.leftChild);
                    case "OUTPUT" -> Main.ASM.code.add("OUTPUT");
                    case "()" -> this.leftChild.toAsm();
                }
            }
            // Noeud avec deux fils
            else {
                switch (nodeValue) {
                    case "+"-> {
                        this.leftChild.toAsm();
                        if(this.leftChild.terminalType != TerminalType.IDENT){
                            Main.ASM.code.add("push eax");
                        }
                        this.rightChild.toAsm();
                        if(this.rightChild.terminalType == TerminalType.IDENT){
                            Main.ASM.code.add("pop eax");
                        }
                        Main.ASM.code.add("pop ebx");

                        Main.ASM.code.add("add eax, ebx");
                    }
                    case "-" -> {
                        this.leftChild.toAsm();
                        if(this.leftChild.terminalType != TerminalType.IDENT){
                            Main.ASM.code.add("push eax");
                        }
                        this.rightChild.toAsm();
                        if(this.rightChild.terminalType == TerminalType.IDENT){
                            Main.ASM.code.add("pop eax");
                        }
                        Main.ASM.code.add("pop ebx");

                        Main.ASM.code.add("sub eax, ebx");
                    }
                    case "*" -> {
                        this.leftChild.toAsm();
                        if(this.leftChild.terminalType != TerminalType.IDENT){
                            Main.ASM.code.add("push eax");
                        }
                        this.rightChild.toAsm();
                        if(this.rightChild.terminalType == TerminalType.IDENT){
                            Main.ASM.code.add("pop eax");
                        }
                        Main.ASM.code.add("pop ebx");
                        Main.ASM.code.add("mul eax, ebx");
                    }
                    case "/" -> {
                        this.leftChild.toAsm();
                        if(this.leftChild.terminalType != TerminalType.IDENT){
                            Main.ASM.code.add("push eax");
                        }
                        this.rightChild.toAsm();
                        if(this.rightChild.terminalType == TerminalType.IDENT){
                            Main.ASM.code.add("pop eax");
                        }
                        Main.ASM.code.add("pop ebx");

                        Main.ASM.code.add("div ebx, eax");
                        Main.ASM.code.add("mov eax, ebx");
                    }
                    case "LET" -> {
                        this.rightChild.toAsm();
                        this.leftChild.toAsm();
                    }
                    case ";" -> {
                        this.leftChild.toAsm();
                        this.rightChild.toAsm();
                    }
                    case "WHILE" -> {

                    }
                    case "IF" -> {

                    }
                    case "ELSE" -> {

                    }
                }
            }
        }
    }

    enum TerminalType {
        NONE, ENTIER, VARIABLE, NIL, INPUT, IDENT
    }
}
