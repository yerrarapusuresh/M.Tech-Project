public class TestAdjMatrix{
	public static void main(String[] args) {
		AdjMatrix mat = new AdjMatrix(4);
		mat.addEdge(0, 1, 3);
		mat.addEdge(3, 2, 5);
		mat.addEdge(2, 1, 10);
		System.out.println(mat);
	}
}
