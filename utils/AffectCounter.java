package utils;

import java.util.PriorityQueue;

/**
 * calculate the traffic flow caused by planned routes
 */
public class AffectCounter {
	
	/**
	 * @param inNodesInfos the edge labels of planned routes
	 * @param currentUserArrTime the current time
	 * @return the traffic flow caused by planned routes at time "currentUserArrTime"
	 */
	public static int calcAffectCount(PriorityQueue<UserNodeInfo> inNodesInfos, float currentUserArrTime)
	{
    	int affectCount = 0;
    	if(inNodesInfos != null)
    	{
    		while(!inNodesInfos.isEmpty())
        	{ 
    			UserNodeInfo prior = inNodesInfos.poll();
				// UserNodeInfo 按照到达时间+通行时间之和排序，到该判断条件前的所有UserNodeInfo不可能再与之后的BatchRefiningProcesssing.update中poll出的用户时间有交集
				// 当前轮次poll出的UserNodeInfo的getArriveTime()+ getProcessTime()均小于BatchRefiningProcesssing.update中poll出的用户当前时间currentUserArrTime，没有交集
				// 上一轮poll出的UserNodeInfo与之后poll出的用户当前时间currentUserArrTime更无交集，因为currentUserArrTime是递增的
    			if(currentUserArrTime <= prior.getArriveTime()+ prior.getProcessTime())
    			{
    				inNodesInfos.add(prior);
    				break;
    			}
        	}
    		affectCount = inNodesInfos.size();
    	}
    	return affectCount;
	}
	
}
