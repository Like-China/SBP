package utils;

public class Node{
	public int nodeId;
	public float sDist = 100000;
	public int dDist = 100000;
	public int pre = 0;
	public float currentTime = -1;
	public Edge preEdge = null;
	
	public Node(int nodeId, int dDist) {
		super();
		this.nodeId = nodeId;
		this.dDist = dDist;
	}
	
}
