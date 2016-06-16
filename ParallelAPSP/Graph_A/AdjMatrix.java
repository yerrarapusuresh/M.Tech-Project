import java.util.Map;
import java.util.HashMap;


public class AdjMatrix
{
	private double[][] matrix;
	private int size;
	private HashMap<Integer, Integer> verticesToComponentIndex;
	private boolean[] isItBoundary;
	private int[] indexToVertex;
	private HashMap<Integer, Integer> nearBoundaryVertices;
	private int numberOfBoundaryVerties;


	public AdjMatrix(int size) {
		this.size = size;
		matrix = new double[size][size];
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(i != j)
					matrix[i][j] = Double.POSITIVE_INFINITY;


	}

	public int numberOfBoundaryVerties() {
		return this.numberOfBoundaryVerties;
	}

	public void addEdge(int i, int j, double value) {
		matrix[i][j] = value;
	}

	public void setVerticesToComponentIndex(HashMap<Integer, Integer> vtc) {
		this.verticesToComponentIndex = vtc;
		indexToVertex = new int[vtc.size()];
	}

	public double getDistance(int i, int j){
		return matrix[i][j];
	}

	public void setIndexToVertex(int[] itv) {

		this.indexToVertex = itv;
		for(int i = 0; i < itv.length; i++)
				if(isItBoundary[indexToVertex[i]])
					numberOfBoundaryVerties++;
	 }

	 public int indexToVertex(int index) {
	 	return indexToVertex[index];
	 }


	public void computeAPSP()
	{
		for (int k = 0; k < size; k++)
        	for (int i = 0; i < size; i++)
            	for (int j = 0; j < size; j++)
                	if (matrix[i][k] + matrix[k][j] < matrix[i][j])
                    	matrix[i][j] = matrix[i][k] + matrix[k][j];
            
        
    
	}

	public void findMinToNonBoundary() { 

		if(nearBoundaryVertices == null){
			nearBoundaryVertices = new HashMap<Integer, Integer>();
			for(int i = 0; i < size; i++) {
				double min = Double.POSITIVE_INFINITY;
				int index  = 0;
				if(!isItBoundary[indexToVertex[i]]){
					for(int j = numberOfBoundaryVerties-1; j >= 0; j--)	 {
						if(matrix[i][j] < min){
							min = matrix[i][j];
							index = j;
						}
					}
					nearBoundaryVertices.put(i, indexToVertex[index]);
				}
			}
		}
	}

	public int getMinVertices(int vertice){
		if(isItBoundary[vertice])
			return 0;
		return nearBoundaryVertices.get(verticesToComponentIndex.get(vertice));
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