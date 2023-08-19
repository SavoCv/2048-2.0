package backend2048;

import java.util.Scanner;
import java.util.Vector;

public class Test {
	
	public static void print(Table t)
	{
		Vector <Vector <Field>> v = t.getTable();
		for(int k = 0; k < t.getSize(); ++k) {
			for(int j = 0; j < t.getSize(); ++j)
			{
				System.out.print(v.get(k).get(j).getVal() + " ");
			}
			System.out.println();
		}
		System.out.println("-----------------------------------");
	}

	public static void main(String[] args) {
		
		try {
		
			Table t = new Table(4);
			
			print(t);
			
			while(true)
			{
				Scanner sc = new Scanner(System.in);
				String s;
				
				try {
					s = sc.next();
				}
				catch(Throwable thr)
				{
					break;
				}
				
				
				for(int i = 0; i < s.length(); ++i) {
					Table.Direction dir = Table.Direction.LEFT;
					switch(s.charAt(i))
					{
					case 'a': case 'A': dir = Table.Direction.LEFT; break;
					case 'd': case 'D': dir = Table.Direction.RIGHT; break;
					case 's': case 'S': dir = Table.Direction.DOWN; break;
					case 'w': case 'W': dir = Table.Direction.UP; break;
					}
					t.slideAndDo(dir, ()->{
						print(t);
					});
					System.out.println(t.getScore());
					System.out.println();
				}
				
				sc.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
