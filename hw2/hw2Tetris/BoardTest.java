import junit.framework.TestCase;

import java.util.Arrays;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	public void testSample2() {
		b.commit();
		int drop = b.dropHeight(sRotated, 1);
		assertEquals(2, drop);
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample3() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}



	public void testSample4() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		int n = b.clearRows();
		assertEquals(1, n);
	}

	public void testSample5() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		b.undo();
		int ans = b.clearRows();
		b.commit();
		assertEquals(1, ans);
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
	}
	// Makre  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	public void testSample6() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		b.commit();
		System.out.println(Arrays.toString(b.grid[0]));
		System.out.println(Arrays.toString(b.grid[1]));
		System.out.println(Arrays.toString(b.grid[2]));
		assertEquals(result, Board.PLACE_OK);
		b.commit();
		int result1 = b.place(sRotated, 1, 1);
		System.out.println(" ");
		System.out.println(Arrays.toString(b.grid[0]));
		System.out.println(Arrays.toString(b.grid[1]));
		System.out.println(Arrays.toString(b.grid[2]));
		assertEquals(result1, Board.PLACE_BAD);
		b.undo();
		b.commit();
		int result2 = b.place(sRotated, 1, 5);
		System.out.println(" ");
		System.out.println(Arrays.toString(b.grid[0]));
		System.out.println(Arrays.toString(b.grid[1]));
		System.out.println(Arrays.toString(b.grid[2]));
		assertEquals(result2, Board.PLACE_OUT_BOUNDS);
	}
	
}
