package org.pdpblr.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class T {
	public static void main(String[] args) {
		String str1 =  "hello world";
			String str2 = "wheel falls of a canoe, how many ball bearings does it take to fill";
String str3 = " up a water buffalo?";
		Vector<String> a = new Vector<String>();
		a.add(str1);
		a.add(str2);
		a.add(str3);
		printMostFrequentWords(a);
	}
	
	static void printMostFrequentWords(Vector<String >a) {
		for (String astr : a) {
			int maxfeq = 0;
			Hashtable<Character, Integer> table = new Hashtable<Character, Integer>();
			for (int i = 0; i < astr.length(); i++) {
				Character ch = astr.charAt(i);
				Integer count = table.get(ch);
				if (count == null) {
					table.put(ch, new Integer(1));
				} else {
					table.put(ch, new Integer(count.intValue() + 1));
				}
			}
			Integer max = Collections.max(table.values());
			
			for (int i = 0; i < astr.length(); i++) {
				Character ch = astr.charAt(i);
				Integer count = table.get(ch);
				if (max == count) {
					System.out.println(ch.toString() + " - "+ max);
					break;
				}
			}
		}
	}
}
