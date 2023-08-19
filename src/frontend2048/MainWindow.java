package frontend2048;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import backend2048.DeepSolver;
import backend2048.Scorer;
import backend2048.Solver;
import backend2048.Table;

import java.util.Vector;

public class MainWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1334522787140910574L;
	private int size = 4;
	Tiles tiles;
	
	Vector <KeyEvent> keyEvents = new Vector<>();
	Thread threadKE, threadSolver;
	
	ScorePanel scorePanel;
	
	Solver solver;
	
	private void processKey(KeyEvent e)
	{
		//System.out.println("key processing");
		Table.Direction dir = Table.Direction.UP;
		boolean isSet = true;
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP: dir = Table.Direction.UP; break;
		case KeyEvent.VK_LEFT: dir = Table.Direction.LEFT; break;
		case KeyEvent.VK_RIGHT: dir = Table.Direction.RIGHT; break;
		case KeyEvent.VK_DOWN: dir = Table.Direction.DOWN; break;
		default: isSet = false;
		}
		if(!isSet)
		{
			isSet = true;
			switch(e.getKeyChar())
			{
			case 'a': case 'A': dir = Table.Direction.LEFT; break;
			case 's': case 'S': dir = Table.Direction.DOWN; break;
			case 'd': case 'D': dir = Table.Direction.RIGHT; break;
			case 'w': case 'W': dir = Table.Direction.UP; break;
			default: isSet = false;
			}
			
			if(!isSet) {
				//HERE ADD another key shortcut or action
				return;
			}
		}
		
		try {
			tiles.getTable().slideAndDo(dir, new Table.FI() {
				@Override
				public void f() {
					revalidate();
					repaint();
					try {
						//Test.print(tiles.getTable());
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			if(!tiles.getTable().canMakeAMove())
			{
				gameOver();
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void gameOver()
	{
		System.out.println("Game over");
	}
	
	private void createThreadForKeyEvents()
	{
		threadKE = new Thread() {
			@Override
			synchronized public void run() {
				try {
					while(!isInterrupted()) {
						while(keyEvents.size() == 0) {
							wait();
						}
						processKey(keyEvents.get(0));
						keyEvents.removeElementAt(0);
					}
				} catch (InterruptedException e) {
				}
			}
		};
		threadKE.start();
	}
	
	private void startAgain()
	{
		super.remove(tiles);
		tiles = new Tiles(size);
		super.add(tiles);
		scorePanel.setTable(tiles.getTable());
		
		revalidate();
		repaint();
		setFocusable(true);
		requestFocusInWindow();
	}
	
	private void undo()
	{
		tiles.getTable().undo();
		revalidate();
		repaint();
		setFocusable(true);
		requestFocusInWindow();
	}
	
	private void help()
	{
		int depth = 3;
		if(solver == null)
			solver = new DeepSolver(tiles.getTable(), depth);
		if(solver.getTable() != tiles.getTable())
			solver = new DeepSolver(tiles.getTable(), depth);
		
		Table.Direction dir;
		dir = solver.nextMove();
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					tiles.getTable().slideAndDo(dir, new Table.FI() {
						@Override
						public void f() {
							revalidate();
							repaint();
							try {
								//Test.print(tiles.getTable());
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!tiles.getTable().canMakeAMove())
				{
					gameOver();
				}
				revalidate();
				repaint();
				setFocusable(true);
				requestFocusInWindow();
			}
		};
		t.start();
	}
	
	private void solve()
	{
		threadSolver = new Thread() {
			
			@Override
			public void run() {
				try {
					while(!isInterrupted() && tiles.getTable().canMakeAMove())
					{
						help();
						Thread.sleep(200);
					}
				}
				catch(InterruptedException e)
				{
					
				}
			};
			
		};
		threadSolver.start();
	}
	
	private void populateWindow()
	{	
		JButton newBtn = new JButton("new"),
				undoBtn = new JButton("undo"),
				withoutUndoBtn = new JButton("without undo"),
				helpBtn = new JButton("help"),
				withoutHelpBtn = new JButton("without help"),
				solveBtn = new JButton("solve");
		JPanel btnPanel = new JPanel(new GridLayout(1, 6)),
				panel = new JPanel(new BorderLayout());
				//timeAndBtnPanel = new JPanel(new FlowLayout());
			
		tiles = new Tiles(size);
		scorePanel = new ScorePanel(tiles.getTable());
		
		btnPanel.add(newBtn);
		btnPanel.add(undoBtn);
		btnPanel.add(withoutUndoBtn);
		btnPanel.add(helpBtn);
		btnPanel.add(withoutHelpBtn);
		btnPanel.add(solveBtn);
		
		//timeAndBtnPanel.add(new JLabel("00:00"));
		//timeAndBtnPanel.add(btnPanel);
		
		panel.add(scorePanel, BorderLayout.NORTH);
		panel.add(btnPanel);
		
		super.add(panel, BorderLayout.NORTH);
		super.add(tiles);
		
		super.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				//System.out.println("key pressed");
				keyEvents.add(e);
				synchronized(threadKE) {
					threadKE.notify();
				}
				
			}
		});
		
		newBtn.addActionListener(ae->{
			startAgain();
		});
		
		undoBtn.addActionListener(ae->{
			undo();
		});
		
		helpBtn.addActionListener(ae->{
			help();
		});
		
		solveBtn.addActionListener(ae->{
			solve();
		});
		
		createThreadForKeyEvents();
		
		setFocusable(true);
		requestFocusInWindow();
	}
	
	public MainWindow()
	{	
		super.setBounds(200, 100, 500, 500);
		
		populateWindow();
			
		super.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				threadKE.interrupt();
				threadSolver.interrupt();
				dispose();
				Scorer.write();
			}
			
		});
		
		super.setBackground(Color.DARK_GRAY);
		
		super.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new MainWindow();
	}
	
}
