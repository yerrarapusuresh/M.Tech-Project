import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;

public class GraphMaker
{
	public static GraphAdjMatrix main(String[] args) throws IOException
	{
/************************************* MISCELLANEOUS CODE FOR READING INPUT PARTATIONED GRAPH AND MAKE GRAPHS******************************/
		if(args.length < 1)
			return null;

		int npart = numOfpartations(args[0]);

		File graphpartation = new File(args[0]);

		if(graphpartation.exists() == false)
		{
			System.out.println("Entered file does not existed in disk");
			return null;
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

		for(int i = 0; i < parts.length-1; i++)
		{
			miniGraphs[i] = new GraphAdjMatrix(parts[i].size());
		}




		for(int i = 0; i < parts.length-1; i++)
		{
			for(int v : parts[i])
			{
				for(Edge w : graph.adj(v))
				{
					int vertex =  (v == w.either()) ? w.other(w.either()):w.either() ;
					if(parts[i].contains(vertex))
						miniGraphs[i].setValues(parts[i].indexOf(v), parts[i].indexOf(vertex), w.getDistance());
					else if(!parts[npart].contains(vertex))
						parts[npart].add(vertex);
				}
			}
//			System.out.println();
		}
		/*
		*Extraction creating boundary graph with  with number of edges
		*/
		miniGraphs[npart] = new GraphAdjMatrix(parts[npart].size());

/********************************** PRINTS EACH COMPONENT IN GRAPH ****************************
		for(int i = 0; i < miniGraphs.length; i++)
		{

			System.out.println(miniGraphs[i]);
		}
/**************************************************************************************************************/
/******************************************STEP 2 : COMPUTES ALL PAIR SHORTEST PATH IN EACH SUBGRAPH **************************/		
		for(int i = 0; i < miniGraphs.length-1; i++)
			miniGraphs[i].computeAPSP();
/*********************************************************STEP 2 : FINISHED ******************************************************/
/****************************************STEP 3 : EXTRACTING BOUNDARY GRAPH AND COMPUTE APSP ON BOUNDARY GRAPH ******************************/

		/*
		*Adding edges to the  boundary graph
		*/
		for(int v : parts[npart])
		{

				for(Edge w : graph.adj(v))
				{
					int vertex =  (v == w.either()) ? w.other(w.either()):w.either() ;
					if(parts[npart].contains(vertex))
						miniGraphs[npart].setValues(parts[npart].indexOf(v), parts[npart].indexOf(vertex), w.getDistance());
					
				}
		}

		/*************************************************************
		*
		*PROVIDING INDEX ASSOSIATED VALUES TO EACH NODE IN SUB GRAPH 
		*
		***********************************************************************************************/

		for(int i = 0; i < miniGraphs.length; i++)
				miniGraphs[i].setList(parts[i]);
		
		
		/********************* ADDING VIRTUAL EDGES TO BOUNDARY GRPAH *********************/
		for(int i = 0; i < miniGraphs.length-1 ; i++)
		{
			for(int k = 0; k < parts[i].size(); k++)
			{
				for(int l = 0; l < parts[i].size(); l++)
					if(k != l)
					if(parts[parts.length-1].contains(parts[i].get(k)) && parts[parts.length-1].contains(parts[i].get(l)))
					{
						miniGraphs[miniGraphs.length-1].setValues(parts[parts.length-1].indexOf(parts[i].get(k)), parts[parts.length-1].indexOf(parts[i].get(l)), miniGraphs[i].getValue(k,l));
					}
			}


		}

		miniGraphs[miniGraphs.length-1].computeAPSP();
/**************************************************************STEP 3 : FINISHED **************************************************************************************/
	/******************************************IT IS PRINTS EACH PARTATION OUTPUT INCLUDING BOUNDARY GRAPH****************************************
	

		for(int i = 0; i < miniGraphs.length; i++)
		{

		//	System.out.println(miniGraphs[i]);
		}*/

	/*********************** IT PRINTS WHICH NODE BELONGS TO WHICH PARTATION BY USING PARTS ARRAY LIST
		for(int i = 0; i < parts.length; i++)
		{	if(i < parts.length)
			{

				for(int v : parts[i])
					System.out.print(v+" ");
				System.out.println();
			}
			
		}
	/******************************************************************************************/		
		
			
		br.close();
		return miniGraphs[miniGraphs.length-1];
		
		
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