import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;



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

		
		

		/****
		*partation the graph accordings to the InputGraph and also store the boundary edges in array list;
		*****/

		ArrayList<Edge> boudaryEdges = new ArrayList<Edge>();
		ArrayList<Edge> nonBoudaryEdges = new ArrayList<Edge>();

		bf = new BufferedReader(new FileReader(inputGraph));
		boolean[] isItBoundary = new boolean[numberOfVertices+1];

		

		toRead = bf.readLine();
		String[] toSplit;
		count = 1;
		int index = 0;
		while((toRead = bf.readLine()) != null) {
			toSplit = toRead.split(" ");
			for(int i = 0; i < toSplit.length; i += 2) 
			{
				int v = Integer.parseInt(toSplit[i]);
				int d = Integer.parseInt(toSplit[i+1]);

				if(verticesToComponent[count] == verticesToComponent[v])
					nonBoudaryEdges.add(new Edge(count, v, d));
				else 
				{
					boudaryEdges.add(new Edge(count, v, d));
					isItBoundary[count] = isItBoundary[v] = true;

				}
			}
			count++;
		}

		int[][] indexToVertices = new int[numberOfComponents][];

		for(int i = 0; i < numberOfComponents; i++)
			indexToVertices[i] = new int[verticesToComponentIndex[i].size()];

		
		for(int i = 0; i < verticesToComponentIndex.length; i++){
			for (Map.Entry<Integer, Integer> entry : verticesToComponentIndex[i].entrySet())
				indexToVertices[i][entry.getValue()] = entry.getKey();
		}

		for(int i = 0; i < numberOfComponents; i++) {
			int k = 0;
			for(int j = 0; j < indexToVertices[i].length; j++) {
					if(isItBoundary[indexToVertices[i][j]])
					{
						int v = indexToVertices[i][j];
						int w = indexToVertices[i][k];
						verticesToComponentIndex[i].put(v, k);
						verticesToComponentIndex[i].put(w, j);
						int temp1 = indexToVertices[i][j];
						indexToVertices[i][j] = indexToVertices[i][k];
						indexToVertices[i][k++] = temp1;
					}
			}
		}


		/********************** TESTING FOR REORDER OF MATRIX ******************

		for(int i = 0; i < numberOfComponents; i++)
		for (Map.Entry<Integer, Integer> entry : verticesToComponentIndex[i].entrySet())
				System.out.println(entry.getValue()+"  "+entry.getKey());

		for (Map.Entry<Integer, Integer> entry : boundaryVerticesToIndex.entrySet())
				System.out.println(entry.getValue()+"  "+entry.getKey());
		/******************* TESTING FOR REORDER IS DONE HERE **************************/

		/***
		*Assigning memory for each component according to their size;
		****/

		AdjMatrix[] components = new AdjMatrix[numberOfComponents];

		for(int i = 0; i < numberOfComponents; i++)
			components[i] = new AdjMatrix(verticesToComponentIndex[i].size());

	//	System.out.println(isItBoundary.length);

		for(int i = 0; i < numberOfComponents; i++){
			components[i].setIsItBoundary(isItBoundary);
			components[i].setVerticesToComponentIndex(verticesToComponentIndex[i]);
			components[i].setIndexToVertex(indexToVertices[i]);
		}
		/********************TESTING FOR EACH TO PRINT OR NOT ??*****************
		for(int i = 0; i < numberOfComponents; i++) {
			System.out.println(components[i]);
		}
		/******************TESTING IS DONE BY HERE***************************/

		/****************EXTRACTING OF EACH BOUNDARY VERTICES FOR COMPONENT AND STORE IT IN BOUNDARY TO INDEX HASHMAP*******/
		HashMap<Integer, Integer> boundaryVerticesToIndex = new HashMap<Integer, Integer>();
		int tempInt;
		count = 0;
		for(int i = 0; i < numberOfComponents; i++) {
			tempInt = components[i].numberOfBoundaryVerties();
			for(int j = 0; j < tempInt; j++)
				boundaryVerticesToIndex.put(indexToVertices[i][j], count++);
		}
		/************************************Extraction of Each Boundary is done Here*************************************************************************/
		/*************************TESTING******************************************
		for(Map.Entry<Integer, Integer> entry : boundaryVerticesToIndex.entrySet())
			System.out.println(entry.getKey()+"  "+entry.getValue());
		/***********************************Extraction of each boundary is done *************************************************************************/

		

		
		for(Edge e : nonBoudaryEdges) {
			int v = e.source();
			int w = e.destination();
			double d = e.distance();
			if(verticesToComponent[v] != verticesToComponent[w])
			{
				System.out.println("severe Erron in line number 224");
				return;
			}
			components[verticesToComponent[v]].addEdge(verticesToComponentIndex[verticesToComponent[v]].get(v), verticesToComponentIndex[verticesToComponent[v]].get(w), d);
		}

		/********************TESTING FOR EACH TO PRINT OR NOT ??*****************
		for(int i = 0; i < numberOfComponents; i++) {
			System.out.println(components[i]);
		}
		/******************TESTING IS DONE BY HERE***************************/


		for(int i = 0; i < numberOfComponents; i++)
			components[i].computeAPSP();

		AdjMatrix boundary = new AdjMatrix(boundaryVerticesToIndex.size());
		boundary.setVerticesToComponentIndex(boundaryVerticesToIndex);
		temp = new int[boundaryVerticesToIndex.size()];
		for(Map.Entry<Integer, Integer> entry : boundaryVerticesToIndex.entrySet())
			temp[entry.getValue()] = entry.getKey();
		
		boundary.setIsItBoundary(isItBoundary);
		boundary.setIndexToVertex(temp);

		

		//System.out.println(boundary);
		/*********** 
		*Adding edges to boundary component ***
		************/
		for(Edge e : boudaryEdges) {
			int v = e.source();
			int w = e.destination();
			double d = e.distance();
			boundary.addEdge(boundaryVerticesToIndex.get(v), boundaryVerticesToIndex.get(w), d);
		}



		/*********
		*Adding virtual Edge to boundary component
		***********/
		for(int i = 0; i < temp.length; i++){
			for(int j = 0; j < temp.length; j++)
				if(i != j && verticesToComponent[temp[i]] == verticesToComponent[temp[j]])
					boundary.addEdge(i, j, components[verticesToComponent[temp[i]]].getDistance( verticesToComponentIndex[verticesToComponent[temp[i]]].get(temp[i]),   verticesToComponentIndex[verticesToComponent[temp[i]]].get(temp[j])                        ));
		}
		//System.out.println(boundary);
		for(int i = 0; i < numberOfComponents; i++)
			components[i].findMinToNonBoundary();
