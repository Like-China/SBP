package utils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The edge
 */
public class Edge{
	public Node startNode;
	public Node endNode;
	public int minCrossTime;
	public int capacity;

	public Edge(Node startNode, Node endNode,int minCrossTime, int capacity) {
		super();
		this.startNode = startNode;
		this.endNode = endNode;
		this.minCrossTime = minCrossTime;
		this.capacity = capacity;
	}
	
	/**
	 * Get the non-query traffic flow of current time
	 * @param currentTime current time
	 * @capacity balance the averaged flow
	 * @return the non-query traffic flow of current time
	 */
	public  int calcNonQueryFlow(float currentTime)
	{
		int capacity_balance;
		capacity_balance = (int)(capacity*0.4);
		return (int)(0.2*capacity_balance*Math.sin(Math.PI/60*currentTime)+capacity_balance);
	}
	
	/**
	 *  calculate the extra travel time caused by planned routes
	 * @param currentFlow current flow
	 * @param minCrossTime the minimum travel time
	 * @param capacity the capacity of the edge
	 * @return the extra travel time caused by planned routes
	 */
	public float calcExtraCost(float currentFlow)
	{
		 float alpha = 2f;
		 float beta = 2f;
		 float cost = (float)Math.pow(currentFlow/capacity, beta)*minCrossTime*alpha;
		 return cost;
	}
	
	// Calculate the travel time to pass through the edge based on the current time
	public float getCrossTime(float currentTime, HashMap<Edge, ArrayList<UserNodeInfo>> map1)
	{
		int affectCount = 0;
		ArrayList<UserNodeInfo> userNodeInfos = map1.get(this);
		if(userNodeInfos != null)
		{ 
			for(int i=0;i<userNodeInfos.size();i++)
			{
				if(userNodeInfos.get(i).getArriveTime()<=currentTime && userNodeInfos.get(i).getProcessTime()+userNodeInfos.get(i).getArriveTime()>=currentTime)
				{
					affectCount ++;
				}
			}
		}
		int currentFlow = calcNonQueryFlow(currentTime) + affectCount;
		float delayTime = calcExtraCost(currentFlow);
		return minCrossTime + delayTime;
	}
	
	// Calculate the travel time to pass through the edge based on the current time
	public float getCrossTime(float currentTime, int affectCount)
	{
		int currentFlow = calcNonQueryFlow(currentTime) + affectCount;
		float delayTime = calcExtraCost(currentFlow);
		return minCrossTime + delayTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return startNode.nodeId+"->"+endNode.nodeId+" minCrossTime: "+minCrossTime+" capacity: "+capacity;
	}
}
