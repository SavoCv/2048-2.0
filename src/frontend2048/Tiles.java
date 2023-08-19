package frontend2048;

import java.awt.GridLayout;

import javax.swing.JPanel;

import backend2048.Table;

import java.util.Vector;

public class Tiles extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7812725884501267943L;
	
	private Vector <Vector <Tile>> tiles;
	private Table table;
	
	public Tiles(int sz)
	{
		super(new GridLayout(sz, sz));
		
		table = new Table(sz);
		
		tiles = new Vector<>(sz);
		for(int i = 0; i < sz; ++i)
		{
			tiles.add(i, new Vector<Tile>(sz));
			for(int j = 0; j < sz; ++j)
				tiles.get(i).add(new Tile(table.getField(i, j)));
		}
		
		for(int i = 0; i < sz; ++i)
			for(int j = 0; j < sz; ++j)
				super.add(tiles.get(i).get(j));
	}
	
	public Table getTable()
	{
		return table;
	}
	
}
