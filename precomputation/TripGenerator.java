package precomputation;
import java.io.*;
import java.util.ArrayList;
import utils.RoadNet;

/*
 * Generate a batch of trips, each trip is in the form of [id, ts, startVertex, endVertex]
 */
public class TripGenerator {

	String userRequestFilepath;
	int minLen;
	int maxLen;
	public int userSize;
	int departureTime;
	RoadNet road = null;
	
	public int[] starts;
	public int[] ends;
	public int[] times;

	
	public TripGenerator(int minLen, int maxLen, int userSize, int departureTime, RoadNet road) {
		userRequestFilepath = "data/randomUser.txt";
		this.maxLen = maxLen;
		this.minLen = minLen;
		this.userSize = userSize;
		this.departureTime = departureTime;
		this.road = road;
		
		starts = new int[userSize];
		ends = new int[userSize];
		times = new int[userSize];
	}

	// generate a set of users and store in a txt file
	public void store( ) {
		try {
			File file = new File(userRequestFilepath);
			Writer out = new FileWriter(file);
			Dijkstra g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);

			int selectedIndexRange = 140;

			for (int i = 0; i < userSize; i++) {
				if(i>0 && i%2000 == 0)
				{
					System.out.println(i+"/"+userSize);
				}
				int startNode = (int) (Math.random() * selectedIndexRange);
				int endtNode = (int) (Math.random() * selectedIndexRange);
				float time = departureTime + i;
				g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
				ArrayList<Integer> paths = g1.dijkstraTravasal(startNode, endtNode); // Same as above line
				// ArrayList<Integer> paths = ShortestPath.findShorestPaths(Dijkstra.preVisited, endtNode);
				while (endtNode == startNode || paths.size() > maxLen || paths.size() < minLen
						|| g1.outEdgesHashMap.get(startNode).size() < 3 || g1.outEdgesHashMap.get(endtNode).size() < 3
						|| paths.size() < 3) {
					startNode = (int) (Math.random() * selectedIndexRange);
					endtNode = (int) (Math.random() * selectedIndexRange);
					g1 = new Dijkstra(road.getVertexCount(), road.starts, road.ends, road.weights);
					paths = g1.dijkstraTravasal(startNode, endtNode);
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

	public static void main(String[] args) {
		RoadNet road = new RoadNet("data/NYC.txt");
		TripGenerator u = new TripGenerator(2, 5, 10000, 0, road);
		u.store();
	}

}