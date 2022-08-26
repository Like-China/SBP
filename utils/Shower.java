package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * A set of methods that shows the experimental results
 */
public class Shower {
	
	public static void showInfo(ArrayList<Node> nodes)
	{
		for(int i=0;i<nodes.size();i++)
		{
			Node cuNode = nodes.get(i);
			System.out.println(cuNode.nodeId +" "+cuNode.sDist+" "+cuNode.dDist+ " Pre:"+cuNode.pre+" curTime: "+cuNode.currentTime);
		}
		System.out.println();
	}
	
	public static void show(ArrayList<User> greedyUsers) {
		System.out.println();
		for (User u : greedyUsers) {
			System.out.println(u);
			System.out.println("-----------------");
		}
	}
	
	public static void show(HashMap<String, PriorityQueue<UserNodeInfo>> hashmap)
	{
		System.out.println();
		for(Map.Entry<String, PriorityQueue<UserNodeInfo>> entry:hashmap.entrySet())
		{
			for(UserNodeInfo info:entry.getValue())
			{
				System.out.println(info);
			}
		}
	}
	
	public static void show(Queue<User> greedyUsers) {
		System.out.println();
		for (User u : greedyUsers) {
			u.showInfo();
			System.out.println(u.currentTime);
			System.out.println("-----------------");
		}
	}
	
	public static void showMap(HashMap<String, ArrayList<UserNodeInfo>> hashmap)
	{
		System.out.println();
		for(Map.Entry<String, ArrayList<UserNodeInfo>> entry:hashmap.entrySet())
		{
			for(UserNodeInfo info:entry.getValue())
			{
				System.out.println(info);
			}
		}
	}
	
	public static float time(ArrayList<User> users) {
		float totalTime = 0;
		for (int i = 0; i < users.size(); i++) {
			totalTime += users.get(i).totalTimeCost();
		}
		// transfer into miniute
		return totalTime/60f;
	}
	
}
