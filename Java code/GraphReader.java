import java.io.*;
import java.util.StringTokenizer;

public class GraphReader
{
	private EdgeWeightedGraph graph;


	public GraphReader(String filename) throws IOException
	{
		String inputFileName  = filename.split("\\.")[0];
		
		BufferedReader br = new BufferedReader(new FileReader(inputFileName));
		String firstLine = br.readLine();
		StringTokenizer st = new StringTokenizer(firstLine);
		graph = new EdgeWeightedGraph(Integer.parseInt(st.nextToken())+1);
		int edges = Integer.parseInt(st.nextToken());
	
		String input;

		int i = 1;
		while((input = br.readLine()) != null)
		{
			st = new StringTokenizer(input);
			while(st.hasMoreTokens())
			{
				graph.addEdge(new Edge(i, Integer.parseInt(st.nextToken()), Double.parseDouble(st.nextToken())));
			}
			i++;
		}
	//	System.out.println(graph);

	}
	public EdgeWeightedGraph getGraph()
	{
		return graph;
	}
}