import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.*;
public class SequentialAPSP
{
	public static void main(String[] args) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String firstLine = br.readLine();
		StringTokenizer st = new StringTokenizer(firstLine);
		int size = Integer.parseInt(st.nextToken());
		GraphAdjMatrix matrix = new GraphAdjMatrix(size);
		int edges = Integer.parseInt(st.nextToken());
		int[][] array = new int[size][size];
		String input;
		for( int i = 0; i < size; i++){
			for(int j = 0; j < size; j++)
				if(i != j)
					array[i][j] = Integer.MAX_VALUE;
		}
		int i = 0;
		while((input = br.readLine()) != null)
		{
			st = new StringTokenizer(input);
			while(st.hasMoreTokens())
			{
				int x = Integer.parseInt(st.nextToken())-1;
				matrix.setValues(i, x, Double.parseDouble(st.nextToken()));
			}
			i++;
		}
		matrix.computeAPSP();

		ArrayList<Integer> li = new ArrayList<Integer>();
		for(i = 1; i <= size; i++)
			li.add(i);
		matrix.setList(li);
		System.out.println(matrix);
		/****************************CROSS CHECK FOR BOUNDARY EXISTED IN ORIGINAL GRAPH EXACTLY OR NOT ***************************************************
		GraphMaker maker = new GraphMaker();
		String[] fileName = new String[1];
		fileName[0] = args[1];
		GraphAdjMatrix bGraph =  maker.main(fileName);
		//System.out.println(bGraph);


		ArrayList<Integer> list = bGraph.getList();
		for(i = 0; i < list.size(); i++)
		{
			for(int j = 0; j < list.size(); j++)
				if(matrix.getValue(list.get(i)-1, list.get(j)-1) == bGraph.getValue(i, j))
				{
					System.out.println(list.get(i)+" "+list.get(j)+" "+matrix.getValue(list.get(i)-1, list.get(j)-1)+""+list.get(i)+" "+list.get(j)+" "+bGraph.getValue(i, j));
				}
		}*/
	}
		
	


}