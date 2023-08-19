package backend2048;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Scorer {

	static private TreeMap <Integer, Integer> bestScores = new TreeMap<>();
	
	final static private String filename = "Scoreboard.txt";
	
	static {
		read();
	}
	
	public static int getBestScore(int dim)
	{
		Integer bs = bestScores.get(dim);
		if(bs == null) return 0;
		return bs;
	}
	
	public static void setBetterScore(int dim, int score)
	{
		Integer bs = bestScores.get(dim);
		if(bs == null || score > bs) {
			bestScores.put(dim, score);
			write();
		}
	}
	
	public static void write()
	{
		try {
			FileWriter writer = new FileWriter(filename);
			for(Map.Entry<Integer, Integer> entry : bestScores.entrySet())
			{
				writer.write(entry.getKey().toString() + " ");
				writer.write(entry.getValue().toString() + "\n");
			}
			writer.close();
		}
		catch(IOException e)
		{
			System.out.println("Write failed");
		}
	}
	
	public static void read()
	{
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextInt())
			{
				Integer score, dim = scanner.nextInt();
				if(scanner.hasNextInt())
				{
					score = scanner.nextInt();
					bestScores.put(dim, score);
				}
				else
				{
					System.out.println("Missing one integer in file");
				}
			}
			scanner.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}
}
