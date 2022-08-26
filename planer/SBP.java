package planer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import utils.Edge;
import utils.FileGetter;
import utils.Graph;
import utils.RoadNet;
import utils.Shower;
import utils.User;
import utils.TripGetter;
import utils.UserNodeInfo;

/**
 * Step3-Self-Aware Batching processing Algorithm
 */
public class SBP {

	public  Graph g ;
	// shortest distance during the rush hours and non-rush hour
	public  float[][] shortestDistsLow;
	public  float[][] shortestDistsHigh;
	public  float[][] dDist;
	public  int swapCheckCount = 0;
	public  String roadName = Settings.roadName;
	// The sum CPU time of multiple experiments
	public static long sumTime = 0;
	public  float minCost = Float.MAX_VALUE;

	public  static ArrayList<User> copyUser(ArrayList<User> users) {
		ArrayList<User> copyUsers = new ArrayList<User>();
		for (int i = 0; i < users.size(); i++) {
			copyUsers.add(users.get(i).copy());
		}
		return copyUsers;
	}

	// establish Graph g
	public  void createGraph() {
		g = new Graph(true);
		RoadNet road = new RoadNet("data/" + roadName + ".txt");
		int nodeSize = road.getVertexCount();
		int edgeSize = road.starts.length;
		dDist = new float[nodeSize][nodeSize];
		for (int i = 0; i < nodeSize; i++) {
			String distPath00 = "data/" + roadName + "Dist/" + i + ".txt";
			dDist[i] = FileGetter.getDistMap(distPath00, nodeSize);
		}
		for (int i = 0; i < nodeSize; i++) {
			g.addNode(i, (int) dDist[1][i]);
		}
		for (int i = 0; i < edgeSize; i++) {
			g.addMap(road.starts[i], road.ends[i], (int) road.weights[i], road.capacitys[i]);
			g.addMap(road.ends[i], road.starts[i], (int) road.weights[i], road.capacitys[i]);
		}
	}

	// retuen all edges lables using planned routes for users
	public  HashMap<Edge, ArrayList<UserNodeInfo>> generateUserNodeInfos(ArrayList<User> users) {
		HashMap<Edge, ArrayList<UserNodeInfo>> map = new HashMap<Edge, ArrayList<UserNodeInfo>>();
		for (int userCount = 0; userCount < users.size(); userCount++) {
			User user = users.get(userCount);
			ArrayList<Float> arriveTime = user.crossNodeTimes;
			for (int i = 0; i < user.crossNodeTimes.size() - 1; i++) {
				float arrTime = arriveTime.get(i);
				float proceTime;
				proceTime = arriveTime.get(i + 1) - arriveTime.get(i);
				Edge thisEdge = user.crossEdges.get(i);
				if (!map.containsKey(thisEdge)) {
					map.put(thisEdge, new ArrayList<UserNodeInfo>());
				}
				map.get(thisEdge).add(new UserNodeInfo(arrTime, proceTime));
			}
		}
		return map;
	}

	// INIT each User instance with inital-searched route and time
	public  ArrayList<User> init(TripGetter userGetter, int batchSize, int batchCount, int refineSize,
			HashMap<Edge, ArrayList<UserNodeInfo>> map, boolean isSelfAware) {
		// current batch size of uses, in most cases it equals batchSize
		int userSize = refineSize;
		ArrayList<User> users = new ArrayList<User>();
		for (int i = 0; i < userSize; i++) {
			int startId = userGetter.starts.get(i + batchCount * batchSize);
			int endId = userGetter.ends.get(i + batchCount * batchSize);
			float stime = userGetter.startTimes.get(i + batchCount * batchSize);
			g.reNode(endId, dDist);
			InitialRouteSearch initialRouteSearch = new InitialRouteSearch();
			initialRouteSearch.travel(startId, endId, stime, g, map, isSelfAware);
			users.add(new User(i, stime, startId, endId, initialRouteSearch.paths, initialRouteSearch.times,
					initialRouteSearch.crossEdges));
		}
		return users;
	}

	// For a refined trip, get all other trips that may be affected. 
	public  ArrayList<User> getAffectUsers(User copyUser, User refiningUser, ArrayList<User> users,
			ArrayList<User> affectedUsers) {
		for (int i = 0; i < refiningUser.crossEdges.size(); i++) {
			Edge affectedEdge = refiningUser.crossEdges.get(i);
			for (int j = 0; j < users.size(); j++) {
				User calUser = users.get(j);
				if (calUser.crossEdges.contains(affectedEdge) && !affectedUsers.contains(calUser)) {
					for (int index = 0; index < calUser.crossEdges.size(); index++) {
						if (calUser.crossEdges.get(index) == affectedEdge
								&& calUser.crossNodeTimes.get(index) > refiningUser.crossNodeTimes.get(i)) {
							affectedUsers.add(calUser);
							break;
						}
					}
				}
			}
		}
		return affectedUsers;
	}

