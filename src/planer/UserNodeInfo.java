package planer;

public class UserNodeInfo implements Comparable<UserNodeInfo>{
	private float arriveTime;
	private float processTime;
	
	
	public UserNodeInfo(float arriveTime,float processTime)
	{
		this.arriveTime = arriveTime;
		this.processTime = processTime;
	}
	
	
	public float getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(float arriveTime) {
		this.arriveTime = arriveTime;
	}



	public float getProcessTime() {
		return processTime;
	}



	public void setProcessTime(float processTime) {
		this.processTime = processTime;
	}


	public void showInfo()
	{
		System.out.println( getArriveTime()+"+"+getProcessTime()+" nextTime: "+(arriveTime+processTime));
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return (arriveTime+"+"+processTime);
	}
	
	@Override
	public int compareTo(UserNodeInfo o) {
		// TODO Auto-generated method stub
		return this.arriveTime>o.arriveTime?1:-1;
	}
}
