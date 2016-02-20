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

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@SuppressWarnings("serial")
public class SudokuGui extends JFrame {

	private SudokuSolver ss;
	
	private JPanel mainContentPane;
	private JPanel jpBoard;
	private JPanel[][] jpSubboard;
	
	private JPanel jpSidePane;
	private JPanel jpButtonPane;
	
	
	private JLabel lblMessage;
	
	private JButton btnStart;
	
	private JButton btnSolve;
	private JButton btnInit;
	private JButton btnSelectFromFile;
	
	private JButton btnClear;
	private JButton btnClearAll;

	public SudokuGui() {
		
		setTitle("Sudoku");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 469);	

		ss = new SudokuSolver();
		
		jpBoard = new JPanel();
		jpBoard.setPreferredSize(new Dimension(450, 450));
		jpBoard.setMinimumSize(new Dimension(450, 450));
		jpBoard.setMaximumSize(new Dimension(450, 450));
		jpBoard.setBorder(new EmptyBorder(5, 5, 5, 5));
		jpBoard.setLayout(new GridLayout(3, 3, 2, 2));

		jpSubboard = new JPanel[3][3];

		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				JPanel p = jpSubboard[i][j] = new JPanel();
				p.setLayout(new GridLayout(3,3,0,0));
				setSubBoard(p);
				jpBoard.add(p);
			}
		}
		
		/* Button Panel */
		jpButtonPane = new JPanel();
		GridBagLayout gbl_buttonPane = new GridBagLayout();
		jpButtonPane.setLayout(gbl_buttonPane);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 0;
		jpButtonPane.add(btnStart, gbc_btnStart);
		
		// btnSolve
		btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(solve()){
					lblMessage.setText("Solved!");
				}
				else{
					lblMessage.setText("Unsolvable");
				}
				
			}
		});
		
		GridBagConstraints gbc_btnSolve = new GridBagConstraints();
		gbc_btnSolve.insets = new Insets(0, 0, 5, 5);
		gbc_btnSolve.gridx = 0;
		gbc_btnSolve.gridy = 1;
		jpButtonPane.add(btnSolve, gbc_btnSolve);
		
		btnInit = new JButton("Set initial value");
		GridBagConstraints gbc_btnInit = new GridBagConstraints();
		gbc_btnInit.insets = new Insets(0, 0, 5, 5);
		gbc_btnInit.gridx = 0;
		gbc_btnInit.gridy = 2;
		jpButtonPane.add(btnInit, gbc_btnInit);
		
		btnSelectFromFile = new JButton("Load board file");
		btnSelectFromFile.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					chooseFile();
					
				}
			});
		GridBagConstraints gbc_btnSelectFromFile = new GridBagConstraints();
		gbc_btnSelectFromFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectFromFile.gridx = 0;
		gbc_btnSelectFromFile.gridy = 3;
		jpButtonPane.add(btnSelectFromFile, gbc_btnSelectFromFile);
		
		btnClear = new JButton("Clear");
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 5);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 4;
		jpButtonPane.add(btnClear, gbc_btnClear);
		
		btnClearAll = new JButton("Clear All");
		GridBagConstraints gbc_btnClearAll = new GridBagConstraints();
		gbc_btnClearAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearAll.gridx = 0;
		gbc_btnClearAll.gridy = 5;
		jpButtonPane.add(btnClearAll, gbc_btnClearAll);
		/*end Button Panel*/
		
		lblMessage = new JLabel("Message goes here");
		lblMessage.setMaximumSize(new Dimension(250, 300));
		lblMessage.setMinimumSize(new Dimension(250, 300));
		lblMessage.setBackground(Color.WHITE);
		lblMessage.setOpaque(true);
		jpSidePane = new JPanel(new BorderLayout());
		jpSidePane.add(jpButtonPane, BorderLayout.CENTER);
		jpSidePane.add(lblMessage,BorderLayout.NORTH);
		mainContentPane = new JPanel();
		mainContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContentPane.setLayout(new BorderLayout());
		mainContentPane.add(jpBoard, BorderLayout.CENTER);
		mainContentPane.add(jpSidePane, BorderLayout.EAST);
		
		setContentPane(mainContentPane);
		
		
	}
	
	private void setSubBoard(JPanel p){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				JTextField t = new JTextField(1);
				
				t.setHorizontalAlignment(JTextField.CENTER);
				Font f = t.getFont();
				t.setFont(new Font(f.getFamily(),f.getStyle(),24));
				p.add(t);

			}
		}
	}
	
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
	
	private void chooseFile(){
		String line = null;
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			
		}
		
	}
	
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
		
		return ss.solve(puzzle);
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
					sudokugui.pack();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
