import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	private JTextArea original;
	private JTextArea solution;
	private JButton check;
	private JCheckBox cb;
	private Sudoku sudoku;


	public SudokuFrame() {
		super("Sudoku Solver");

		sudoku = null;

		BorderLayout bl = new BorderLayout(4, 4);
		original = new JTextArea(15, 20);
		original.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(cb.isSelected()) textAreaListener();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(cb.isSelected()) textAreaListener();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(cb.isSelected()) textAreaListener();
			}
		});
		original.setBorder(new TitledBorder("Puzzle"));
		solution = new JTextArea(15, 20);
		solution.setBorder(new TitledBorder("Solution"));
		solution.setEditable(false);
		bl.addLayoutComponent(original, BorderLayout.WEST);
		bl.addLayoutComponent(solution, BorderLayout.EAST);
		this.setLayout(bl);
		this.add(original, BorderLayout.WEST);
		this.add(solution, BorderLayout.EAST);

		JPanel bxP = new JPanel();
		BoxLayout bx = new BoxLayout(bxP, BoxLayout.X_AXIS);
		bxP.setLayout(bx);
		check = new JButton("Check");
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaListener();
			}
		});
		cb = new JCheckBox("Auto Check");
		cb.setSelected(true);
		bxP.add(check);
		bxP.add(cb);
		this.add(bxP, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void textAreaListener(){
		sudoku = new Sudoku(Sudoku.stringsToGrid(original.getText()));
		int s = sudoku.solve();
		StringBuilder sb = new StringBuilder();
		sb.append(sudoku.getSolutionText());
		sb.append('\n');
		sb.append("solutions:" + s);
		sb.append('\n');
		sb.append("elapsed:" + sudoku.getElapsed() + "ms");
		solution.setText(sb.toString());
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
