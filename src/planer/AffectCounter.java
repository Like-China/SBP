package planer;

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
    			if(currentUserArrTime < prior.getArriveTime()+ prior.getProcessTime())
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
