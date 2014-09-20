package org.pdpblr.util;

import java.util.ArrayList;

public class Test {
	/*String abc;
	static int x = 1;
	public Test() {
		System.out.println("default constructor");
	}
	
	public Test(String abc) {
		System.out.println("paramter constructor");
		//abc = abc;
	}*/
	
	/*static {
		System.out.println("static");
	}
	{
		System.out.println("block");
	}*/
	/*@SuppressWarnings("finally")
	static void t(int i) {
		x++; 
		i++;
		int a[][] = new int[5][5];
		int[] b[] = new int[5][5];
		int[] c[] = new int[5][];
		ArrayList<String> list = new ArrayList<String>();
		list.add("Shashwat");
		list.add("Samiksha");
		
		try {
			System.out.println(2);
			throw new Exception();
		} catch (Exception ex) {
			System.out.println("Catch Block " + 3);
			return;
		} finally {
			System.out.println("Finally Block " + 4);
			return;
		}
	}*/
	public static void main(String[] args) {
		/*int y = 3;
		t(y);
		System.out.println(x + " , " +  y);*/
		
		char[] s = {'a', 'b', 'c', 'd'};
		char[] r = {'b'};
		int src, dst;
		boolean[] flags = new boolean[128];
		for (src = 0; src < s.length - 1; ++src) {
			flags[(int)r[src]] = true;
		}
		
		src = 0;
		dst = 0;
		while (src < s.length) {
			if (!flags[s[src]]) {
				s[dst++] = s[src];
			}
			++src;
		}
		System.out.println(new String(s, 0, dst));
	}
}
