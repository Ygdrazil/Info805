package fr.usmb.m1isc.compilation.tp;

import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception  {
		LexicalAnalyzer yy;
		if (args.length > 0)
			yy = new LexicalAnalyzer(new FileReader(args[0])) ;
		else
			yy = new LexicalAnalyzer(new InputStreamReader(System.in)) ;
		parser p = new parser (yy);
		Symbol s = p.parse();
		BinaryNode arbre = (BinaryNode) s.value;
		System.out.println(arbre.toString());
		arbre.toAsm();
		ASM.print();
    /*	DATA SEGMENT
			prixHt DD
			prixTtc DD
		DATA ENDS
		CODE SEGMENT
			mov eax, 200
			mov prixHt, eax
			mov eax, prixHt
			push eax
			mov eax, 119
			pop ebx
			mul eax, ebx
			push eax
			mov eax, 100
			pop ebx
			div ebx, eax
			mov eax, ebx
			mov prixTtc, eax
	    CODE ENDS*/
	}

	public static class ASM {
		public static ArrayList<String> code = new ArrayList<>();
		public static ArrayList<String> data = new ArrayList<>();

		public static void print() {
			System.out.println("DATA SEGMENT");
			for( String s : data) {
				System.out.println("	" + s);
			}
			System.out.println("DATA ENDS\nCODE SEGMENT");
			for( String s : code) {
				System.out.println("	" + s);
			}
			System.out.println("CODE ENDS");
		}
	}

}
