package utils;

import java.util.*;

/**
 * User
 * A trip query is specified by a user.
 */
public class User{
	/**
	 * user id
	 */
	public int userId;
	/**
	 * departure time
	 */
	public float departureTime;
	/**
	 * the source vertex
	 */
	public int startVertexId;
	/**
	 * the destination vertex
	 */
	public int endVertexId;
	/**
	 * the current number of vertexes that the user has traveled
	 */
	public int currentIndex;
	/**
	 * the vertex that the user is traveling
	 */
	public int currentVertex;
	/**
	 * the current time
	 */
	public float currentTime;
	
	/**
	 * three lists are used to record the vertexes, times and edges that the user has traveled
	 */
	public ArrayList<Integer> crossNodeIds = new ArrayList<Integer>();
	public ArrayList<Float> crossNodeTimes = new ArrayList<Float>();
	public 	ArrayList<Edge> crossEdges = new ArrayList<Edge>();
	
	/**
	 * The possible paths from the source to destination
	 */
	public ArrayList<int[]> allPaths = new ArrayList<int[]>();
	
	/**
	 * The user generating 1 for our SBP algorithm
	 * 
	 * @param userId        user id
	 * @param departureTime departure time
	 * @param startVertexId the source vertex id
	 * @param endVertexId   the destination id
	 * @param paths         the vertexes the user has traveled
	 * @param times         the times at each traveled vertex
	 */
	public User(int userId, float departureTime, int startVertexId, int endVertexId, ArrayList<Integer> paths,
			ArrayList<Float> times, ArrayList<Edge> crossEdges) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
		this.departureTime = departureTime;
		this.startVertexId = startVertexId;
		this.endVertexId = endVertexId;

		// set the route and corresponding records
		this.crossNodeIds = paths;
		this.crossNodeTimes = times;
		this.currentIndex = 0;
		this.currentVertex = crossNodeIds.get(currentIndex);
		this.currentTime = crossNodeTimes.get(currentIndex);
		this.crossEdges = crossEdges;
	}

	/**
	 * The user generating 2 for exact algorithm
	 * 
	 * @param userId        user id
	 * @param departureTime departure time
	 * @param startVertexId the source vertex id
	 * @param endVertexId   the destination id
	 */
	public User(int userId, float departureTime, int startVertexId, int endVertexId, ArrayList<int[]> allPaths) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
		this.departureTime = departureTime;
		this.startVertexId = startVertexId;
		this.endVertexId = endVertexId;

		this.currentIndex = 0;
		this.currentTime = departureTime;
		this.currentVertex = startVertexId;
		
		this.allPaths = allPaths;
	}

	/**
	 * user copy
	 * @return a copied user
	 */
	public User copy() {
		return new User(userId, departureTime, startVertexId, endVertexId, new ArrayList<Integer>(crossNodeIds),
				new ArrayList<Float>(crossNodeTimes) , new ArrayList<Edge>(crossEdges));
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * set the current index of the user, denoting how many vertexes that the user has traveled
	 * @param index the current index of the user
	 */
	public void setCurrentIndex(int index) {
		currentIndex = index;
		currentVertex = crossNodeIds.get(currentIndex);
		currentTime = crossNodeTimes.get(currentIndex);
	}

	
	public void showInfo() {
		System.out.println(userId + " " + departureTime + " " + startVertexId + "->" + endVertexId);
		System.out.println(crossNodeIds);
		System.out.println(crossNodeTimes);
	}

	/**
	 * calculate the travel time from the source to destination
	 * @return the travel time from the source to destination
	 */
	public float totalTimeCost() {
		return crossNodeTimes.get(crossNodeTimes.size() - 1) - departureTime;
	}
	
	
	/**
	 * exact algorithm - randomly assign a route
	 * @param g graph object
	 */
	public void assignRoute(Graph g)
	{
		crossNodeIds = new ArrayList<Integer>();
		crossNodeTimes = new ArrayList<Float>();
		crossEdges = new ArrayList<Edge>();
		int randomIndex = new Random().nextInt(allPaths.size());
		int[] routeSelected = allPaths.get(randomIndex);
		for(int n = 0;n<routeSelected.length;n++)
		{
			int curNodeId = routeSelected[n];
			this.crossNodeIds.add(curNodeId);
			this.crossNodeTimes.add(departureTime);
			if(n == routeSelected.length - 1)
			{
				break;
			}
			Edge curEdge = null;
			Node curNode = g.nodes.get(curNodeId);
			for(Edge e:g.edgeMap.get(curNode))
			{
				if(e.endNode.nodeId == routeSelected[n+1])
				{
					curEdge = e;
					this.crossEdges.add(curEdge);
				}
			}
			
		}
		
	}
	
	/**
	 * exact algorithm - randomly assign a route
	 * @param g  graph object
	 * @param randomIndex the 
	 */
	public void assignRoute(Graph g, int randomIndex)
	{
		crossNodeIds = new ArrayList<Integer>();
		crossNodeTimes = new ArrayList<Float>();
		crossEdges = new ArrayList<Edge>();
		int[] routeSelected = allPaths.get(randomIndex);
		for(int n = 0;n<routeSelected.length;n++)
		{
			int curNodeId = routeSelected[n];
			this.crossNodeIds.add(curNodeId);
			this.crossNodeTimes.add(departureTime);
			if(n == routeSelected.length - 1)
			{
				break;
			}
			Edge curEdge = null;
			Node curNode = g.nodes.get(curNodeId);
			for(Edge e:g.edgeMap.get(curNode))
			{
				if(e.endNode.nodeId == routeSelected[n+1])
				{
					curEdge = e;
					this.crossEdges.add(curEdge);
				}
			}
		}
	}
}
