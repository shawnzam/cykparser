package cykparser;

import static org.junit.Assert.*;

import java.util.HashSet;



import org.junit.Before;
import org.junit.Test;

public class CYKtest {
	public CYKParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new CYKParser("english.txt");
		parser.tryToOpenFile();
	}

	@Test
	public void testForInput() {
		assertFalse(parser.lines.isEmpty());
	}

	@Test
	public void testcheckInputFile() {
		assertTrue("A -> foo ".matches("^[A-Z] -> ([A-Z]{2}|[a-z]+ )"));
		assertTrue("A -> DD".matches("^[A-Z] -> ([A-Z]{2}|[a-z]+ )"));
		assertFalse("A -> foo".matches("^[A-Z] -> ([A-Z]{2}|[a-z]+ )"));
		assertFalse("A -> DDD".matches("^[A-Z] -> ([A-Z]{2}|[a-z]+ )"));
		assertFalse("C ->AB".matches("^[A-Z] -> ([A-Z]{2}|[a-z]+ )"));
		assertTrue(parser.checkInputFile());
	}

	@Test
	public void testMakeCartensianProduct() {
		HashSet<String> testSet= new HashSet<String>();
		testSet.add("AA");
		testSet.add("AB");
		testSet.add("BA");
		testSet.add("BB");
		HashSet<String> thisSet = parser.makeCartensianProduct("AB,AB");
		assertEquals(testSet, thisSet);
		HashSet<String> testSet2= new HashSet<String>();
		testSet2.add("AB");
		HashSet<String> thisSet2 = parser.makeCartensianProduct("AA,BB");
		assertEquals(testSet2, thisSet2);
		HashSet<String> testSet3= new HashSet<String>();
		testSet3.add("A");
		HashSet<String> thisSet3 = parser.makeCartensianProduct("A");
		assertEquals(testSet3, thisSet3);
		

	}
}
