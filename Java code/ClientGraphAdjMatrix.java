public class ClientGraphAdjMatrix
{
	public static void main(String[] args)
	{
		GraphAdjMatrix graph = new GraphAdjMatrix(4);
		graph.setValues(0, 3, 10);
		graph.setValues(0, 1, 5);
		graph.setValues(1, 2, 3);
		graph.setValues(2, 3, 1);
		
		System.out.println(graph);
		graph.computeAPSP();
		System.out.println(graph);
	}
}