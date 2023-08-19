package frontend2048;

import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import backend2048.Table;

public class ScorePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3309018517177513436L;
	
	Table table;
	JLabel score, best;
	
	public ScorePanel(Table t)
	{
		super(new FlowLayout());
		table = t;
		score = new JLabel("Score: " + t.getScore());
		best = new JLabel("Best: " + t.getBestScore());
		super.add(score);
		super.add(best);
		super.add(new JLabel("00:00"));
	}
	
	public void setTable(Table t)
	{
		table = t;
	}
	
	@Override
	public void paint(Graphics g) {
		score.setText("Score: " + table.getScore());
		best.setText("Best: " + table.getBestScore());
		super.paint(g);
	}
	
	
}
