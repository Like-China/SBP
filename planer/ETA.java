package planer;

import java.util.ArrayList;
import java.util.HashMap;

import dfs.Node;
import dfs.PathGetter;
import dfs.Turns;
import utils.Graph;
import utils.RoadNet;
import utils.Shower;
import utils.User;
import utils.TripGetter;

public class ETA {
    public String roadName = Settings.roadName;
    public float minCost = Float.MAX_VALUE;
	public Graph g = new Graph(true);
	
    // get all paths using network expansion
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

    public void exactAlgo(int userSize, TripGetter userGetter) {
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<String[]> selectedIndex = new ArrayList<String[]>();
		for (int i = 0; i < userSize; i++) {
			int start = userGetter.starts.get(i);
			int end = userGetter.ends.get(i);
			float t = userGetter.startTimes.get(i);
			ArrayList<int[]> paths = getPath(roadName, start, end);
			String[] ids = new String[paths.size()];
			selectedIndex.add(ids);
			for (int nnn = 0; nnn < paths.size(); nnn++) {
				ids[nnn] = nnn + "";
			}
			PathGetter.sers = new ArrayList<Object[]>();
			User newUser = new User(i, t, start, end, paths);
			newUser.assignRoute(g);
			users.add(newUser);
		}

		String[] a = selectedIndex.get(0);
		String[] b = selectedIndex.get(1);
		String[] add = Turns.turns(a, b);

		for (int i = 2; i < userSize; i++) {
			b = selectedIndex.get(i);
			add = Turns.turns(add, b);
		}
		ArrayList<User> optimalUsers = new ArrayList<User>();

		for (int count = 0; count < add.length; count++) {
			if (count % (int) (add.length * 0.1) == 0) {
				System.out.println("Process" + count + " " + add.length);
			}
			String selectIndex = add[count];
			for (int i = 0; i < users.size(); i++) {
				users.get(i).currentIndex = 0;
				users.get(i).assignRoute(g, Integer.parseInt(selectIndex.split(",")[i]));
				UserUpdate.greedyUserQueue.add(users.get(i));
			}
			UserUpdate.update(g);
			if (Shower.time(users) < minCost) {
				minCost = Shower.time(users);
				optimalUsers = SBP.copyUser(users);
			}
		}

	}

}
