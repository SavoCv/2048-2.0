package frontend2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import backend2048.Field;

public class Tile extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5763908097625351204L;
	
	Field f;
	
	private static int arcSize = 16;
	private static int border = 10;
	
	public Tile(Field field)
	{
		f = field;
	}
	
	private Color getBackgroundColor()
	{
		switch(f.getVal())
		{
		case 0: return new Color(0xccbeb3);
		case 2: return new Color(15590105); 
		case 4: return new Color(15458247);
		case 8: return new Color(0xf0b078);
		case 16: return new Color(0xf69563);
		case 32: return new Color(0xf47b5e);
		case 64: return new Color(0xf05f3b);
		case 128: return new Color(0xeccf71);
		case 256: return new Color(0xf1d049);
		case 512: return new Color(0xecc64e);
		case 1024: return new Color(0xe0ba23);
		case 2048: return new Color(0xebc31d);
		}
		
		return Color.BLACK;
	}
	
	private Color getForegroundColor()
	{
		
		switch(f.getVal())
		{
		case 2: return new Color(7826788);
		case 4: return new Color(0x776d64);
		case 8: return Color.WHITE;
		}
		if(f.getVal() <= 2048)
			return Color.WHITE;
		else
			return Color.BLACK;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//setBackground(Color.BLACK); //NO EFFECT ??
		//System.out.println(getX() + " " + getY());
		g.setColor(getBackgroundColor());
		g.fillRoundRect(border / 2, border / 2, getWidth() - border, getHeight() - border, arcSize, arcSize);
		
		if(f.getVal() != 0) {
			int centerX, centerY;
			centerX = getWidth() / 2;
			centerY = getHeight() / 2;
			
			String s = String.valueOf(f.getVal());
			int strX, strY;
			g.setFont(new Font("Arial", Font.PLAIN, 25));
			strX = centerX - g.getFontMetrics().stringWidth(s) / 2;
			strY = centerY + g.getFont().getSize() / 2;
			
			g.setColor(getForegroundColor());
			g.drawString(s, strX, strY);
		}
	}
	
}
