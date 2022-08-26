package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Get the road network information
 * [edgeId vertexId_1 vertex_Id2 distance]
 */
public class RoadNet {
	private String filepathString;
	private int edgeCount = 0;
	private int vertexCount = 0;
	private float[] weightRange = new float[2];
	public int[] starts;
	public int[] ends;
	public float[] weights;
	public int[] capacitys;

	public String getFilepathString() {
		return filepathString;
	}

	public void setFilepathString(String filepathString) {
		this.filepathString = filepathString;
	}

	public int getEdgeCount() {
		return edgeCount;
	}

	public void setEdgeCount(int edgeCount) {
		this.edgeCount = edgeCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public float[] getWeightRange() {
		return weightRange;
	}

	public void setWeightRange(float[] weightRange) {
		this.weightRange = weightRange;
	}

	public RoadNet() {
		filepathString = "data/NY.txt";
		nodeCount();
		starts = new int[edgeCount];
		ends = new int[edgeCount];
		weights = new float[edgeCount];
		getInfo();
		getCostRange();
	}

	public RoadNet(String path) {
		filepathString = path;
		nodeCount();
		starts = new int[edgeCount];
		ends = new int[edgeCount];
		weights = new float[edgeCount];
		capacitys = new int[edgeCount];
		getInfo();
		getCostRange();

	}

	// The required number of nodes is maxID+1
	public void nodeCount() {
		try {
			File file = new File(filepathString);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			while ((lineString = reader.readLine()) != null) {
				edgeCount++;
				int nodeId1 = Integer.parseInt(lineString.split(" ")[1]);
				int nodeId2 = Integer.parseInt(lineString.split(" ")[2]);
				if (nodeId1 > vertexCount) {
					vertexCount = nodeId1;
				}
				if (nodeId2 > vertexCount) {
					vertexCount = nodeId2;
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		vertexCount = vertexCount + 1;
	}

	// Each line: [id, start_vertex_id, end_vertex_id, weight(minCrossTime),
	// capacity]
	public void getInfo() {
		try {
			File file = new File(filepathString);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			int count = 0;
			while ((lineString = reader.readLine()) != null) {
				starts[count] = Integer.parseInt(lineString.split(" ")[1]);
				ends[count] = Integer.parseInt(lineString.split(" ")[2]);
				weights[count] = Float.parseFloat(lineString.split(" ")[3]);
				capacitys[count] = Integer.parseInt(lineString.split(" ")[4]);
				count++;
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// get the range of minCrossTime of all moving objects
	public void getCostRange() {
		float minCost = Float.MAX_VALUE;
		float maxCost = 0f;
		try {
			File file = new File(filepathString);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			while ((lineString = reader.readLine()) != null) {
				float nodeCost = Float.parseFloat(lineString.split(" ")[3]);
				if (nodeCost > maxCost) {
					maxCost = nodeCost;
				}
				if (nodeCost < minCost) {
					minCost = nodeCost;
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		weightRange[0] = minCost;
		weightRange[1] = maxCost;
	}

}
