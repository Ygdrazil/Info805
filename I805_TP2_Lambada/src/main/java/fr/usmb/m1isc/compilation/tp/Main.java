package fr.usmb.m1isc.compilation.tp;

import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
	}

	public static class ASM {
		public static int nbWhile = 0;

		public static LinkedHashMap<String,ArrayList<String>> instructions = new LinkedHashMap<>();
		public static ArrayList<String> data = new ArrayList<>();

		public static String state = "code";

		public static void print() {
			System.out.println("DATA SEGMENT");
			for( String s : data) {
				System.out.println("	" + s);
			}
			System.out.println("DATA ENDS\nCODE SEGMENT");
			for( String key : instructions.keySet()){
				if(!key.equals("code"))System.out.println(key+":");
				for( String s : instructions.get(key)) {
					System.out.println("	" + s);
				}
			}
			System.out.println("CODE ENDS");
		}

		public static void put(String line) {
			if(!instructions.containsKey(state)){
				instructions.put(state, new ArrayList<>());
			}
			ArrayList<String> map = instructions.get(state);
			map.add(line);
		}

		public static void addVariable(String var) {
			if(!data.contains(var + " DD")){
				data.add(var + " DD");
			}
		}
	}

}
