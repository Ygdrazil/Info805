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
                case ENTIER -> Main.ASM.put("mov eax, " + nodeValue);
                case VARIABLE -> {
                    Main.ASM.put("mov "+nodeValue+", eax");
                    Main.ASM.addVariable(nodeValue);
                }
                case NIL -> Main.ASM.put("nil");
                case INPUT -> {
                    Main.ASM.put("in eax");
                }
                case IDENT -> {
                    Main.ASM.put("mov eax, "+ this.nodeValue);
                    Main.ASM.put("push eax");
                }
            }
        }

        if(this.leftChild != null) {
            // Noeud avec un seul fils
            //System.out.println("NODE : " + nodeValue);
            if(this.rightChild == null) {
                switch (nodeValue) {
                    case "-" -> Main.ASM.put("-"+this.leftChild);
                    case "OUTPUT" -> {
                        Main.ASM.put("mov eax, a");
                        Main.ASM.put("out eax");
                    }
                    case "()" -> this.leftChild.toAsm();
                }
            }
            // Noeud avec deux fils
            else {
                switch (nodeValue) {
                    case "+"-> {
                        handleChildren();
                        Main.ASM.put("add eax, ebx");
                    }
                    case "-" -> {
                        handleChildren();
                        Main.ASM.put("sub eax, ebx");
                    }
                    case "*" -> {
                        handleChildren();
                        Main.ASM.put("mul eax, ebx");
                    }
                    case "/" -> {
                        handleChildren();
                        Main.ASM.put("div ebx, eax");
                        Main.ASM.put("mov eax, ebx");
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
                        Main.ASM.state = "debut_while_1";
                        this.leftChild.toAsm();
                        Main.ASM.state = "debut_while_1";
                        Main.ASM.put("mov eax,1");
                        Main.ASM.put("jmp corps_while_1");
                        Main.ASM.state = "corps_while_1";
                        Main.ASM.put("jz sortie_while_1");
                        this.rightChild.toAsm();
                        Main.ASM.put("jmp debut_while_1");
                        Main.ASM.state = "sortie_while_1";
                    }
                    case "IF" -> {

                    }
                    case "ELSE" -> {

                    }
                    case ">=" -> {
                        handleChildren();
                        Main.ASM.put("sub eax,ebx");
                        Main.ASM.put("jge faux_gte_1");
                        setFaux("faux_gte_1");
                    }
                    case ">" -> {
                        handleChildren();
                        Main.ASM.put("sub eax,ebx");
                        Main.ASM.put("jg faux_gt_1");
                        setFaux("faux_gt_1");
                    }
                    case "<=" -> {
                        handleChildren();
                        Main.ASM.put("sub eax,ebx");
                        Main.ASM.put("jle faux_lte_1");
                        setFaux("faux_lte_1");
                    }
                    case "<" -> {
                        handleChildren();
                        Main.ASM.put("sub eax,ebx");
                        Main.ASM.put("jl faux_lt_1");
                        setFaux("faux_lt_1");

                    }
                }
            }
        }
    }

    enum TerminalType {
        NONE, ENTIER, VARIABLE, NIL, INPUT, IDENT
    }

    private void handleChildren() {
        this.leftChild.toAsm();
        if(this.leftChild.terminalType != TerminalType.IDENT){
            Main.ASM.put("push eax");
        }
        this.rightChild.toAsm();
        if(this.rightChild.terminalType == TerminalType.IDENT){
            Main.ASM.put("pop eax");
        }
        Main.ASM.put("pop ebx");
    }

    private void setFaux(String state) {
        Main.ASM.state = state;
        Main.ASM.put("mov eax,0");
    }
}
