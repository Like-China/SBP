package precomputation;

import java.io.*;

import planer.RoadNet;

/**
 * Calculate the shortest distance 
 * Store all-pairs shortest distance 
 */
public class FileStore {
	String filepath1;
	int startId;
	int nodeSize;

	public FileStore(int startId,int nodeSize,String roadNetPath)
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
	
	public static float calcExtraCost(int capacity, float minCrossTime, boolean isRushHour)
	{
		if(!isRushHour)
		{
			return (float)Math.pow(0.16, 2)*minCrossTime*2f;
		}else {
			return (float)Math.pow(0.48, 2)*minCrossTime*2f;
		}
		
		
	}
	
	public static void main(String[] args) {
		String roadName = "TGC";
    	RoadNet road = new RoadNet("data/"+roadName+".txt");
    	int nodeSize = road.getVertexCount();
		
		boolean isRushHour = true;
		int edgeSize = road.starts.length;
		float minCrossTime[] = new float[edgeSize];
		for(int i=0;i<edgeSize;i++)
		{
			minCrossTime[i] = road.weights[i]+ calcExtraCost(road.capacitys[i], road.weights[i], isRushHour);
		}
        Dijkstra g = new Dijkstra(nodeSize,road.starts,road.ends, minCrossTime);
        for(int i= 0;i<nodeSize;i++)
        {
        	g = new Dijkstra(nodeSize,road.starts,road.ends, minCrossTime);
        	g.dijkstraTravasal(i, nodeSize-1);
        	FileStore f = new FileStore(i,nodeSize,roadName);
        	f.storeMinDist();
        	if(i%200 == 0)
        	{
        		System.out.printf("%d / %d (finished/total)\n", i,nodeSize);
        	}
        }
	}
}
