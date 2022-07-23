package precomputation;


public class Edge 
{
	private int startVertex;
	private int endVertex;
	private float weight;

	public Edge(int startVertex, int endVertex, float minCrossTime,int capacity) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.weight = minCrossTime;
	}

	public int getStartVertex() {
		return startVertex;
	}

	public int getEndVertex() {
		return endVertex;
	}

	public float getWeight() {
		return weight;
	}

	public void setStartVertex(int startVertex) {
		this.startVertex = startVertex;
	}

	public void setEndVertex(int endVertex) {
		this.endVertex = endVertex;
	}

}

