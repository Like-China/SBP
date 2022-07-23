package planer;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Graph
 */
class Graph{
	ArrayList<Node> nodes = new ArrayList<Node>();
	HashMap<Node, ArrayList<Edge>> edgeMap = new HashMap<Node, ArrayList<Edge>>();
	
	boolean isDirected = true;
	
	public Graph() {
		// TODO Auto-generated constructor stub
	}
	
	public Graph(boolean isDirected)
	{
		this.isDirected =  isDirected;
	}
	
	
	
	/**
	 * add vertex
	 * @param nodeId the node id
	 * @param dDist the evaluated distance from the node to the destination
	 */
	public void addNode(int nodeId, int dDist)
	{
		Node newNode = new Node(nodeId, dDist);
		nodes.add(newNode);
		edgeMap.put(newNode,new ArrayList<Edge>());
	}
	
	
	
	/**
	 * update the node records
	 * @param endId the destination id
	 * @param shortestDist the evaluated shortest distances 
	 */
	public void reNode(int endId, float[][] shortestDist)
	{
		for(int i=0;i<nodes.size();i++)
		{
			nodes.get(i).dDist = (int)shortestDist[endId][i];
			nodes.get(i).pre = 0;
			nodes.get(i).sDist = 100000;
		}
	}

	
	
	public void addMap(int startId,int endId,int weight,int capacity)
	{
		Node startNode = nodes.get(startId);
		Node endNode = nodes.get(endId);
		
		if(isDirected)
		{
			Edge newEdge = new Edge(startNode, endNode, weight,capacity);
			edgeMap.get(startNode).add(newEdge);
		}else {
			Edge newEdge00 = new Edge(startNode, endNode, weight,capacity);
			Edge newEdge01 = new Edge(endNode,startNode,  weight,capacity);
			edgeMap.get(startNode).add(newEdge00);
			edgeMap.get(endNode).add(newEdge01);
		}
	}
	
	
	
}
