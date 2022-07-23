package precomputation;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import planer.RoadNet;

public class UserGenerator {

	String userRequestFilepath;
	int minLen = 0;
	int maxLen = 20;
	public int userSize = 1000;
	int departureTime = 0;
	RoadNet road = null;
	
	public int[] starts;
	public int[] ends;
	public int[] times;

	
	public UserGenerator(int minLen, int maxLen, int userSize, int departureTime, RoadNet road) {
		userRequestFilepath = "data/randomUserTGC.txt";
		this.maxLen = maxLen;
		this.minLen = minLen;
		this.userSize = userSize;
		this.departureTime = departureTime;
		this.road = road;
		
		starts = new int[userSize];
		ends = new int[userSize];
		times = new int[userSize];
	}

	public void storeUser( ) {
		try {
			File file = new File(userRequestFilepath);
			Writer out = new FileWriter(file);
			Dijkstra g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);

			for (int i = 0; i < userSize; i++) {
				if(i%100 ==0)
				{
					System.out.println(i+"/"+userSize);
				}
				int startNode = (int) (Math.random() * 50);
				int endtNode = (int) (Math.random() * 50);
				float time = departureTime + i;
				g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
				g1.dijkstraTravasal(startNode, endtNode);
				ArrayList<Integer> paths = ShortestPath.findShorestPaths(Dijkstra.preVisited, endtNode);
				while (endtNode == startNode || paths.size() > maxLen || paths.size() < minLen
						|| g1.outEdgesHashMap.get(startNode).size() < 3 || g1.outEdgesHashMap.get(endtNode).size() < 3
						|| paths.size() < 3) {
					startNode = (int) (Math.random() * 50);
					endtNode = (int) (Math.random() * 50);
					g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
					g1.dijkstraTravasal(startNode, endtNode);
					paths = ShortestPath.findShorestPaths(Dijkstra.preVisited, endtNode);
				}
				String data = i + " " + time + " " + startNode + " " + endtNode + "\n";
				out.write(data);
			}
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void generate() {

		Dijkstra g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
		
		for (int i = 0; i < userSize; i++) {
			int startNode = (int) (Math.random() * 40);
			int endtNode = (int) (Math.random() * 50);
			departureTime = departureTime + new Random().nextInt(5);
			g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
			g1.dijkstraTravasal(startNode, endtNode);
			ArrayList<Integer> paths = ShortestPath.findShorestPaths(Dijkstra.preVisited, endtNode);
			while (endtNode == startNode || paths.size() > maxLen || paths.size() < minLen
					|| g1.outEdgesHashMap.get(startNode).size() < 3 || g1.outEdgesHashMap.get(endtNode).size() < 3
					|| paths.size() < 3) {
				startNode = (int) (Math.random() * 40);
				endtNode = (int) (Math.random() * 50);
				g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
				g1.dijkstraTravasal(startNode, endtNode);
				paths = ShortestPath.findShorestPaths(Dijkstra.preVisited, endtNode);
			}
			starts[i] = startNode;
			ends[i] = endtNode;
			times[i] = departureTime;
		}
	}

}