import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;

public class GraphMaker
{
	public static void main(String[] args) throws IOException
	{
		if(args.length < 1)
			return ;

		int npart = numOfpartations(args[0]);

		File graphpartation = new File(args[0]);

		if(graphpartation.exists() == false)
		{
			System.out.println("Entered file does not existed in disk");
			return;
		}

		FileReader fr = new FileReader(graphpartation);
		BufferedReader br = new BufferedReader(fr);

		/************separe graph using ArrayList*********/
		ArrayList<Integer>[] parts = (ArrayList<Integer>[])new ArrayList<?>[npart+1];
		for(int i = 0; i < parts.length; i++)
			parts[i] = new ArrayList<Integer>();

		String s;
		int j = 1;

		while((s = br.readLine()) != null)
			parts[Integer.parseInt(s)].add(j++);

		EdgeWeightedGraph graph = (new GraphReader(args[0])).getGraph();
		GraphAdjMatrix[] miniGraphs = new GraphAdjMatrix[parts.length];
		for(int i = 0; i < parts.length ; i++)
		{
			miniGraphs[i] = new GraphAdjMatrix(parts[i].size());
		}


		

		for(int i = 0; i < parts.length -1 ; i++)
		{
			for(int v : parts[i])
			{
				for(Edge w : graph.adj(v))
				{
					int vertex =  w.other(v) ;
					if(parts[i].contains(vertex))
					{
						miniGraphs[i].setValues(parts[i].indexOf(v), parts[i].indexOf(vertex), w.getDistance());
					
					}
					else
					{
						parts[npart].add(vertex);
					}
				}
			}
			System.out.println();
		}

		

		

		for(int i = 0; i < miniGraphs.length; i++)
			System.out.println(miniGraphs[i]);
		for(int i = 0; i < parts.length; i++)
		{	if(i < parts.length - 1)
			{

				for(int v : parts[i])
					System.out.print(v+" ");
				System.out.println();
			}
			else
			{
				System.out.print("Boundaray Edges");
				for(int v : parts[i])
					System.out.print(v+" ");
				System.out.println();
			}	
			
		}
			
		
			
		br.close();
		
		
	}

	public static void printArray(int[] array)
	{
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i]);
		System.out.println();
	}
	public static int numOfpartations(String fileName)
	{
		Pattern p = Pattern.compile("\\d+$");
		Matcher m = p.matcher(fileName);
		String s = null;
		while(m.find())
			s = m.group();
		return (s == null) ? 0 : Integer.parseInt(s);
	}
}