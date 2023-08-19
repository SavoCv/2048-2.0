package backend2048;

import java.util.Vector;

public class Table implements Cloneable {

	private Vector<Vector<Field>> table;
	private int size, score;
	private Vector<Vector <Vector<Field>>> tablesBefore;
	private Vector<Integer> scoresBefore;
	
	final static private double probabilityFor2 = 0.75;
	
	public static enum Direction{
		LEFT, RIGHT, UP, DOWN
	}
	
	public static interface FI{
		void f();
	}
	
	public Table(int sz)
	{
		size = sz;
		table = new Vector<Vector <Field>>(size);
		tablesBefore = new Vector<>();
		scoresBefore = new Vector<>();
		for(int i = 0; i < size; ++i) {
			table.add(i, new Vector<>(size));
			for(int j = 0; j < size; ++j)
				table.get(i).add(new Field());
		}
		try {
			drop2or4();
			drop2or4();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public int getBestScore()
	{
		return Scorer.getBestScore(size);
	}
	
	public static double getProbabilityFor2()
	{
		return probabilityFor2;
	}
	
	public void addToScore(int d)
	{
		score += d;
		Scorer.setBetterScore(size, score);
	}
	
	public Field getField(int i, int j)
	{
		if(i >= 0 && i < size && j >= 0 && j < size)
			return table.get(i).get(j);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Vector<Field>> getTable()
	{
		Vector<Vector<Field>> copy;
		copy = (Vector<Vector<Field>>) table.clone();
		return copy;
	}
	
	@Override
	public Table clone() {
		Vector<Vector<Field>> tmp = new Vector<>();
		for(int i = 0; i < size; ++i)
		{
			tmp.add(new Vector<Field>());
			for(int j = 0; j < size; ++j)
				tmp.get(i).add((Field) table.get(i).get(j).clone());
		}
		Table t = new Table(size);
		t.table = tmp;
		return t;
	}
	
	public void drop2or4() throws Exception
	{
		int numZeros = 0;
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				if(table.get(i).get(j).getVal() == 0)
					++numZeros;
		if(numZeros == 0)
			throw new Exception("Can't drop 2 or 4, no free tiles");
		numZeros = (int)(Math.random() * numZeros);
		int val = 2;
		if(Math.random() > probabilityFor2)
			val = 4;
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				if(table.get(i).get(j).getVal() == 0)
					if(numZeros == 0) {
						table.get(i).get(j).setVal(val);
						return;
					}
					else
						--numZeros;
	}
	
	private boolean slideOneLeft()
	{
		boolean isMoved = false;
		for(int i = 0; i < size; ++i)
			for(int j = 1; j < size; ++j)
				isMoved |= Field.slide(table.get(i).get(j), table.get(i).get(j - 1), this);
		return isMoved;
	}
	
	private boolean slideOneRight()
	{
		boolean isMoved = false;
		for(int i = 0; i < size; ++i)
			for(int j = size - 1; j > 0; --j)
				isMoved |= Field.slide(table.get(i).get(j - 1), table.get(i).get(j), this);
		return isMoved;
	}
	
	private boolean slideOneUp()
	{
		boolean isMoved = false;
		for(int j = 0; j < size; ++j)
			for(int i = 1; i < size; ++i)
				isMoved |= Field.slide(table.get(i).get(j), table.get(i - 1).get(j), this);
		return isMoved;
	}
	
	private boolean slideOneDown()
	{
		boolean isMoved = false;
		for(int j = 0; j < size; ++j)
			for(int i = size - 1; i > 0; --i)
				isMoved |= Field.slide(table.get(i - 1).get(j), table.get(i).get(j), this);
		return isMoved;
	}
	
	private void recordTable()
	{
		Vector<Vector<Field>> tmp = new Vector<>();
		for(int i = 0; i < size; ++i)
		{
			tmp.add(new Vector<Field>());
			for(int j = 0; j < size; ++j)
				tmp.get(i).add((Field) table.get(i).get(j).clone());
		}
		tablesBefore.add(tmp);
		scoresBefore.add(score);
	}
	
	public boolean slideAndDo(Direction d, FI fo)
	{
		recordTable();
		boolean isMoved = false;
		for(int i = 0; i < size; ++i)
		{
			boolean tmp = false;
			switch(d)
			{
			case LEFT: tmp = slideOneLeft(); break;
			case RIGHT: tmp = slideOneRight(); break;
			case UP: tmp = slideOneUp(); break;
			case DOWN: tmp = slideOneDown(); break;
			}
			if(tmp == false)
				break;
			isMoved = true;
			fo.f();
		}
		if(isMoved)
			try {
				drop2or4();
			}
			catch(Exception e)
			{
				System.out.println("ERROR: can't drop 2 or 4");
			}
		fo.f();
		
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				table.get(i).get(j).resetMerged();
		if(!isMoved)
			tablesBefore.remove(tablesBefore.size() - 1);
		
		return isMoved;
	}
	
	public boolean slideWithoutDroping2or4andRecording(Direction d)
	{
		boolean isMoved = false;
		for(int i = 0; i < size; ++i)
		{
			boolean tmp = false;
			switch(d)
			{
			case LEFT: tmp = slideOneLeft(); break;
			case RIGHT: tmp = slideOneRight(); break;
			case UP: tmp = slideOneUp(); break;
			case DOWN: tmp = slideOneDown(); break;
			}
			if(tmp == false)
				break;
			isMoved = true;
		}
		
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				table.get(i).get(j).resetMerged();
		
		return isMoved;
	}
	
	public boolean canMakeAMove()
	{
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				if(table.get(i).get(j).getVal() == 0)
					return true;
				else
					if(i < size - 1 && table.get(i).get(j).getVal() == table.get(i + 1).get(j).getVal())
						return true;
					else
						if(j < size - 1 && table.get(i).get(j).getVal() == table.get(i).get(j + 1).getVal())
							return true;
		return false;
	}
	
	public boolean undo()
	{
		if(tablesBefore.size() == 0)
			return false;
		
		Vector<Vector<Field>> tmp = tablesBefore.remove(tablesBefore.size() - 1);
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				table.get(i).get(j).setVal(
						tmp.get(i).get(j).getVal());
		
		score = scoresBefore.remove(scoresBefore.size() - 1);
		
		
		return true;
	}
}