//		for(int i = 0; i < numberOfComponents; i++)
//			System.out.println(components[i]);
		boundary.computeAPSP();
//		System.out.println(boundary);

	/*************************************************COMPUTING FINAL RESULT***********************************/
	double[][] result = new double[numberOfVertices][numberOfVertices];
	double minTemp, d1, d2, d3 ;
	int _component, __component;
	int I, J;

	for(int i = 0; i < numberOfVertices; i++)
		for(int j = 0; j < numberOfVertices; j++)
		{
			if(i == j)
				result[i][j] = 0;
			else if(isItBoundary[i+1] && isItBoundary[j+1])
				result[i][j] = boundary.getDistance(boundaryVerticesToIndex.get(i+1), boundaryVerticesToIndex.get(j+1));
			else if(verticesToComponent[i+1] == verticesToComponent[j+1]&&!isItBoundary[i+1] && !isItBoundary[j+1])
				{
				

					_component = verticesToComponent[i+1];
					d1 = components[_component].getDistance( verticesToComponentIndex[_component].get(i+1), verticesToComponentIndex[_component].get(j+1) );
					I = components[_component].getMinVertices(i+1);
					d2 = components[_component].getDistance( verticesToComponentIndex[_component].get(i+1), verticesToComponentIndex[_component].get( I));
					J = components[_component].getMinVertices(j+1);
					d2 += components[_component].getDistance( verticesToComponentIndex[_component].get(j+1), verticesToComponentIndex[_component].get( J));
					d2 += boundary.getDistance(boundaryVerticesToIndex.get(I), boundaryVerticesToIndex.get(J));
					result[i][j] = Math.min(d1, d2);

			}

			else if(!isItBoundary[i+1] && isItBoundary[j+1])
			{

				minTemp = Double.POSITIVE_INFINITY;
				_component = verticesToComponent[i+1];
				for(int k = 0; k < components[_component].numberOfBoundaryVerties(); k++)
				{
					int v =  components[_component].indexToVertex(k);
					double t = components[_component].getDistance(verticesToComponentIndex[_component].get(i+1), k) + boundary.getDistance(boundaryVerticesToIndex.get(v), boundaryVerticesToIndex.get(j+1));
					if(t < minTemp)
						minTemp = t;
				}
				result[i][j] = minTemp;
			}
			else if(isItBoundary[i+1] && !isItBoundary[j+1])
			{

				minTemp = Double.POSITIVE_INFINITY;
				_component = verticesToComponent[j+1];
				for(int k = 0; k < components[_component].numberOfBoundaryVerties(); k++)
				{
					int v =  components[_component].indexToVertex(k);
					double t = components[_component].getDistance(verticesToComponentIndex[_component].get(j+1), k) + boundary.getDistance(boundaryVerticesToIndex.get(v), boundaryVerticesToIndex.get(i+1));
					if(t < minTemp)
						minTemp = t;
				}
				result[i][j] = minTemp;

			}

			else
			{
					_component = verticesToComponent[i+1];
					__component = verticesToComponent[j+1];
					minTemp = Double.POSITIVE_INFINITY;
					for(int k = 0; k < components[_component ].numberOfBoundaryVerties(); k++)
					{
						for(int l = 0; l < components[__component].numberOfBoundaryVerties(); l++)
						{
							d1 = components[_component].getDistance(verticesToComponentIndex[_component].get(i+1), k);	
							d2 = components[__component].getDistance(verticesToComponentIndex[__component].get(j+1), l);	
							I = components[_component].indexToVertex(k);
							J = components[__component].indexToVertex(l);
							
							d1 += d2 + boundary.getDistance(boundaryVerticesToIndex.get(I), boundaryVerticesToIndex.get(J));
							if(d1 < minTemp)
								minTemp = d1;

						}
					}
					result[i][j] = minTemp;

				

			}
		}
	for(int i = 0; i < result.length; i++)
			for(int j = 0; j < result.length; j++)
				System.out.println(result[i][j]);
	System.out.println(1);
		

		/********************TESTING THE COMPONENTS***********************
		for(int i = 0; i < components.length; i++)
			System.out.println(components[i]);
		
		System.out.println(boundary);
		/*******************TESTING IS DONE*******************************************

	for(int i = 0; i < components.length; i++)
			components[i].findMinToNonBoundary();
	

	/********************************************* COMPUTING FINAL RESULT *******************************************************

	double[][] result = new double[numberOfVertices][numberOfVertices];

	for(int i = 0; i < numberOfVertices; i++)
		for(int j = 0; j < numberOfVertices; j++) {
			if(i == j)
				result[i][j] = 0.0;
			else if(isItBoundary[i+1] && isItBoundary[j+1])
				result[i][j] = boundary.getDistance(boundaryVerticesToIndex.get(i+1), boundaryVerticesToIndex.get(j+1));
			else if(!isItBoundary[i+1] && isItBoundary[j+1]){
				
				int nearBoundary = components[verticesToComponent[i+1]].getMinVertices(i+1);
				result[i][j] = components[verticesToComponent[i+1]].getDistance( verticesToComponentIndex[verticesToComponent[i+1]].get(i+1), verticesToComponentIndex[verticesToComponent[i+1]].get(nearBoundary)    )  + boundary.getDistance(boundaryVerticesToIndex.get(nearBoundary), boundaryVerticesToIndex.get(j+1));
			}

			else if(isItBoundary[i+1] && !isItBoundary[j+1]){
				int nearBoundary = components[verticesToComponent[j+1]].getMinVertices(j+1);
				result[i][j] = components[verticesToComponent[j+1]].getDistance( verticesToComponentIndex[verticesToComponent[j+1]].get(j+1), verticesToComponentIndex[verticesToComponent[j+1]].get(nearBoundary)    )  + boundary.getDistance(boundaryVerticesToIndex.get(nearBoundary), boundaryVerticesToIndex.get(i+1));
			}

			else
			{
				if(verticesToComponent[i+1] == verticesToComponent[j+1]) { 

					int componentOne = verticesToComponent[i+1];
					int componentTwo = verticesToComponent[j+1];

					int nearBoundaryI = components[componentOne].getMinVertices(i+1);
					int nearBoundaryJ = components[componentTwo].getMinVertices(j+1);

					double distOne = components[componentOne].getDistance( verticesToComponentIndex[componentOne].get(i+1), verticesToComponentIndex[componentOne].get(nearBoundaryI)  );
					double distTwo = components[componentTwo].getDistance( verticesToComponentIndex[componentTwo].get(j+1), verticesToComponentIndex[componentTwo].get(nearBoundaryJ)  );
					double pathLeaveAndEnter = distOne + distTwo + boundary.getDistance( boundaryVerticesToIndex.get(nearBoundaryI), boundaryVerticesToIndex.get(nearBoundaryJ) );
					double withInComponent = components[componentOne].getDistance( verticesToComponentIndex[componentOne].get(i+1), verticesToComponentIndex[componentOne].get(j+1)  );
					result[i][j] = Math.min(pathLeaveAndEnter, withInComponent);
				}

				else
				{
					int componentOne = verticesToComponent[i+1];
					int componentTwo = verticesToComponent[j+1];

					int nearBoundaryI = components[componentOne].getMinVertices(i+1);
					int nearBoundaryJ = components[componentTwo].getMinVertices(j+1);

					double distOne = components[componentOne].getDistance( verticesToComponentIndex[componentOne].get(i+1), verticesToComponentIndex[componentOne].get(nearBoundaryI)  );
					double distTwo = components[componentTwo].getDistance( verticesToComponentIndex[componentTwo].get(j+1), verticesToComponentIndex[componentTwo].get(nearBoundaryJ)  );
					result[i][j] = distOne + distTwo + boundary.getDistance( boundaryVerticesToIndex.get(nearBoundaryI), boundaryVerticesToIndex.get(nearBoundaryJ) );
					if(i == 0 && j == 6)
						System.out.println(distOne+" "+distTwo+" "+boundary.getDistance( boundaryVerticesToIndex.get(nearBoundaryI), boundaryVerticesToIndex.get(nearBoundaryJ)) );
				}
			}

		}

	
		for(int i = 0; i < result.length; i++){
			for(int j = 0; j < result.length; j++)
				System.out.print(result[i][j]+"  ");
			System.out.println();
		}
		/***************************************************COMPUTATION IS DONE BY HERE **********************************/

	}

		
}

























