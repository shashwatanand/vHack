package org.pdpblr.util;

public class A extends Test {
	public A(String abc) {
		System.out.println("A contsructor");
		
	}
	public static void main(String[] a) {
		A obj = new A("xyz");
		System.out.println(obj);
	}
}
