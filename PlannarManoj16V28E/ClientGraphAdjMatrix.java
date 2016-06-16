public class ClientGraphAdjMatrix
{
	public static void main(String[] args)
	{
		GraphAdjMatrix graph = new GraphAdjMatrix(5);
		graph.setValues(0, 0, 1);
		graph.setValues(1, 1, 1);
		graph.setValues(2, 2, 1);
		graph.setValues(3, 3, 1);
		graph.setValues(4, 4, 1);
		System.out.println(graph);
	}
}