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
				// UserNodeInfo ���յ���ʱ��+ͨ��ʱ��֮�����򣬵����ж�����ǰ������UserNodeInfo����������֮���BatchRefiningProcesssing.update��poll�����û�ʱ���н���
				// ��ǰ�ִ�poll����UserNodeInfo��getArriveTime()+ getProcessTime()��С��BatchRefiningProcesssing.update��poll�����û���ǰʱ��currentUserArrTime��û�н���
				// ��һ��poll����UserNodeInfo��֮��poll�����û���ǰʱ��currentUserArrTime���޽�������ΪcurrentUserArrTime�ǵ�����
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
