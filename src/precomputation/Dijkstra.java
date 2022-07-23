package precomputation;

import java.util.*;


/**
 * Dijkstra
 */
public class Dijkstra {
	
	static Comparator<Integer> vertexC = new Comparator<Integer>()
	{
      public int compare(Integer o1, Integer o2) 
      {
          if(minDist[o1]>minDist[o2])
          {
        	  return 1;
          }else if(minDist[o1]<minDist[o2])
          {
			return -1;
		  }
          else {
			return 0;
         }
	  }
	};
	
	public HashMap<Integer, ArrayList<Integer>> outEdgesHashMap = new HashMap<Integer, ArrayList<Integer>>();
	public Map<Integer, List<Edge>> ver_edgeList_map = new HashMap<Integer, List<Edge>>();
	public static Queue<Integer> childList = new PriorityQueue<Integer>(vertexC);
	public static float[] minDist;
	public static int[] preVisited;
	public static boolean[] isVisited;
	
	public void init(int nodeSize)
	{
		preVisited = new int[nodeSize];
		isVisited = new boolean[nodeSize];
		Arrays.fill(preVisited , -1);
		minDist = new float[nodeSize];
		Arrays.fill(minDist ,Float.MAX_VALUE);
		for(int i=0;i<nodeSize;i++)
		{
			outEdgesHashMap.put(i, new ArrayList<Integer>());
			ver_edgeList_map.put(i, new ArrayList<Edge>());
		}
	}
	
	/**Dijkstra constructor
	 * @param nodeSize  the number of vertexes
	 * @param starts    the list consists all v1 of edge e(v1,v2)
	 * @param ends      the list consists all v2 of edge e(v1,v2)
	 * @param weights   the list consists all weight of edge e(v1,v2)
	 */
	public Dijkstra(int nodeSize, int[] starts,int[] ends,float[] weights) {
		init(nodeSize);
		Random randomer = new Random(10);
		for (int i = 0; i < starts.length; i++) {
			int start = starts[i];
			int end = ends[i];
			float minCrossTime = weights[i];
			int capacity = randomer.nextInt(200)+20;
			if (end != start && start>=0 && end>=0) {
				outEdgesHashMap.get(start).add(end);
				outEdgesHashMap.get(end).add(start);
				ver_edgeList_map.get(start).add(new Edge(start,end, minCrossTime,capacity));
				ver_edgeList_map.get(end).add(new Edge(end,start, minCrossTime,capacity));
			}
		}
	}

	public void setRoot(int start) {
		preVisited[start] = -1;
		minDist[start] = 0;
	}

	private void update(int v)
	{
		childList.add(v);
		while(childList.size() > 0)
		{
			int curVertex = childList.poll();
			if (curVertex <0 || ver_edgeList_map.get(curVertex) == null || ver_edgeList_map.get(curVertex).size() == 0) {
				return;
			}
			
			isVisited[curVertex] = true;
			for (Edge e : ver_edgeList_map.get(curVertex)) {
				int childVertex = e.getEndVertex();
				if (minDist[curVertex] + e.getWeight() < minDist[childVertex]) {
					minDist[childVertex] = minDist[curVertex] + e.getWeight();
					preVisited[childVertex] = curVertex;
					if (!childList.contains(childVertex) && !isVisited[childVertex]) {
						childList.add(childVertex);
					}
				}
			}
		}
	}
	
	public ArrayList<Integer> dijkstraTravasal(int startVertex, int endVertex) {
		ArrayList<Integer> pathsArrayList = new ArrayList<Integer>();
		pathsArrayList.add(endVertex);
		setRoot(startVertex);
		
		update(startVertex);
		while ((preVisited[endVertex] != -1) && (endVertex != startVertex)) 
		{
			pathsArrayList.add(0, preVisited[endVertex]);
			endVertex = preVisited[endVertex];
		}
		return pathsArrayList;
	}

}
