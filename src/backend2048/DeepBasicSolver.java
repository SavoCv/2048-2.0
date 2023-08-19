package backend2048;

import backend2048.Table.Direction;

public class DeepBasicSolver extends BasicSolver {

	private int depth;
	
	public DeepBasicSolver(Table t, int d) {
		super(t);
		depth = d;
	}

	private double getEstimatedValueWithDepth(Table t, int de)
	{
		if(de <= 1)
			return super.getEstimatedValue(t);
		double l = 0, max = 0;
		int cnt = 0, n = t.getSize();
		Direction[] directions = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.RIGHT};
		for(Direction dir : directions) {
			for(int i = 0; i < n; ++i)
				for(int j = 0; j < n; ++j)
				{
					if(t.getField(i, j).getVal() == 0)
					{
						Table tmp = t.clone();
						double w2 = 0, w4 = 0;
						boolean m2 = false, m4 = false;
						tmp.getField(i, j).setVal(2);
						if(tmp.slideWithoutDroping2or4andRecording(dir))
						{
							w2 = getEstimatedValueWithDepth(tmp, de - 1);
							m2 = true;
						}
						
						tmp = t.clone();
						tmp.getField(i, j).setVal(4);
						if(tmp.slideWithoutDroping2or4andRecording(dir))
						{
							w4 = getEstimatedValueWithDepth(tmp, de - 1);
							m4 = true;
						}
						
						if(m2 || m4)
						{
							++cnt;
							if(!m2)
								l += w4;
							else
								if(!m4)
									l += w2;
								else
								{
									double tt = Table.getProbabilityFor2();
									l += tt * w2 + (1 - tt) * w4;
								}
						}
							
					
					}
				}
			if(l / cnt > max)
				max = l / cnt;
		}
		
		return max;
	}
	
	@Override
	protected long getEstimatedValue(Table t) {
		return (long) getEstimatedValueWithDepth(t, depth);
	}
	
}
