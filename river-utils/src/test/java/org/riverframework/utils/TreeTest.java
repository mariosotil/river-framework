package org.riverframework.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.riverframework.RiverException;

public class TreeTest {
	@Test
	public void testTree() {
		Tree<String, Integer> tree = new Tree<String, Integer>(String.class, Integer.class);
		tree.put("A", "B", "C", 10);
		tree.put("A", "B", "D", 20);
		tree.put("A", "B", "E", 30);
		tree.put("A", "B", 40);
		tree.put("A", "F", 50);
		tree.put("A", "F", "G", 80);

		Integer result = tree.get("A", "B");
		assertTrue("Could not get the expected result for A, B", result == 40);
		
		result = tree.get("A");
		assertTrue("Could not get the expected result for A", result == null);
		
		result = tree.get("A", "F", "G");
		assertTrue("Could not get the expected result for A, F, G", result == 80);

		result = tree.get("A", "F", "G", "Z");
		assertTrue("Could not get the expected result for A, F, G, Z", result == null);		
	}

	@Test(expected=RiverException.class)
	public void testBadKey() { 
		Tree<String, Integer> tree = new Tree<String, Integer>(String.class, Integer.class);
		tree.put("A", new ArrayList<Integer>(), 50);
	}

	@Test(expected=RiverException.class)
	public void testBadValue() { 
		Tree<String, Integer> tree = new Tree<String, Integer>(String.class, Integer.class);
		tree.put("A", "B", new ArrayList<Integer>());
	}
}
