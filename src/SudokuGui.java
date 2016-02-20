/*
 * SudokuGui.java
 * 
 * Main/GUI class for this Sodoku Solver application
 * 
 * @author Chih-Hsuan Huang
 * Date: February 20, 2016
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class SudokuGui extends JFrame {

	private SudokuSolver ss;
	
	private JPanel mainContentPane;
	private JPanel jpBoard;
	private JPanel[][] jpSubboard;
	
	private JPanel jpSidePane;
	private JPanel jpButtonPane;

	private JLabel lblMessage;
	
	private JButton btnSolve;
	private JButton btnSelectFromFile;
	
	private JButton btnClear;
	private JButton btnClearAll;

	public SudokuGui() {
		
		setTitle("Sudoku");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 469);	
		
		// Initialize Solver object
		ss = new SudokuSolver();
		
		/* Initialize main 3x3 board*/
		jpBoard = new JPanel();
		
		jpBoard.setBorder(new EmptyBorder(5, 5, 5, 5));
		jpBoard.setLayout(new GridLayout(3, 3, 2, 2));
		jpBoard.setPreferredSize(new Dimension(450, 450));
		jpBoard.setMinimumSize(new Dimension(450, 450));
		jpBoard.setMaximumSize(new Dimension(450, 450));

		/* Initialize sub 3x3 board and add it onto the main board*/
		jpSubboard = new JPanel[3][3];

		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				JPanel p = jpSubboard[i][j] = new JPanel();
				
				p.setLayout(new GridLayout(3,3,0,0));
				
				for(int cellx = 0; cellx < 3; cellx++){
					for(int celly = 0; celly < 3; celly++){
						JTextField t = new JTextField(1);
						t.setHorizontalAlignment(JTextField.CENTER);
						t.setDocument(new PlainDocument(){
							@Override
							public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
								 if((getLength() + str.length()) <= 1){
									 char[] ch = str.toCharArray();
									 for(char c:ch){
										 if(!Character.isDigit(c)) return;
									 }
									  super.insertString(offs, str, a);
								 }
							         
							}
						});
						t.addMouseListener(new MouseListener() {
							@Override
							public void mouseReleased(MouseEvent e) {}
							@Override
							public void mousePressed(MouseEvent e) {
								JTextField jt = (JTextField)e.getComponent();
								jt.selectAll();
								
							}
							@Override
							public void mouseExited(MouseEvent e) {}
							
							@Override
							public void mouseEntered(MouseEvent e) {}
							
							@Override
							public void mouseClicked(MouseEvent e) {}
						});
						
						Font f = t.getFont();
						t.setFont(new Font(f.getFamily(),f.getStyle(),24));
						p.add(t);

					}
				}
				
				jpBoard.add(p);
			}
		}
		
		/* Button Panel */
		jpButtonPane = new JPanel();
		GridBagLayout gbl_buttonPane = new GridBagLayout();
		jpButtonPane.setLayout(gbl_buttonPane);
		
		// Button: Solve puzzle
		btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {

				if(solve()){
					lblMessage.setText("<html><p>Solved!</p><html>");
				}
				else{
					lblMessage.setText("<html><p>Unsolvable.</p><html>");
				}
				
				refreshBoardValue();
				
			}
		});
		
		GridBagConstraints gbc_btnSolve = new GridBagConstraints();
		gbc_btnSolve.insets = new Insets(0, 0, 5, 5);
		gbc_btnSolve.gridx = 0;
		gbc_btnSolve.gridy = 1;
		jpButtonPane.add(btnSolve, gbc_btnSolve);
		
		// Button: Select file
		btnSelectFromFile = new JButton("Select file");
		btnSelectFromFile.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					chooseFile();
					refreshBoardValue();
				}
			});
		GridBagConstraints gbc_btnSelectFromFile = new GridBagConstraints();
		gbc_btnSelectFromFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectFromFile.gridx = 0;
		gbc_btnSelectFromFile.gridy = 3;
		jpButtonPane.add(btnSelectFromFile, gbc_btnSelectFromFile);
		
		// Button: Clear board
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ss.restartPuzzle();
				
				refreshBoardValue();
			}
		});
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 5);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 4;
		jpButtonPane.add(btnClear, gbc_btnClear);
		
		// Button: Clear and reset board
		btnClearAll = new JButton("Clear All");
		btnClearAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lblMessage.setText("<html>Insert values<br><br>Or<br><br>Load values<br>from file<br></html>");
				ss.resetPuzzle();
				
				refreshBoardValue();
			}
		});
		GridBagConstraints gbc_btnClearAll = new GridBagConstraints();
		gbc_btnClearAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearAll.gridx = 0;
		gbc_btnClearAll.gridy = 5;
		jpButtonPane.add(btnClearAll, gbc_btnClearAll);
		/*end Button Panel*/
		
		// Label: Message display
		lblMessage = new JLabel("<html>Insert values<br><br>Or<br><br>Load values<br>from file<br></html>");
		lblMessage.setMaximumSize(new Dimension(250, 300));
		lblMessage.setMinimumSize(new Dimension(250, 300));
		lblMessage.setBackground(Color.WHITE);
		lblMessage.setOpaque(true);
		lblMessage.setFont(new Font(lblMessage.getFont().getFamily(), lblMessage.getFont().getStyle(), 16));
		lblMessage.setHorizontalAlignment(JTextField.CENTER);
		
		/* Add Message Label and Button panel to the Side Panel*/
		jpSidePane = new JPanel(new BorderLayout());
		jpSidePane.add(jpButtonPane, BorderLayout.CENTER);
		jpSidePane.add(lblMessage,BorderLayout.NORTH);
		
		/* Add main puzzle Board Panel to the centre of main panel*/
		mainContentPane = new JPanel();
		mainContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContentPane.setLayout(new BorderLayout());
		
		mainContentPane.add(jpBoard, BorderLayout.CENTER);
		mainContentPane.add(jpSidePane, BorderLayout.EAST);
		
		setContentPane(mainContentPane);
	}
	
	/**
	 * Refresh the values and status of the board
	 */
	private void refreshBoardValue(){
		int [][] board = ss.getPuzzle();
		
		for(int row = 0; row < board.length; row++){
			for(int col = 0; col < board[0].length; col++){
				JTextField jtextfield = (JTextField)(jpSubboard[row/3][col/3].getComponent(((row%3)*3) + (col%3)));
				if(board[row][col] != 0){
					jtextfield.setText(""+board[row][col]);
					if(ss.isPreset(row,col)){
						jtextfield.setForeground(Color.BLUE);
						jtextfield.setEditable(false);
					}
					else{
						jtextfield.setForeground(Color.BLACK);
						jtextfield.setEditable(true);
					}
				}
				else{
					jtextfield.setText("");
					jtextfield.setForeground(Color.BLACK);
					jtextfield.setEditable(true);
				}
			}
		}
	}
	
	/**
	 * Return a value of a cell
	 * 
	 * @param suboardx: sub board x position (0-2)
	 * @param suboardy: sub board y position (0-2)
	 * @param cellx: cell x position (0-2)
	 * @param celly: cell y position (0-2)
	 * @return a number on the cell in the range of 1-9,
	 * 			return 0 otherwise.
	 * 				
	 */
	private int getBoardValue(int suboardx,int suboardy, int cellx,int celly){

		JTextField jtf = (JTextField)(jpSubboard[suboardx][suboardy].getComponent(cellx*3 + celly));
		String strVal = jtf.getText().trim();
		int value = 0;
		if(!strVal.equals("") && strVal != null){
			value = Integer.parseInt(strVal);
			if(value >= SudokuSolver.MIN_VALUE && value <= SudokuSolver.MAX_VALUE){
				return value;
			}
		}
		
		return 0;
	}
	
	/**
	 * Open file chooser and load puzzle from a file
	 */
	private void chooseFile(){
		String line = null;
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			
			try {
				File file = fc.getSelectedFile();
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String strPuzzle = "";
				char[] charPuzzle = null;
				while((line = br.readLine()) != null){
					line = line.replaceAll("[^\\d.]|\\n|\\s", "");
					strPuzzle += line;
				}
				
				charPuzzle = strPuzzle.toCharArray();
				
				ss.setPuzzle(ss.convertToIntPuzzle(charPuzzle));
				
				fr.close();
				br.close();
			} catch (FileNotFoundException e) {
				lblMessage.setText("<html>File not found<html>");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * Solve the puzzle
	 * @return true: the puzzle is solved, false otherwise
	 */
	private boolean solve(){
		
		int[][] puzzle = new int[9][9];
	
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				for(int x = 0; x < 3; x++){
					for(int y = 0; y < 3; y++){
						int val = getBoardValue(i,j,x,y);
						puzzle[3*i+x][3*j+y] = val;
					}	
				}
			}	
		}
		
		if(!ss.setPuzzle(puzzle)) {return false;}
		
		return ss.solve();
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {			
					SudokuGui sudokugui = new SudokuGui();
					sudokugui.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
