//import javafx.util.Pair;
import kotlin.Pair;

import javax.sound.midi.SysexMessage;
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	
	private List<Spot> board;
	private List<Integer> solution;
	private List<Pair<Spot, Integer>> spots;
	private long startTime;
	private long endTime;
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		startTime = System.currentTimeMillis();
		endTime = 0;
		board = new ArrayList<>();
		solution = new ArrayList<>();
		for(int i = 0; i < ints.length; i++){
			for(int j = 0; j < ints[0].length; j++){
				Spot s = new Spot(ints[i][j], i*9 + j);
				board.add(s);
			}
		}

		creatSpots();
	}
	
	private void creatSpots(){
		spots = new ArrayList<>();
		for(int i = 0; i < board.size(); i++){
			int cur = 0;
			Spot s = board.get(i);
			if(s.get() == 0) {
//				System.out.print(" " + s.index+"-" +i);
				cur = s.getValues(board, i).size();
				int index = 0;
				for (int j = 0; j < spots.size(); j++) {
					if (spots.get(j).getSecond() > cur) {
						index = j;
						break;
					}
					index++;
				}
				spots.add(index, new Pair<Spot, Integer>(s, cur));
			}
		}
		///just for test
//		System.out.println();
//		for(int i = 0; i < spots.size(); i++){
//			System.out.print(" " + spots.get(i).getFirst().getIndex() + "-" + spots.get(i).getSecond());
//		}
//		System.out.println();
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		int ans = recSolve(0, 0);
		endTime = System.currentTimeMillis();
		return  ans;
	}

	private int recSolve( int index ,int ans){
//		System.out.print(" " + index);
		if(index == spots.size()){
//			System.out.println("WOOOHOOOOO FOUND ONE ");
			if(this.solution.size() == 0){
				solution = new ArrayList<>();
				for(int i = 0; i < this.board.size(); i++){
					solution.add(board.get(i).get());
				}
			}
			return ans += 1;
		}
		Spot s = spots.get(index).getFirst();
//		System.out.print(" REAL INDEX " + s.getIndex() + " ");
		List<Integer> vals = s.getValues(board, s.getIndex());
		if(vals == null){
//			System.out.println("WOOPS");
			return 0;
		}
//		System.out.println();
		int prev = ans;
		for(int i = 0; i < vals.size(); i++){
//			System.out.println(" " + i);
			s.set(vals.get(i));
			ans += recSolve(index + 1, prev);
		}
		s.set(0);
		return ans;
	}
	
	public String getSolutionText() {
		if(solution != null) {
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < this.solution.size(); i++) {
				if(i % 9 == 0 && i != 0) s.append('\n');
				s.append(solution.get(i));
				s.append(" ");
			}
			return s.toString();
		}
		return "";
	}
	
	public long getElapsed() {
		return endTime - startTime;
	}

	private class Spot{

		private int value;
		private int index;



		public Spot(int i, int index) {
			value = i;
			this.index = index;
		}

		@Override
		public String toString() {
			return Integer.toString(this.value);
		}

		public int getIndex(){
			return this.index;
		}

		public int get(){
			return value;
		}

		public void set(int i){
			value = i;
		}

		public List<Integer> getValues(List<Spot> board, int index){
//			if(index == 76) System.out.println("SPECIAL CASE \n");
			List<Integer> vals = new ArrayList<>();
			for(int i = 1; i <= 9; i++){
				vals.add(i);
			}

//			System.out.println("LENGTH " + board.size());
//			System.out.println("INDEX " + index);
			int row = index / 9;
//			System.out.println("ROW " + row);
			int startOfX = index - (index % 9);
			int startOfY = index % 9;
			int startOfRect = (row - (row % 3)) * 9 + (startOfY - (startOfY % 3));
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					if(vals.size() == 0) return null;
//					System.out.println("POS INDEX " + ((startOfRect + 9*i) + j) );
					Spot s = board.get((startOfRect + 9*i) + j);
//					if(index == 76 && s.get() != 0) System.out.println(s.get());
					vals.remove(new Integer(s.get()));
				}
			}

			for(int i = 0; i < 9; i++){
				if(vals.size() == 0) return null;
				Spot s = board.get(startOfX + i);
//				if(index == 76 && s.get() != 0) System.out.println(s.get());
				vals.remove(new Integer(s.get()));
			}


			for(int i = 0; i < 9; i++){
				if(vals.size() == 0) return null;
				Spot s = board.get(startOfY + 9*i);
//				if(index == 76 && s.get() != 0) System.out.println(s.get());
				vals.remove(new Integer(s.get()));
			}
			if(vals.size() != 0) return vals;
			return null;
		}
	}

}
