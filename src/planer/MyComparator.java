package planer;

import java.util.Comparator;

public class MyComparator {
	// User-User comparator
	static Comparator<User> cUser=new Comparator<User>()
	{
      public int compare(User o1, User o2) 
      {
          if(o1.currentTime>o2.currentTime)
          {
        	  return 1;
          }else if(o1.currentTime<o2.currentTime)
          {
			return -1;
		  }
          else {
			return 0;
         }
	  }
	};
	
	// UserNodeInfo-UserNodeInfo Comparator
	static Comparator<UserNodeInfo> nodeInfoComparator = new Comparator<UserNodeInfo>() {
		public int compare(UserNodeInfo u1,UserNodeInfo u2)
		{
			if(u1.getArriveTime()+u1.getProcessTime()>u2.getArriveTime()+u2.getProcessTime())
			{
				return 1;
			}else if(u1.getArriveTime()+u1.getProcessTime()<u2.getArriveTime()+u2.getProcessTime())
			{
				return -1;
			}else {
				return 0;
			}
		}
	};
	
	
	//Node-Node comparator
	static Comparator<Node> openComparator = new Comparator<Node>() {
		public int compare(Node o1, Node o2) {
			if(o1.sDist+o1.dDist >o2.sDist+o2.dDist)
			{
				return 1;
			}else if(o1.sDist+o1.dDist<o2.sDist+o2.dDist){
				return -1;
			}else {
				return 0;
			}
		};
	};
}
