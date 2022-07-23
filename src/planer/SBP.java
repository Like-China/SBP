package planer;

import java.util.ArrayList;
import java.util.HashMap;

import dfs.Node;
import dfs.PathGetter;
import dfs.Turns;

/**
 * Step3-Self-Aware Batching processing Algorithm
 */
public class SBP {
	
	public static Graph g = new Graph(true);
	public static float[][] shortestDistsLow;
	public static float[][] shortestDistsHigh;
	public static float[][] dDist;
	
	public static int swapCheckCount = 0;
	
	public static boolean isStatic = false;
	public static boolean isSelfAware = true;
	public static boolean isExpTime = true;
	public static boolean isRushHour = true;
	public static boolean isPreCheck = true;
	public static String roadName = "NYC";
	
	/**
	 * The default settings
	 */
	public static int userSize = (roadName=="TGC")?4000:2000 ;
	public static int interval = 2;
	public static float e = 0.01f;
	public static int queryDensity = 50;
	public static int expNum = 20;
	
	/**
	 * The sum CPU time of 20 experiments
	 */
	public static long sumTime = 0;
	
	public static float minCost = Float.MAX_VALUE;
	
	public static void main(String[] args) {
		
		SBP.createGraph(roadName,isRushHour, isExpTime);
		// the varied parameters
		int[] userSizesNY = {2000,4000,6000,8000,10000};
		int[] userSizesTG = {4000,8000,12000,16000,20000};
		int[] userSizes = (roadName=="TGC")?userSizesTG:userSizesNY;
		float[] es = {0.0002f,0.0004f,0.0006f,0.0008f,0.001f};
		int[] intervals = {1,2,3,4,5};
		int[] queryDensitys = {20,40,60,80,100};
		
		// simulated trip queries
		UserGetter userGetter = new UserGetter("data/randomUser"+roadName+".txt",queryDensity,0);
		
		
		//  vary the trip count (user count) for SBP algorithm
		varyUserSize(userSizes, userGetter);
		
		// vary the trip count (user count) for Ind algorithm, remember set self-aware = false
		varyUserSizeForInd(userSizes, userGetter);
		
		//  vary the refining parameter epsilon
		varyPara(es, userGetter);
		
		//  vary the refining interval
		varyRefineInterval(intervals, userGetter);
		
		// vary the query arrival rate
		varyQueryDensity(queryDensitys, userGetter);
	}
	
	public static HashMap<Edge, ArrayList<UserNodeInfo>> generateUserNodeInfos(ArrayList<User> users)
	{
		HashMap<Edge, ArrayList<UserNodeInfo>> map = new HashMap<Edge, ArrayList<UserNodeInfo>>();

        for(int userCount=0;userCount<users.size();userCount++)
        {
        	User user = users.get(userCount);
	        ArrayList<Float> arriveTime = user.crossNodeTimes;
	        for(int i=0;i<user.crossNodeTimes.size()-1;i++)
	        {
	        	float arrTime = arriveTime.get(i);
	        	float proceTime;
	        	proceTime = arriveTime.get(i+1)-arriveTime.get(i);
	        	Edge thisEdge = user.crossEdges.get(i);
				if (!map.containsKey(thisEdge)) {
					map.put(thisEdge, new ArrayList<UserNodeInfo>());
				}
				map.get(thisEdge).add(new UserNodeInfo(arrTime,proceTime));
	        }
        }
        return map;
	}
	
	public static ArrayList<User> copyUser(ArrayList<User> users)
	{
		ArrayList<User> copyUsers = new ArrayList<User>();
		for(int i=0;i<users.size();i++)
		{
			copyUsers.add(users.get(i).copy());
		}
		return copyUsers;
	}
	
	public static ArrayList<User> getAffectUsers(User copyUser, User refiningUser, ArrayList<User> users, ArrayList<User> affectedUsers)
	{
		for(int i=0;i<refiningUser.crossEdges.size();i++)
		{
			Edge affectedEdge = refiningUser.crossEdges.get(i);
			for(int j=0;j<users.size();j++)
			{
				User calUser  = users.get(j);
				if(calUser.crossEdges.contains(affectedEdge) && !affectedUsers.contains(calUser))
				{
					for(int index = 0;index<calUser.crossEdges.size();index++)
					{
						if(calUser.crossEdges.get(index) == affectedEdge && calUser.crossNodeTimes.get(index)>refiningUser.crossNodeTimes.get(i))
						{
							affectedUsers.add(calUser);
							break;
						}
					}
				}
			}
		}
		
		return affectedUsers;
	}
	
