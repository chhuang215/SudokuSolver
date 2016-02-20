import java.util.Arrays;


public class SudokuSolver {
	public static final int MAX_VALUE = 9;
	public static final int MIN_VALUE = 1;

	private boolean presetSetted = false;
	
	private int[][] puzzle;
	private boolean[][] preset;

	/**
	 * Constructor 
	 */
	public SudokuSolver(){
		puzzle = new int[MAX_VALUE][MAX_VALUE];
		
		preset = new boolean[MAX_VALUE][MAX_VALUE];

		resetPuzzle();
	}
	
	/**
	 * Set and initialize values for the puzzle
	 * @param p: puzzle board
	 * @return true: the values are valid; false: otherwise
	 */
	public boolean setPuzzle(int[][] p){
		resetPuzzle();

		// Indicate initial value and
		// 	Check initial value is valid or not
		for(int row = 0; row < 9; row ++){
			for (int col = 0; col < 9; col++){
				if(p[row][col] != 0){
					if(validate(row, col, p[row][col])){
						puzzle[row][col] = p[row][col];
						preset[row][col] = true;
					}
					else{

						puzzle = p.clone();
						return false;
					}					
				}
			}
		}
		presetSetted = true;
		return true;
		
	}
	
	/**
	 * 
	 * @return int[][] puzzle board
	 */
	public int[][] getPuzzle(){
		return puzzle.clone();
	}
	
	/**
	 * Reset puzzle
	 */
	public void resetPuzzle(){
		for(int i = 0 ; i < puzzle.length; i++){
			Arrays.fill(puzzle[i], 0);
			Arrays.fill(preset[i], false);

			presetSetted = false;
		}
	}
	
	/**
	 * Reset puzzle without clearing preset values
	 */
	public void restartPuzzle(){		
		for(int i = 0 ; i < puzzle.length; i++){
			for(int j = 0; j < puzzle[i].length ; j++){
				if(!preset[i][j]){
					puzzle[i][j] = 0;
				}
			}
		}
	}
	
	/**
	 * @return true: board have preset values; false: otherwise
	 */
	public boolean havePreset(){
		return presetSetted;
	}
	
	/**
	 * Determine if a value of cell is preset or not
	 * @param row position
	 * @param col position
	 * @return true : value is preset; false otherwise
	 */
	public boolean isPreset(int row, int col){
		return preset[row][col];
	}
	
	public int[][] convertToIntPuzzle(char[] charPuzzle){
		
		int [][] p = new int[MAX_VALUE][MAX_VALUE];
		
		if(charPuzzle.length == 81){
			
			for(int row = 0; row < MAX_VALUE; row++){
				for(int col = 0; col < MAX_VALUE; col++){
					if(charPuzzle[row*9+col] != '0' && charPuzzle[row*9+col] != '.'){
						p[row][col] = Character.getNumericValue(charPuzzle[row*9+col]);
					}
					else{
						p[row][col] = 0;
					}
				}
			}

			return p.clone(); 	
		}
		return null;
	}
	
	/**
	 * Solve the sudoku puzzle
	 * @return true: solved; false: otherwise
	 */
	public boolean solve(){
		//Solve the puzzle using backtrack method
		return solve(0,0);
	}

	/**
	 * Solve the sudoku puzzle
	 * @param row: row position of cell
	 * @param col: column position of cell
	 * @return true: solved; false: otherwise
	 */
	private boolean solve(int row, int col){

        if (col >= 9){
            return true;
        }
	
		
		// If the cell is not empty and the number is not preset
		//	skip to the next number
		if( puzzle[row][col] != 0 ){
			if(row == 8){
				if(solve(0,col+1)){

					return true;
				}
			}else{
				if(solve(row+1,col)){

					return true;
				}
			}
			return false;
		}
		    
		
	     // Find a valid number for the empty cell
		for( int num = 1; num <= MAX_VALUE; num++ ){
			
		    if( validate(row,col,num) )
		    {
		    	puzzle[row][col] = num ;
		
		    	
		    	// next number		    	
	    		if(row == 8){
					if(solve(0,col+1)){

						return true;
					}
				}else{
					if(solve(row+1,col)){

						return true;
					}
				}
	    		
	    		// No solution found
	    		puzzle[row][col] = 0;
		    }
		}
		return false;			
	
	}
	
	/**
	 * Check if the number on a cell is valid or not
	 * 
	 * @param row: row index
	 * @param col: column index
	 * @param num: value of the number
	 * @return true: if the number is allowed on the position
	 * @return false: if the number is now allowed on the position
	 */
	private boolean validate(int row, int col, int num){
		
		// Check vertical 
		for( int r = 0; r < 9; r++ ){
			if(puzzle[r][col] == num ){
	
				return false ;
			}
		}
		
		// Check horizontal
		for( int c = 0; c < 9; c++ ){
			if(puzzle[row][c] == num ){
			
				return false ; 
			}
		}
		
		// Check sub board
		int subrow = (row / 3) * 3 ;
		int subcol = (col / 3) * 3 ;
		
		for( int r = 0; r < 3; r++ ){
		   for( int c = 0; c < 3; c++ ){
			   
			   if(puzzle[subrow+r][subcol+c] == num ){

			      return false ;
			   }
		   }
		}
		
		return true;
	
	}
}
