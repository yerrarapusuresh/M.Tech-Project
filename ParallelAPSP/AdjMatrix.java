import java.util.HashMap;
import java.util.Map;

public class AdjMatrix
{
	private double[][] matrix;
	private int size;
	private HashMap<Integer, Integer> verticesToComponentIndex;
	private boolean[] isItBoundary;
	private int[] indexToVertex;


	public AdjMatrix(int size) {
		this.size = size;
		matrix = new double[size][size];
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(i != j)
					matrix[i][j] = Double.POSITIVE_INFINITY;
	}

	public void addEdge(int i, int j, double value) {
		matrix[i][j] = value;
	}

	public void setVerticesToComponentIndex(HashMap<Integer, Integer> vtc) {
		this.verticesToComponentIndex = vtc;
		indexToVertex = new int[vtc.size()];
		
		for (Map.Entry<Integer, Integer> entry : vtc.entrySet()) {
    		indexToVertex[entry.getValue()] = entry.getKey();
}
	}

	public void setIsItBoundary(boolean[] iib) {
		this.isItBoundary = iib;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("   "));
		for(int i = 0; i < indexToVertex.length; i++)
			sb.append(String.format("%9d", indexToVertex[i]));
		sb.append(String.format("\n"));
		for(int i = 0; i < size; i++) {
			sb.append(String.format("%3d", indexToVertex[i]));
			for(int j = 0; j < size; j++) 
				sb.append(String.format("%7.2f   ", matrix[i][j]));
			
			sb.append(String.format("\n"));
		}
		return sb.toString();
	}

}