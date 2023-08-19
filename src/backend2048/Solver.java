package backend2048;

public interface Solver {

	Table.Direction nextMove();
	
	Table getTable();
}
