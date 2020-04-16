// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int widths[];
	private int heights[];
	public boolean[][] grid;
	private boolean[][]posGrid;
	private boolean DEBUG = true;
	boolean committed;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		posGrid = new boolean[width][height];
		committed = true;
		
		this.widths = new int[height];
		this.heights = new int[width];
		for(int i = 0; i < height; i++){
			this.widths[i] = 0;
		}
		for(int i = 0; i < width; i++){
			this.heights[i] = 0;
		}
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		int max = 0;
		for(int i = 0; i < this.width; i++){
			if(this.heights[i] > max) max = this.heights[i];
		}
		return max;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
//		System.out.println("before san");
//		System.out.println(Arrays.toString(this.grid[0]));
//		System.out.println(Arrays.toString(this.grid[1]));
//		System.out.println(Arrays.toString(this.grid[2]));
		if (DEBUG) {
			int[] compHeights = new int[this.width];
			int[] compWidths = new int[this.height];
			for(int row = 0; row < this.width; row++) {
				int curHeight = 0;
				for (int i = 0; i < this.height; i++) {
					if(this.grid[row][i]) curHeight = i + 1 ;
				}
				compHeights[row] = curHeight;
			}
//			System.out.println("COMP HEIGHTS " + Arrays.toString(compHeights) +  " TO " + Arrays.toString(this.heights));

			for (int i = 0; i < this.height; i++) {
				int curWidth = 0;
				for(int row = 0; row < this.width; row++) {
					if(this.grid[row][i]) curWidth++;
				}
				compWidths[i] = curWidth;
			}
//			System.out.println("COMP WIDTH " + Arrays.toString(compWidths) +  " TO " + Arrays.toString(this.widths));
			if(!Arrays.equals(this.heights, compHeights)) throw new RuntimeException("WRONG HEIGHTS");
			if(!Arrays.equals(this.widths, compWidths)) throw new RuntimeException("WRONG WIDTHS");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int stopY = 0;
		int y = 0;
		for(int i = 0; i < piece.getWidth(); i++){
			if(this.heights[x + i] + piece.getSkirt()[i] > stopY){
				stopY = this.heights[x + i] + piece.getSkirt()[i];
				y = stopY + (piece.getSkirt()[i] - piece.getSkirt()[0] - 1);
			}
		}
		return y;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		int ans = this.heights[x];
		return ans;
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return this.widths[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x >= this.width || y >= this.height) return true;
		return this.grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		gridClon(grid, posGrid);
		TPoint[] body = piece.getBody();
		for(int i = 0; i < piece.getBody().length; ++i){
			int pX = body[i].x;
			int pY = body[i].y;
			if(x + pX >= this.width || y + pY >= this.height || x + pX < 0 || y + pY < 0){
				committed = false;
				return PLACE_OUT_BOUNDS;
			}
			if(this.grid[x + pX][y + pY]){
				committed = false;
				return PLACE_BAD;
			}
			//this.heights[x + pX] > y + piece.getSkirt()[pX] + 1 ||
			this.grid[x + pX][y + pY] = true;
			this.widths[y + pY]++;
			this.heights[x + pX]++;
			if(this.widths[y + pY] == this.width){
				committed = false;
				return PLACE_ROW_FILLED;
			}
		}
		committed = false;
		return PLACE_OK;
	}

	private void gridClon(boolean[][] src, boolean[][]dest){
		for(int row = 0; row < this.width; row++) {
			for (int i = 0; i < this.height; i++) {
				dest[row][i] = src[row][i];
			}
		}
	}

	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		gridClon(grid, posGrid);
		for(int col = 0; col < this.height; ++col){
			boolean isGood = true;
			for(int row = 0; row < this.width; row++){
				if(this.grid[row][col] == false) isGood = false;
			}
			if(isGood){
				moveCol(col);
				rowsCleared++;
				col = col - 1;

			}
		}
		updateHeights();
		updateWidth();
		committed = false;
		sanityCheck();
		return rowsCleared;
	}

	private void updateHeights(){
		for(int row = 0; row < this.width; row++) {
			int curHeight = 0;
			for (int i = 0; i < this.height; i++) {
				if(this.grid[row][i]) curHeight = i + 1 ;
			}
			this.heights[row] = curHeight;
		}
	}

	private void updateWidth(){
		for (int i = 0; i < this.height; i++) {
			int curWidth = 0;
			for(int row = 0; row < this.width; row++) {
				if(this.grid[row][i]) curWidth++;
			}
			this.widths[i] = curWidth;
		}
	}

	private void moveCol( int col){
		for(int row = 0; row < this.width; row++){
			for(int i = col + 1; i < this.height; i++){
				this.grid[row][i - 1] = this.grid[row][i];
				this.widths[i-1] = this.widths[i];
			}
		}
		//add free spaces at the end
		for(int row = 0; row < this.width; row++){
			this.grid[row][this.height - 1] = false;
		}
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (!committed) {
			gridClon(posGrid, grid);
			updateHeights();
			updateWidth();
			committed = true;
		}
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}


}


