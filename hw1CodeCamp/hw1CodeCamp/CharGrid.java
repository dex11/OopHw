import java.util.ArrayList;
import java.util.HashSet;

// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int smallestX = grid.length;
		int largestX = 0;
		int smallestY = grid[0].length;
		int largestY = 0;
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				if(grid[row][col] == ch){
					if(col < smallestX) smallestX = col;
					if(col > largestX) largestX = col;

					if(row < smallestY) smallestY = row;
					if(row > largestY) largestY = row;
				}
			}
		}
		int result = (largestX - smallestX) * (largestY - smallestY);
//		System.out.print(result);
		if(result == (grid.length * grid[0].length)) return 0;
		result = (largestX - smallestX + 1) * (largestY - smallestY + 1);
		return result;
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int ans = 0;

		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				HashSet<Integer> data = new HashSet<Integer>();
				int cmp = countHelper(col, row, grid[row][col], 0);
				if(cmp != 0){
					data.add(cmp);
					for(int dir = 1; dir <= 3; dir++) {
						data.add(countHelper(col, row, grid[row][col], dir));
					}
					if(data.size() == 1) ans++;
				}
			}
		}
		return ans;
	}
	//this method just goes from each side of the cross and counts the lengthes of each cross line.
	private int countHelper(int x, int y, char ch, int dir) {
		int ans = 0;
		while(true){
			if(dir == 0) x = x + 1;
			if(dir == 1) x = x - 1;
			if(dir == 2) y = y + 1;
			if(dir == 3) y = y - 1;
			if(x < 0 || x >= grid[0].length || y < 0 || y >= grid.length || grid[y][x] != ch ) break;
			ans++;
		}
		return ans;
	}
	
}
