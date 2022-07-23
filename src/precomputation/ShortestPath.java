package precomputation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShortestPath {

	
	public static ArrayList<Integer> findShorestPaths(int[] preVisited,int endVertexID)
	{
		ArrayList<Integer>  paths= new ArrayList<Integer>();
		while(preVisited[endVertexID] != -1)
		{
			paths.add(0,endVertexID);
			endVertexID = preVisited[endVertexID];
		}
		paths.add(0,endVertexID);
		return paths;
	}
	
	
	public  static ArrayList<Float> caluShortestPathTimes(ArrayList<Integer> paths,
			float startTime,Dijkstra g)
	{
		ArrayList<Float> crossNodesTimes = new ArrayList<Float>();
		Map<Integer,List<Edge> > ver_edgeList_map = g.ver_edgeList_map;
		for(int i=0;i<paths.size();i++)
		{
			
			if(i == 0)
			{
				crossNodesTimes.add(startTime);
			}
			else {
				int currentId = paths.get(i);
				int lastId = paths.get(i-1);
				List<Edge> lastEdges = ver_edgeList_map.get(lastId);
				for(int j=0;j<lastEdges.size();j++)
				{
					if(lastEdges.get(j).getEndVertex() == currentId)
					{
						crossNodesTimes.add(startTime+lastEdges.get(j).getWeight());
						startTime = startTime+lastEdges.get(j).getWeight();
					}
				}
				
			}
		}
		
		return crossNodesTimes;
	}
	
}
