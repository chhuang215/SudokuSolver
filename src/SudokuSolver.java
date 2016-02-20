import java.util.Arrays;

public class SudokuSolver {
	public static final int MAX_VALUE = 9;
	public static final int MIN_VALUE = 1;
	
	private int[][] puzzle;
	private boolean[][] preset;
	public SudokuSolver(){
		puzzle = new int[MAX_VALUE][MAX_VALUE];
		preset = new boolean[MAX_VALUE][MAX_VALUE];
	}
	
	public int[][] getPuzzle(){
		return puzzle.clone();
	}
	
	public boolean solve(int[][] p){
		puzzle = p.clone();
		
		// Indicate initial value and
		// 	Check initial value is valid or not
		for(int row = 0; row < 9; row ++){
			for (int col = 0; col < 9; col++){
				if(puzzle[row][col] != 0){
					if(isValid(row, col, puzzle[row][col])){
						preset[row][col] = true;
					}
					else{
						return false;
					}					
				}
			}
		}
	
		
		//Solve the puzzle using backtrack algorithm
		boolean result =  backtrack(0,0);
		printPuzzle();
		return result;
	}
	
	private void printPuzzle(){
		System.out.println();
		for(int i = 0; i < 9 ; i++){
			System.out.println(Arrays.toString(puzzle[i]));
		}
		System.out.println();
	}
	
	private boolean backtrack(int row, int col){
		if (col == 9) {
	            col = 0;
	            if (++row == 9)
	                return true;
	    }
		printPuzzle();
		// If the cell is not empty, continue with the next cell
		if( !preset[row][col] && puzzle[row][col] != 0 ){
			return backtrack(row,col+1);
		}
		    
		else{
		     // Find a valid number for the empty cell
			 for( int num = 1; num <= MAX_VALUE; num++ )
			 {
			    if( isValid(row,col,num) )
			    {
			    	puzzle[row][col] = num ;
			
			    	if(backtrack(row,col+1)){
			    		return true;
			    	}
			    }
			 }
			
			puzzle[row][col] = 0;
			return false;
		}
	}
	
	private boolean isValid(int row, int col, int num){
		if(checkHorizontal(row,num) && checkVerticle(col,num) && checkSubboard(row,col,num)){
			return true;
		}
		return false;
	}

	
	private boolean checkVerticle(int col, int num){
	
		for( int row = 0; row < 9; row++ )
			if( puzzle[row][col] == num ){
				 return false ;
			}
		System.out.println("ROW: OK");
		return true ;
	}
	
	private  boolean checkHorizontal(int row, int num){	
		for( int col = 0; col < 9; col++ )
			if( puzzle[row][col] == num ){
				return false ; 
			}
		
		System.out.println("COL: OK");
		return true ;
	}
	
	private boolean checkSubboard(int row,int col, int num){
		
		row = (row / 3) * 3 ;
		col = (col / 3) * 3 ;
		
		for( int r = 0; r < 3; r++ ){
		   for( int c = 0; c < 3; c++ ){
			   if( puzzle[row+r][col+c] == num ){
			      return false ;
			   }
		   }
		}
		System.out.println("BOX: OK");
		return true ;
	}
}
