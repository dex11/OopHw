//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

import java.util.Arrays;

public class TetrisGrid {
	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = new boolean[grid.length][grid[0].length];
		for(int row = 0; row < grid.length ; row++){
			for( int col = 0; col < grid[0].length; col++){
				this.grid[row][col] = grid[row][col];
			}
		}
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		for(int col = 0; col < this.grid[0].length; ++col){
			boolean isGood = true;
			for(int row = 0; row < this.grid.length; row++){
				if(this.grid[row][col] == false) isGood = false;
			}
			if(isGood){
				moveCol(col);
				col = col - 1;
			}
		}
	}

	private void moveCol( int col){
		for(int i = col + 1; i < this.grid[0].length; i++){
			for(int row = 0; row < this.grid.length; row++){
				this.grid[row][i - 1] = this.grid[row][i];
			}
		}
		//add free spaces at the end
		for(int row = 0; row < this.grid.length; row++){
			this.grid[row][this.grid[0].length - 1] = false;
		}
	}

	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid; // YOUR CODE HERE
	}
}
