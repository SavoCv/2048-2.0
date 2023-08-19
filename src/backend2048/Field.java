package backend2048;

public class Field implements Cloneable {
	
	private int val;
	private boolean merged;
	
	public Field()
	{
		val = 0;
		merged = false;
	}
	
	public void setVal(int v)
	{
		val = v;
	}
	
	public int getVal()
	{
		return val;
	}
	
	public void resetMerged()
	{
		merged = false;
	}
	
	@Override
	public Object clone() {
		Field f = new Field();
		f.val = val;
		f.merged = merged;
		return f;
	}

	public static boolean slide(Field fieldFrom, Field fieldTo, Table t) {
		if(fieldFrom.val == 0)
			return false;
		if(fieldTo.val == 0)
		{
			int tmpVal = fieldFrom.val;
			boolean tmpMerged = fieldFrom.merged;
			fieldFrom.val = fieldTo.val;
			fieldFrom.merged = fieldTo.merged;
			fieldTo.val = tmpVal;
			fieldTo.merged = tmpMerged;
			return true;
		}
		if(fieldTo.val == fieldFrom.val && !fieldTo.merged && !fieldFrom.merged)
		{
			fieldTo.val *= 2;
			fieldTo.merged = true;
			fieldFrom.val = 0;
			t.addToScore(fieldTo.val);
			return true;
		}
		
		return false;
	}
	
}
