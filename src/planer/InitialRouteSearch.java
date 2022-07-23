package planer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * Step1-Initial Route Search 
 */
public class InitialRouteSearch {
	
	public  ArrayList<Integer> paths = new ArrayList<Integer>();
	public  ArrayList<Float> times = new ArrayList<Float>();
	public 	ArrayList<Edge> crossEdges = new ArrayList<Edge>();
	
	/**
	 * initialize the start and the end
	 * @param start the source
	 * @param end the destination
	 * @param t the departure time
	 * @param g the Graph
	 */
	public static void setRoot(int start, int end, float t, Graph g)
	{
		ArrayList<Node> nodes = g.nodes;
		nodes.get(start).pre = -1;
		nodes.get(start).sDist = 0;
		nodes.get(start).currentTime = t;
		nodes.get(end).dDist = 0; 
	}
	
	
	public static int go(int start, int end, float t, Graph g, Boolean isStatic,
			Boolean isSelfAware, HashMap<Edge, ArrayList<UserNodeInfo>> nodeMap)
	{
		ArrayList<Node> nodes = g.nodes;
		HashMap<Node, ArrayList<Edge>> map = g.edgeMap;
		Queue<Node> open = new PriorityQueue<Node>(MyComparator.openComparator); 
		setRoot(start, end, t, g);
		open.add(nodes.get(start));

		while(!open.isEmpty())
		{
			Node currentNode = open.poll();
			ArrayList<Edge> edges = map.get(currentNode);
			if(currentNode.nodeId == end)
			{
				return 1;
			}
			float currentTime = isStatic?t:currentNode.currentTime;
			for(int i=0;i<edges.size();i++)
			{
				Edge curEdge = edges.get(i);
				Node outNode = curEdge.endNode;
				float curEdgeCrossTime = isSelfAware?curEdge.getCrossTime(currentTime, nodeMap, SBP.isRushHour):curEdge.getCrossTime(currentTime, 0, SBP.isRushHour);
				if(currentNode.sDist+curEdgeCrossTime<outNode.sDist)
				{
					outNode.sDist = currentNode.sDist+curEdgeCrossTime;
					outNode.pre = currentNode.nodeId;
					outNode.currentTime = currentTime + curEdgeCrossTime;
					outNode.preEdge = curEdge;
					if(!open.contains(outNode))
					{
						open.add(outNode);
					}
				}
			}
		}
		return 0;
	}
	
	
	public boolean travel(int start, int end, float t, Graph g, boolean isStatic,
			Boolean isSelfAware, HashMap<Edge, ArrayList<UserNodeInfo>> nodeMap)
	{
		ArrayList<Node> nodes = g.nodes;
		int res = go(start, end,t, g, isStatic,isSelfAware,nodeMap);
		
		if(res == 0)
		{
			System.out.println(start+"->"+end+"is not connected");
		}else {
			Node curNode = nodes.get(end);
			while(curNode.nodeId != start)
			{ 
				
				if(paths.contains(curNode.nodeId))
				{
					return true;
				}
				paths.add(0,curNode.nodeId);
				times.add(0,curNode.currentTime);
				crossEdges.add(0,curNode.preEdge);
				curNode = nodes.get(curNode.pre);
			}
			paths.add(0,start);
			times.add(0,t);
		}
		return false;
	}

	public static int go(int start, int end, float t, Graph g, HashMap<Edge, ArrayList<UserNodeInfo>> nodeMap)
	{
		ArrayList<Node> nodes = g.nodes;
		HashMap<Node, ArrayList<Edge>> map = g.edgeMap;
		Queue<Node> open = new PriorityQueue<Node>(MyComparator.openComparator); 
		setRoot(start, end, t, g);
		open.add(nodes.get(start));

		while(!open.isEmpty())
		{
			Node currentNode = open.poll();
			ArrayList<Edge> edges = map.get(currentNode);
			if(currentNode.nodeId == end)
			{
				return 1;
			}
			float currentTime = currentNode.currentTime;
			for(int i=0;i<edges.size();i++)
			{
				Edge curEdge = edges.get(i);
				Node outNode = curEdge.endNode;
				float curEdgeCrossTime = curEdge.getCrossTime(currentTime, nodeMap, SBP.isRushHour);
				if(currentNode.sDist+curEdgeCrossTime<outNode.sDist)
				{
					outNode.sDist = currentNode.sDist+curEdgeCrossTime;
					outNode.pre = currentNode.nodeId;
					outNode.currentTime = currentTime + curEdgeCrossTime;
					outNode.preEdge = curEdge;
					if(!open.contains(outNode))
					{
						open.add(outNode);
					}
				}
			}
		}
		return 0;
	}
	
	public boolean travel(int start, int end, float t, Graph g, HashMap<Edge, ArrayList<UserNodeInfo>> nodeMap)
	{
		ArrayList<Node> nodes = g.nodes;
		int res = go(start, end,t, g,nodeMap);
		
		if(res == 0)
		{
			System.out.println(start+"->"+end+"is not connected");
		}else {
			Node curNode = nodes.get(end);
			while(curNode.nodeId != start)
			{ 
				
				if(paths.contains(curNode.nodeId))
				{
					return true;
				}
				paths.add(0,curNode.nodeId);
				times.add(0,curNode.currentTime);
				crossEdges.add(0,curNode.preEdge);
				curNode = nodes.get(curNode.pre);
			}
			paths.add(0,start);
			times.add(0,t);
		}
		return false;
	}

}