	public  ArrayList<User> refine(ArrayList<User> users, float eplison) {
		ArrayList<User> copyUsers = copyUser(users);
		while (true) {
			boolean flag = false;
			for (int userCount = 0; userCount < users.size(); userCount++) {
				Float rawCost = Shower.time(users);
				User refiningUser = users.get(userCount);
				User copyUser = refiningUser.copy();
				users.remove(refiningUser);
				HashMap<Edge, ArrayList<UserNodeInfo>> othersMap = generateUserNodeInfos(users);

				int startId = refiningUser.startVertexId;
				int endId = refiningUser.endVertexId;
				float stime = refiningUser.departureTime;
				g.reNode(endId, dDist);
				InitialRouteSearch initialRouteSearch = new InitialRouteSearch();
				boolean loop = initialRouteSearch.travel(startId, endId, stime, g, othersMap, Settings.isSelfAware);

				int size1 = copyUser.crossNodeIds.size();
				int size2 = initialRouteSearch.paths.size();

				ArrayList<User> affectedUsers = new ArrayList<User>();
				float maxD = calMaxDecrement(copyUser, users, affectedUsers);
				float maxDecrement = Settings.isPreCheck ? maxD : Float.MAX_VALUE;
				// the route is the same as the raw route, or loop terminates, or such update route cannot sufficiently reduce time cost
				// add the copyed user back
				if ((size1 == size2
						&& copyUser.crossNodeIds.get(size1 - 2).equals(initialRouteSearch.paths.get(size1 - 2)))  //|| loop
						|| maxDecrement < eplison * rawCost) {
					users.add(userCount, copyUser);
				// the updated route is valid
				// add the refined user instead
				} else {
					refiningUser.crossNodeIds = initialRouteSearch.paths;
					refiningUser.crossNodeTimes = initialRouteSearch.times;
					refiningUser.crossEdges = initialRouteSearch.crossEdges;
					users.add(userCount, refiningUser);
					// affectedUsers = getAffectUsers(copyUser, refiningUser, users, affectedUsers);
					affectedUsers = users;
					UserUpdate.greedyUserQueue.addAll(affectedUsers);
					UserUpdate.update(g);
					swapCheckCount++;
					Float refinedCost = Shower.time(users);

					if ((rawCost - refinedCost) >= eplison * rawCost) {
						copyUsers = copyUser(users);
						flag = true;
					} else {
						users = copyUser(copyUsers);
					}
				}
			}
			if (!flag) {
				return users;
			}
			
		}
	}

	public  float calMaxDecrement(User copyUser, ArrayList<User> users, ArrayList<User> affectedUsers) {
		int minCapacity = Integer.MAX_VALUE;
		int edgeSize = copyUser.crossEdges.size();
		for (int i = 0; i < edgeSize; i++) {
			Edge affectedEdge = copyUser.crossEdges.get(i);
			if (affectedEdge.capacity < minCapacity) {
				minCapacity = affectedEdge.capacity;
			}
			for (int j = 0; j < users.size(); j++) {
				User calUser = users.get(j);
				if (calUser.crossEdges.contains(affectedEdge) && !affectedUsers.contains(calUser)) {
					for (int index = 0; index < calUser.crossEdges.size(); index++) {
						if (calUser.crossEdges.get(index) == affectedEdge
								&& calUser.crossNodeTimes.get(index) > copyUser.crossNodeTimes.get(i)) {
							affectedUsers.add(calUser);
							break;
						}
					}
				}
			}
		}
		float userCount = affectedUsers.size();
		float routeSelfTime = copyUser.totalTimeCost();
		return routeSelfTime + edgeSize * userCount
				* (minCapacity * minCapacity - (minCapacity - 1) * (minCapacity - 1)) / (minCapacity * minCapacity) * 2;
	}

	public  ArrayList<User> sbp(int userSize, int queryDensity, int refineInterval, float e,
			TripGetter userGetter) {
		sumTime = 0;
		createGraph();
		ArrayList<User> allUsers = new ArrayList<User>();
		int refineSize = queryDensity * refineInterval;
		int refineCount = (userSize % refineSize == 0) ? userSize / refineSize : userSize / refineSize + 1;
		for (int i = 0; i < refineCount; i++) {
			// store exisitng edge labels in each edge of all batches of users
			HashMap<Edge, ArrayList<UserNodeInfo>> map = generateUserNodeInfos(allUsers);
			int currentBatchSize = ((i + 1) * refineSize > userSize) ? userSize - i * refineSize : refineSize;
			// a batch of users with initial routes
			long t1 = System.currentTimeMillis();
			ArrayList<User> users = init(userGetter, refineSize, i, currentBatchSize, map, Settings.isSelfAware);
			// a batch of users with refined routes
			users = refine(users, e);
			allUsers.addAll(users);
			long t2 = System.currentTimeMillis();
			sumTime += (t2 - t1);
			// update the arrival time
			UserUpdate.greedyUserQueue.addAll(allUsers);
			UserUpdate.update(g);
		}
		return allUsers;
	}

	public  ArrayList<User> ind(int userSize, int queryDensity, int refineInterval, float e,
			TripGetter userGetter) {
		sumTime = 0;
		createGraph();
		ArrayList<User> allUsers = new ArrayList<User>();
		int refineSize = queryDensity * refineInterval;
		int refineCount = (userSize % refineSize == 0) ? userSize / refineSize : userSize / refineSize + 1;
		for (int m = 0; m < refineCount; m++) {
			HashMap<Edge, ArrayList<UserNodeInfo>> map = generateUserNodeInfos(allUsers);
			int currentBatchSize = ((m + 1) * refineSize > userSize) ? userSize - m * refineSize
					: refineSize;
			long t1 = System.currentTimeMillis();
			ArrayList<User> users = init(userGetter, refineSize, m, currentBatchSize, map, false);
			long t2 = System.currentTimeMillis();
			sumTime += (t2 - t1);
			allUsers.addAll(users);
			// update the arrival time
			UserUpdate.greedyUserQueue.addAll(allUsers);
			UserUpdate.update(g);
		}
		return allUsers;
	}

}
