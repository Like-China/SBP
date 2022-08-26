package precomputation;

import java.io.*;

import utils.RoadNet;

/**
 * Calculate the shortest distance 
 * Store all-pairs shortest distance 
 */
public class ShortestDistanceStore {
	String filepath1;
	int startId;
	int nodeSize;

	public ShortestDistanceStore(int startId,int nodeSize,String roadNetPath)
	{
		this.startId = startId;
		this.nodeSize = nodeSize;
		this.filepath1 = "data/"+roadNetPath+"Dist/"+startId+".txt"; 
	}
	
	public void storeMinDist()
	{
		try {
			File file = new File(filepath1);
			FileWriter fileWriter = new FileWriter(file);
		    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"gbk"));
			for(int i=0;i<nodeSize;i++)
			{
				bufferedWriter.write(String.format("%.2f", Dijkstra.minDist[i]));
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
		    bufferedWriter.close();
		    fileWriter.close();
			}
		 catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	// extra time cost caused by non-query objects
	public static float calcExtraCost(int capacity, float minCrossTime)
	{
		return (float)Math.pow(0.4*0.8, 2)*minCrossTime*2f;
	}
	
	public static void main(String[] args) {
		String roadName = "NYC";
    	RoadNet road = new RoadNet("data/"+roadName+".txt");
    	int nodeSize = road.getVertexCount();
		int edgeSize = road.starts.length;
		float minCrossTime[] = new float[edgeSize];
		for(int i=0;i<edgeSize;i++)
		{
			minCrossTime[i] = road.weights[i] + calcExtraCost(road.capacitys[i], road.weights[i]);
		}
        Dijkstra g = new Dijkstra(nodeSize,road.starts,road.ends, minCrossTime);
        for(int i= 0;i<nodeSize;i++)
        {
        	g = new Dijkstra(nodeSize,road.starts,road.ends, minCrossTime);
        	g.dijkstraTravasal(i, nodeSize-1);
        	ShortestDistanceStore f = new ShortestDistanceStore(i,nodeSize,roadName);
        	f.storeMinDist();
        	if(i > 0 && i % 1000 == 0)
        	{
        		System.out.printf("%d / %d (finished/total)\n", i,nodeSize);
        	}
        }
	}
	
}
