package planer;

class Node{
	int nodeId;
	float sDist = 100000;
	int dDist = 100000;
	int pre = 0;
	float currentTime = -1;
	Edge preEdge = null;
	
	public Node(int nodeId, int dDist) {
		super();
		this.nodeId = nodeId;
		this.dDist = dDist;
	}
	
}
