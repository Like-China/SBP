package planer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import utils.AffectCounter;
import utils.Edge;
import utils.Graph;
import utils.MyComparator;
import utils.User;
import utils.UserNodeInfo;

/**
 * After knowing planned routes of all trips, update the trip info of each user
 */
public class UserUpdate {
	public static HashMap<Edge, PriorityQueue<UserNodeInfo>> hashmap = new HashMap<Edge, PriorityQueue<UserNodeInfo>>();
	public static Queue<User> greedyUserQueue = new PriorityQueue<User>(MyComparator.cUser);

	public static void update(Graph g) {
		while (!greedyUserQueue.isEmpty()) {
			// 1. select a user with minimal current timestamp
			User user = greedyUserQueue.poll();
			// 2. expansion
			int currentIndex = user.getCurrentIndex();
			float currentTime = user.currentTime;
			int nextNodeId = user.crossNodeIds.get(currentIndex + 1);
			Edge currentEdge = user.crossEdges.get(currentIndex);
			int affectCount = AffectCounter.calcAffectCount(hashmap.get(currentEdge), currentTime);
			float realNextCost = currentEdge.getCrossTime(currentTime, affectCount);
			// update the records of edge labels
			if (!hashmap.containsKey(currentEdge)) {
				hashmap.put(currentEdge, new PriorityQueue<UserNodeInfo>(MyComparator.nodeInfoComparator));
			}
			hashmap.get(currentEdge).add(new UserNodeInfo(currentTime, realNextCost));
			// update the records of the user
			user.crossNodeTimes.set(currentIndex + 1, currentTime + realNextCost);
			user.setCurrentIndex(currentIndex + 1);

			if (nextNodeId == user.endVertexId) {
				user.setCurrentIndex(0);
			} else {
				greedyUserQueue.add(user);
			}
		}
		hashmap = new HashMap<Edge, PriorityQueue<UserNodeInfo>>();
	}

}
