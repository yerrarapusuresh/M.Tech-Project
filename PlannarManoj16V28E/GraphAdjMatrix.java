import java.util.ArrayList;

public class GraphAdjMatrix
{
	private final double[][] matrix;
	private final int size;
	private ArrayList<Integer> verticesOrder;

	public GraphAdjMatrix(int n)
	{
		matrix = new double[n][n];
		size = n;

		for(int i = 0; i < n ; i++)
			for(int j = 0; j < n ; j++)
				if(i != j)
					matrix[i][j] = Double.POSITIVE_INFINITY;
	}

	public void setList(ArrayList<Integer> list)
	{
		verticesOrder = list;
	}

	public ArrayList<Integer> getList()
	{
		return verticesOrder;
	}

	public void setValues(int i, int j, double value)
	{
		//System.out.println(size);
		matrix[i][j] = value;
	}

	public double getValue(int i, int j)
	{
		return matrix[i][j];
	}

	public int size()
	{
		return size;
	}

	public String toString()
	{
for(int i = 0; i < matrix.length ; i++)
			for(int j = 0; j < matrix.length; j++)
				System.out.println(matrix[i][j]);
					
/**
		if(verticesOrder == null)
		{
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < matrix.length; i++)
			{
				for(int j = 0; j < matrix[i].length; j++)
					sb.append(String.format("%7.2f ",matrix[i][j]));
				sb.append(String.format("\n"));
			}
			return sb.toString();
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("  "));
			for(int i : verticesOrder)
				sb.append(String.format("%7d ",i));
			sb.append(String.format("\n"));
			for(int i = 0; i < matrix.length; i++)
			{	
				sb.append(String.format("%d ",verticesOrder.get(i)));
				for(int j = 0; j < matrix[i].length; j++)
					sb.append(String.format("%7.2f ",matrix[i][j]));
				sb.append(String.format("\n"));
			}
			return sb.toString();
		}**/
	return "1";
	}

	public void computeAPSP()
	{
		for (int k = 0; k < size; k++)
        	for (int i = 0; i < size; i++)
            	for (int j = 0; j < size; j++)
                	if (matrix[i][k] + matrix[k][j] < matrix[i][j])
                    	matrix[i][j] = matrix[i][k] + matrix[k][j];
            
        
    
	}
}
