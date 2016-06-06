import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;



public class ParallelAPSP{

	public static void main(String[] args) throws IOException{

		/***
		*Below condition check wherther User Passed enough parameter or not?
		***/
		if(args.length < 4){
			System.out.println("Usage: ParallelAPSP PartationOutputFile  InputGraph  NumberOfComponents  NumberOfVertices");
			return;
		}

		/***
		*Opening of two input files
		***/	

		File partationFile = new File(args[0]);
		File inputGraph = new File(args[1]);
		int numberOfComponents = Integer.parseInt(args[2]);
		int numberOfVertices = Integer.parseInt(args[3]);
		/****
		*check number of components
		****/
		if(numberOfComponents < 1) {
			System.out.println("components should not be less than 1");
			return;
		}

		/****
		*Checking existing partation file
		****/

		if(!partationFile.exists()) {
			System.out.println("Unable to open partationOutputFile");
			return;
		}

		/***
		*checking existing input graph file
		****/

		if(!inputGraph.exists()) {
			System.out.println("Unable to open Input Graph file");
			return;
		}

		/***
		*Get number of vertices
		*/
		
		if(numberOfVertices < 1){
			System.out.println("Number of vertices should be less than 1");
			return;
		}

		int[] verticesToComponent = new int[numberOfVertices+1];
		HashMap<Integer, Integer>[] verticesToComponentIndex = (HashMap<Integer, Integer>[])new HashMap[numberOfComponents];

		for(int i = 0; i < numberOfComponents; i++)
			verticesToComponentIndex[i] = new HashMap<Integer, Integer>();

		int[] temp = new int[numberOfComponents];

		for(int i = 0; i < numberOfComponents; i++)
			temp[i] = 0;

		/***
		*Separating individual components; and also identify their associtated index in the component
		***/
		
		String toRead;
		int count = 1;
		BufferedReader bf = new BufferedReader(new FileReader(partationFile));
		while((toRead = bf.readLine()) != null) {
			int tempInt = Integer.parseInt(toRead);
			verticesToComponent[count] = tempInt;
			verticesToComponentIndex[tempInt].put(count, temp[tempInt]++);
			count++;

		}
		/************************************************** TESTING CODE ***********************
		for(int i = 0; i < numberOfComponents; i++)
		for (Map.Entry<Integer, Integer> entry : verticesToComponentIndex[i].entrySet()) 
    		System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    	System.out.println("-------------------------------index to component-------------------------------");
    	for(int i = 1; i < verticesToComponent.length; i++)
    		System.out.println(""+verticesToComponent[i]);

		/************************************************** TESTING IS DONE *************************/

		/***
		*Assigning memory for each component according to their size;
		****/
		AdjMatrix[] components = new AdjMatrix[numberOfComponents];
		for(int i = 0; i < numberOfComponents; i++)
			components[i] = new AdjMatrix(verticesToComponentIndex[i].size());

		/********************TESTING THE COMPONENTS***********************
		for(int i = 0; i < components.length; i++)
			System.out.println(components[i]);

		/*******************TESTING IS DONE*******************************************/
		/****
		*adding vertices to index to each component;
		*****/
		for(int i = 0; i < numberOfComponents; i++)
			components[i].setVerticesToComponentIndex(verticesToComponentIndex[i]);

		/****
		*partation the graph accordings to the InputGraph and also store the boundary edges in array list;
		*****/

		ArrayList<Edge> boudaryEdges = new ArrayList<Edge>();
		bf = new BufferedReader(new FileReader(inputGraph));
		boolean[] isItBoundary = new boolean[numberOfVertices+1];
		HashMap<Integer, Integer> boundaryVerticesToIndex = new HashMap<Integer, Integer>();
		toRead = bf.readLine();
		String[] toSplit;
		count = 1;
		int index = 0;
		while((toRead = bf.readLine()) != null) {
			toSplit = toRead.split(" ");
			for(int i = 0; i < toSplit.length; i += 2) {
				int v = Integer.parseInt(toSplit[i]);
				int d = Integer.parseInt(toSplit[i+1]);
				if(verticesToComponent[count] == verticesToComponent[v])
					components[verticesToComponent[count]].addEdge(verticesToComponentIndex[verticesToComponent[count]].get(count), verticesToComponentIndex[verticesToComponent[count]].get(v), d);
				else {
					boudaryEdges.add(new Edge(count, v, d));
					isItBoundary[count] = isItBoundary[v] = true;
					if(!boundaryVerticesToIndex.containsKey(count))
						boundaryVerticesToIndex.put(count, index++);
					if(!boundaryVerticesToIndex.containsKey(v))
						boundaryVerticesToIndex.put(v, index++);

				}
			}
			count++;
		}
		
		/*************
		*Adding boundary vertices to each component;
		**************/
		for(int i = 0; i < numberOfComponents; i++)
			components[i].setIsItBoundary(isItBoundary);
		for(int i = 0; i < numberOfComponents; i++)
			System.out.println(components[i]);
		

	}
		
}

























