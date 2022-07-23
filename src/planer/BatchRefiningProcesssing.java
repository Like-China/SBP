package planer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Step2-Batch Refining Processing
 */
public class BatchRefiningProcesssing {
	public static float sum1 = 0f;
	public static float sum2 = 0f;
	public static float sum3 = 0f;

	public static HashMap<Edge, PriorityQueue<UserNodeInfo>> hashmap = new HashMap<Edge, PriorityQueue<UserNodeInfo>>();
	public static Queue<User> greedyUserQueue = new PriorityQueue<User>(MyComparator.cUser);

	public static HashMap<Edge, ArrayList<UserNodeInfo>> hashmap1 = new HashMap<Edge, ArrayList<UserNodeInfo>>();
	
	public static void Update(Graph g) {
		while (!greedyUserQueue.isEmpty()) { 
			// 1.  select a user
			long t1 = System.currentTimeMillis();
			User user = greedyUserQueue.poll();
			long t2 = System.currentTimeMillis();
			int currentIndex = user.getCurrentIndex();
			float arrTime = user.currentTime;
			// 2. expansion
			int minCostNodeId = user.crossNodeIds.get(currentIndex + 1);
			sum1 += (t2 - t1);
			t1 = System.currentTimeMillis();
			Edge currentEdge = user.crossEdges.get(currentIndex);
			int affectCount = AffectCounter.calcAffectCount(hashmap.get(currentEdge), arrTime);
			float nextCost = currentEdge.getCrossTime(arrTime, affectCount, SBP.isRushHour);
			 t2 = System.currentTimeMillis();
			sum2 += (t2 - t1);
			// update the records of edge labels
			t1 = System.currentTimeMillis();
			if (!hashmap.containsKey(currentEdge)) {
				hashmap.put(currentEdge, new PriorityQueue<UserNodeInfo>(MyComparator.nodeInfoComparator));
			}
			hashmap.get(currentEdge).add(new UserNodeInfo(arrTime, nextCost));
			// update the records of the user
			user.crossNodeTimes.set(currentIndex + 1, arrTime + nextCost);
			user.setCurrentIndex(currentIndex + 1);
			
			if (minCostNodeId == user.endVertexId) {
				user.setCurrentIndex(0);
			} else {
				greedyUserQueue.add(user);
			}
			t2 = System.currentTimeMillis();
			sum3 += (t2 - t1);
		}
		hashmap = new HashMap<Edge, PriorityQueue<UserNodeInfo>>();
	}
	

}
