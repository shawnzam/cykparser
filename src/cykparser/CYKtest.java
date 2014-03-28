package cykparser;

import static org.junit.Assert.*;



import org.junit.Before;
import org.junit.Test;

public class CYKtest {
	public CYKParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new CYKParser("english.txt");
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
		assertTrue(parser.checkInputFile());
	}

	@Test
	public void makeGrammer() {
		parser.makeGrammer();
//		assertTrue(parser.grammerMap.get('W').variables.equals("OL"));
	}
}
