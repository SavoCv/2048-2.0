package backend2048;

import java.util.Vector;

import backend2048.Table.Direction;

public class BasicSolver implements Solver {

	Table table;
	long arr[][];
	
	public BasicSolver(Table t)
	{
		table = t;
		
		int size = table.getSize();
		arr = new long[size][size];
		long curr = 1;
		
		for(int i = size - 1; i >= 0; --i)
			if(((size % 2) ^ (i % 2)) == 1)
				for(int j = 0; j < size; ++j)
				{
					arr[j][i] = curr;
					curr *= 4;
				}
			else
				for(int j = size - 1; j >= 0; --j)
				{
					arr[j][i] = curr;
					curr *= 4;
				}
	}
	
	@Override
	public Table getTable()
	{
		return table;
	}
	
	private long getEstimatedValue1(Table t)
	{
		int size = t.getSize();
		long x = 0;
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				x += arr[i][j] * t.getField(i, j).getVal();
		return x;
	}
	
	private long getEstimatedValue2(Table t)
	{
		int size = t.getSize();
		long x = getEstimatedValue1(t);
		Vector <Integer> v = new Vector<>(size * size);
		for(int i = 0; i < size; ++i)
			for(int j = 0; j < size; ++j)
				v.add(t.getField(i, j).getVal());
		
		v.sort((a, b)->{
			return b - a;
		});
		
		/*for(int i = 0; i < v.size(); ++i)
			System.out.print(v.get(i) + " ");
		System.out.println();*/
		
		int curr = 0;
		boolean br = false;
		for(int i = 0; i < size && !br; ++i)
			if(((size % 2) ^ (i % 2)) == 1)
				for(int j = 0; j < size; ++j)
				{
					if (t.getField(j, i).getVal() == v.get(curr) && v.get(curr) != 0)
						++curr;
					else 
					{
						br = true;
						break;
					}
				}
			else
				for(int j = size - 1; j >= 0; --j)
				{
					if (t.getField(j, i).getVal() == v.get(curr) && v.get(curr) != 0)
						++curr;
					else
					{
						br = true;
						break;
					}
				}
		//System.out.println(curr + 1);
		
		return x * (curr + 1);
	}
	
	protected long getEstimatedValue(Table t)
	{
		return getEstimatedValue2(t);
	}
	
	@Override
	public Direction nextMove() {
		
		Table tl = table.clone(),
				tr = table.clone(),
				tu = table.clone(),
				td = table.clone();
		
		boolean cl, cr, cu, cd;
		
		cl = !tl.slideWithoutDroping2or4andRecording(Direction.LEFT);
		cr = !tr.slideWithoutDroping2or4andRecording(Direction.RIGHT);
		cu = !tu.slideWithoutDroping2or4andRecording(Direction.UP);
		cd = !td.slideWithoutDroping2or4andRecording(Direction.DOWN);
		
		long xl = getEstimatedValue(tl), 
				xr = getEstimatedValue(tr),
				xu = getEstimatedValue(tu), 
				xd = getEstimatedValue(td);
		
		if(cl)
			xl = 0;
		if(cr)
			xr = 0;
		if(cu)
			xu = 0;
		if(cd)
			xd = 0;
		
		
		long m = Math.max(Math.max(xl, xr), Math.max(xu, xd));
		if(m == xl)
			return Direction.LEFT;
		if(m == xr)
			return Direction.RIGHT;
		if(m == xu)
			return Direction.UP;
		else
			return Direction.DOWN;
	}
	
}