	public static void createGraph(String roadName, boolean isRushHour, boolean isExpTime)
	{
		RoadNet road = new RoadNet("data/" + roadName + ".txt");
		int nodeSize = road.getVertexCount();
		int edgeSize = road.starts.length;
		shortestDistsLow = new float[nodeSize][nodeSize];
		shortestDistsHigh = new float[nodeSize][nodeSize];
		for (int i = 0; i < nodeSize; i++) {
			String distPath00 = "data/" + roadName + "Dist/"+ i + ".txt";
			String distPath01 = "data/" + roadName + "Dist/"+ i + ".txt";
			shortestDistsLow[i] = FileGetter.getDistMap(distPath00, nodeSize);
			shortestDistsHigh[i] = FileGetter.getDistMap(distPath01, nodeSize);
		}
		dDist = isRushHour?shortestDistsHigh:shortestDistsLow;
		for (int i = 0; i < nodeSize; i++) {
			g.addNode(i, (int) dDist[1][i]);
		}
		for (int i = 0; i < edgeSize; i++) {
			g.addMap(road.starts[i], road.ends[i], (int) road.weights[i], road.capacitys[i]);
			g.addMap(road.ends[i], road.starts[i], (int) road.weights[i], road.capacitys[i]);
		}
	}
	
	public static ArrayList<User> init(UserGetter userGetter,int batchSize, int batchCount, int refineSize, HashMap<Edge, ArrayList<UserNodeInfo>> map) {
		int userSize = refineSize;
		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < userSize; i++) {
			int startId = userGetter.starts.get(i+batchCount*batchSize);
			int endId = userGetter.ends.get(i+batchCount*batchSize);
			float stime = userGetter.startTimes.get(i+batchCount*batchSize);
			g.reNode(endId, dDist);
			InitialRouteSearch initialRouteSearch = new InitialRouteSearch();
			initialRouteSearch.travel(startId, endId, stime, g, isStatic, isSelfAware, map);
			users.add(new User(i, stime, startId, endId, initialRouteSearch.paths, initialRouteSearch.times, initialRouteSearch.crossEdges));
		}
		for(int i=0;i<users.size();i++)
		{
			BatchRefiningProcesssing.greedyUserQueue.add(users.get(i));
		}
		BatchRefiningProcesssing.Update(g);
		return users;
	}
	
	public static ArrayList<User> refine(ArrayList<User> users, float eplison)
	{
		ArrayList<User> copyUsers  = copyUser(users);
		while(true)
		{
			boolean flag = false;
			for(int userCount=0;userCount<users.size();userCount++)
			{
				Float cost1 = Shower.time(users); 
				User refiningUser = users.get(userCount);
				User copyUser = refiningUser.copy();
				users.remove(refiningUser);
				HashMap<Edge, ArrayList<UserNodeInfo>> othersMap = generateUserNodeInfos(users);
				
				int startId = refiningUser.startVertexId;
				int endId = refiningUser.endVertexId;
				float stime = refiningUser.departureTime;
				g.reNode(endId, dDist);
				InitialRouteSearch initialRouteSearch = new InitialRouteSearch();
				boolean loop = initialRouteSearch.travel(startId, endId, stime, g, othersMap);
				
				int size1 = copyUser.crossNodeIds.size();
				int size2 = initialRouteSearch.paths.size();
				
				
				ArrayList<User> affectedUsers = new ArrayList<User>();
				float maxD = calMaxDecrement(copyUser, users, affectedUsers);
				float maxDecrement = isPreCheck?maxD:Float.MAX_VALUE;
				if ( (size1== size2 && copyUser.crossNodeIds.get(size1-2).equals(initialRouteSearch.paths.get(size1-2))) ||loop ||maxDecrement<eplison*cost1) {
					users.add(userCount, copyUser);
				}else {
					refiningUser.crossNodeIds = initialRouteSearch.paths;
					refiningUser.crossNodeTimes = initialRouteSearch.times;
					refiningUser.crossEdges = initialRouteSearch.crossEdges;
					users.add(userCount, refiningUser);
					affectedUsers = getAffectUsers(copyUser, refiningUser, users, affectedUsers);
					
					for(int i=0;i<affectedUsers.size();i++)
					{
						BatchRefiningProcesssing.greedyUserQueue.add(affectedUsers.get(i));
					}
					
					BatchRefiningProcesssing.Update(g);
					swapCheckCount++;
					Float cost2 = Shower.time(users);
					
					
					if((cost1-cost2) > eplison*cost1)
					{
						copyUsers = copyUser(users);
						flag = true;
					}else {
						users = copyUser(copyUsers);
					}
				}
			}
			if(!flag)
			{
				return users;
			}
		}
	}
	
	public static float calMaxDecrement(User copyUser, ArrayList<User> users, ArrayList<User> affectedUsers)
	{
		int minCapacity = Integer.MAX_VALUE;
		int edgeSize = copyUser.crossEdges.size();
		for(int i=0;i<edgeSize;i++)
		{
			Edge affectedEdge = copyUser.crossEdges.get(i);
			if(affectedEdge.capacity < minCapacity)
			{
				minCapacity = affectedEdge.capacity;
			}
			for(int j=0;j<users.size();j++)
			{
				User calUser  = users.get(j);
				if(calUser.crossEdges.contains(affectedEdge) && !affectedUsers.contains(calUser))
				{
					for(int index = 0;index<calUser.crossEdges.size();index++)
					{
						if(calUser.crossEdges.get(index) == affectedEdge && calUser.crossNodeTimes.get(index)>copyUser.crossNodeTimes.get(i))
						{
							affectedUsers.add(calUser);
							break;
						}
					}
				}
			}
		}
		float userCount = affectedUsers.size();
		float routeSelfTime = copyUser.totalTimeCost(); 
		return routeSelfTime+edgeSize*userCount*(minCapacity*minCapacity-(minCapacity-1)*(minCapacity-1))/(minCapacity*minCapacity)*2;
	}
	
	public static ArrayList<int[]> getPath(String roadName, int startNode, int endNode) {
		RoadNet road = new RoadNet("data/" + roadName + ".txt");
		int vertexCount = road.getVertexCount();
		int[] starts = road.starts;
		int[] ends = road.ends;
		Node[] node = new Node[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			node[i] = new Node();
			node[i].setName(i);
		}
		HashMap<Integer, ArrayList<Node>> listHashMap = new HashMap<Integer, ArrayList<Node>>();
		for (int i = 0; i < starts.length; i++) {
			int start = starts[i];
			int end = ends[i];
			if (!listHashMap.containsKey(start)) {
				listHashMap.put(start, new ArrayList<Node>());
			}
			if (!listHashMap.containsKey(end)) {
				listHashMap.put(end, new ArrayList<Node>());
			}
			listHashMap.get(start).add(node[end]);
			listHashMap.get(end).add(node[start]);
		}
		for (int i = 0; i < vertexCount; i++) {
			ArrayList<Node> linkedNodes = listHashMap.get(i);
			if (linkedNodes != null) {
				node[i].setRelationNodes(linkedNodes);
			}
		}
		PathGetter.getPaths(node[startNode], null, node[startNode], node[endNode]);
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for (Object[] o : PathGetter.sers) {
			int[] path1 = new int[o.length];
			for (int i = 0; i < o.length; i++) {
				Node nNode = (Node) o[i];
				path1[i] = nNode.getName();
			}
			paths.add(path1);
		}
		return paths;
	}

	public static void exactAlgo(int userSize, int findCOunt, UserGetter userGetter)
	{
		SBP.createGraph(roadName,isRushHour, isExpTime);
		
		ArrayList<User> users = new ArrayList<User>();
		for(int i=0;i<userSize;i++)
		{
			int start = userGetter.starts.get(i);
			int end = userGetter.ends.get(i);
			float t = userGetter.startTimes.get(i);
			ArrayList<int[]> paths = getPath(roadName, start, end);
			PathGetter.sers = new ArrayList<Object[]>();
			User newUser = new User(i, t, start, end, paths);
			newUser.assignRoute(g);
			users.add(newUser);
			
		}
		
		ArrayList<User> optimalUsers = new ArrayList<User>();
		for(int count = 0;count < findCOunt;count++)
		{
			if(count % (int)(findCOunt*0.1) == 0)
			{
				System.out.println("Process£º"+count + "/ "+findCOunt);
			}
			for(int i=0;i<users.size();i++)
			{
				users.get(i).currentIndex = 0;
				users.get(i).assignRoute(g);
				BatchRefiningProcesssing.greedyUserQueue.add(users.get(i));
			}
			BatchRefiningProcesssing.Update(g);
			if( Shower.time(users) < minCost)
			{
				minCost = Shower.time(users);
				optimalUsers = copyUser(users);
			}
		}
		
			for(int i=0;i<users.size();i++)
			{
				BatchRefiningProcesssing.greedyUserQueue.add(users.get(i));
			}
			BatchRefiningProcesssing.Update(g);
	}
	
	public static void exactAlgo(int userSize, UserGetter userGetter)
	{
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<String[]> selectedIndex = new ArrayList<String[]>();
		for(int i=0;i<userSize;i++)
		{
			int start = userGetter.starts.get(i);
			int end = userGetter.ends.get(i);
			float t = userGetter.startTimes.get(i);
			ArrayList<int[]> paths = getPath(roadName, start, end);
			String[] ids = new String[paths.size()];
			selectedIndex.add(ids);
			for(int nnn=0;nnn<paths.size();nnn++)
			{
				ids[nnn] = nnn+"";
			}
			PathGetter.sers = new ArrayList<Object[]>();
			User newUser = new User(i, t, start, end, paths);
			newUser.assignRoute(g);
			users.add(newUser);
		}
		
		String[] a = selectedIndex.get(0);
		String[] b = selectedIndex.get(1);
		String[] add = Turns.turns(a, b);
		
		for(int i=2;i<userSize;i++)
		{
			b = selectedIndex.get(i);
			add = Turns.turns(add, b);
		}
		ArrayList<User> optimalUsers = new ArrayList<User>();
        
		
		for(int count = 0;count < add.length;count++)
		{
			if(count % (int)(add.length*0.1) == 0)
			{
				System.out.println("Process£º"+count + "/ "+add.length);
			}
			String selectIndex = add[count];
			for(int i=0;i<users.size();i++)
			{
				users.get(i).currentIndex = 0;
				users.get(i).assignRoute(g, Integer.parseInt(selectIndex.split(",")[i]));
				BatchRefiningProcesssing.greedyUserQueue.add(users.get(i));
			}
			BatchRefiningProcesssing.Update(g);
			if( Shower.time(users) < minCost)
			{
				minCost = Shower.time(users);
				optimalUsers = copyUser(users);
			}
		}
		
	}
	
		
	public static ArrayList<User> test(int userSize, int queryDensity, int refineInterval ,float e, UserGetter userGetter)
	{
		ArrayList<User> allUsers = new ArrayList<User>();
		int refineSize = queryDensity*refineInterval;
		int refineCount =  (userSize%refineSize == 0)? userSize/refineSize:userSize/refineSize+1;
		for(int i=0;i<refineCount;i++)
		{
			HashMap<Edge, ArrayList<UserNodeInfo>>  map= generateUserNodeInfos(allUsers);
			int curRefineSize = ((i+1)*refineSize>userSize)?userSize-i*refineSize:refineSize;
			ArrayList<User> users = init(userGetter, refineSize,i,curRefineSize, map);
			long t1 = System.currentTimeMillis();
			users = refine(users, e);
			long t2 = System.currentTimeMillis();
			sumTime +=  (t2-t1);
			allUsers.addAll(users);
			for(int j=0;j<allUsers.size();j++)
			{
				BatchRefiningProcesssing.greedyUserQueue.add(allUsers.get(j));
			}
			BatchRefiningProcesssing.Update(g);
		}
		return allUsers;
	}
	
	// vary user count
	public static void varyUserSize(int[] userSizes, UserGetter userGetter)
	{
		for(int i = 0;i<userSizes.length;i++)
		{
			System.out.println("**********************************");
			System.out.println("Current value: " + userSizes[i]);
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for(int expCount = 0; expCount<expNum;expCount++)
			{
				allUsers = test(userSizes[i], queryDensity, interval, e, userGetter);
			}
			System.out.println("\n"+expNum+"experiments the sum CPU time: "+ sumTime + 
					"\n"+expNum+"experiments the averaged CPU time: "+ sumTime/expNum+"\ntotal travel time£º"+Shower.time(allUsers));
			sumTime = 0;
		}
	}
	
	public static void varyUserSizeForInd(int[] userSizes, UserGetter userGetter)
	{	
		for(int i = 0;i<userSizes.length;i++)
		{
			System.out.println("**********************************");
			System.out.println("Current value: " + userSizes[i]);
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for(int expCount = 0; expCount<expNum;expCount++)
			{
				int refineSize = queryDensity*interval;
				int refineCount =  (userSizes[i]%refineSize == 0)? userSizes[i]/refineSize:userSizes[i]/refineSize+1;
				for(int m=0;m<refineCount;m++)
				{
					HashMap<Edge, ArrayList<UserNodeInfo>>  map= generateUserNodeInfos(allUsers);
					int curRefineSize = ((m+1)*refineSize>userSizes[i])?userSizes[i]-m*refineSize:refineSize;
					long t1 = System.currentTimeMillis();
					ArrayList<User> users = init(userGetter, refineSize,m,curRefineSize, map);
					long t2 = System.currentTimeMillis();
					sumTime += (t2-t1);
					allUsers.addAll(users);
					// update the arrival time
					for(int j=0;j<allUsers.size();j++)
					{
						BatchRefiningProcesssing.greedyUserQueue.add(allUsers.get(j));
					}
					BatchRefiningProcesssing.Update(g);
				}
			}
			System.out.println("\n"+expNum+"experiments the sum CPU time: "+ sumTime + 
					"\n"+expNum+"experiments the averaged CPU time: "+ sumTime/expNum+"\ntotal travel time£º"+Shower.time(allUsers));
		}
	}
	
	// vary refining interval
	public static void varyRefineInterval(int[] intervals,UserGetter userGetter)
	{
		for(int i = 0;i<intervals.length;i++)
		{
			System.out.println("**********************************");
			System.out.println("Current value: " + intervals[i]);
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for(int expCount = 0; expCount<expNum;expCount++)
			{
				allUsers = test(userSize, queryDensity, intervals[i], e, userGetter);
			}
			System.out.println("\n"+expNum+"experiments the sum CPU time: "+ sumTime + 
					"\n"+expNum+"experiments the averaged CPU time: "+ sumTime/expNum+"\ntotal travel time£º"+Shower.time(allUsers));
			sumTime = 0;
		}
	}
	
	// vary query density
	public static void varyQueryDensity(int[] queryDensitys, UserGetter userGetter)
	{
		for(int i = 0;i<queryDensitys.length;i++)
		{
			System.out.println("**********************************");
			System.out.println("Current value: " + queryDensitys[i]);
			userGetter = new UserGetter("data/randomUser"+roadName+".txt",queryDensitys[i],0);
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for(int expCount = 0; expCount<expNum;expCount++)
			{
				allUsers = test(userSize, queryDensitys[i], interval, e, userGetter);
			}
			System.out.println("\n"+expNum+"experiments the sum CPU time: "+ sumTime + 
					"\n"+expNum+"experiments the averaged CPU time: "+ sumTime/expNum+"\ntotal travel time£º"+Shower.time(allUsers));
			sumTime = 0;
		}
	}
	
	/**
	 * The performance of algorithms as we vary refining parameter.
	 * @param es  The  refining parameter epsilon
	 * @param userGetter The 
	 */
	public static void varyPara(float[] es, UserGetter userGetter)
	{
		for(int i = 0;i<es.length;i++)
		{
			System.out.println("**********************************");
			System.out.println("Current value: " + es[i]);
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for(int expCount = 0; expCount<expNum;expCount++)
			{
				allUsers = test(userSize, queryDensity, interval, es[i], userGetter);
			}
			System.out.println("\n"+expNum+"experiments the sum CPU time: "+ sumTime + 
					"\n"+expNum+"experiments the averaged CPU time: "+ sumTime/expNum+"\ntotal travel time£º"+Shower.time(allUsers));
			sumTime = 0;
		}
	}
}
