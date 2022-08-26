package evaluate;

import planer.SBP;
import planer.Settings;

import java.util.ArrayList;
import utils.Shower;
import utils.User;
import utils.TripGetter;

public class Evaluate {
	public SBP s = new SBP();
	public int expNum = Settings.expNum;
	// all simulated trip queries
	TripGetter tripGetter = new TripGetter("data/randomUser.txt", Settings.queryDensity, 0);
    public static  void main(String[] args) {
		Evaluate e = new Evaluate();
		// vary the user count for Ind algorithm, set self-aware = false
		e.varyUserSizeForInd();
		// vary the trip count (user count) for SBP algorithm
		e.varyUserSize();
		// vary the refining interval
		e.varyRefineInterval();
		// vary the refining parameter epsilon
		e.varyThreshold();
		e.varyQueryDensity();
	}
    
    public void varyUserSize() {
		System.out.println("\n********Vary Trip Size (SBP)*****************");
		ArrayList<Long> runtime = new ArrayList<>();
		ArrayList<Float> travelTime = new ArrayList<>();
		int[] userSizes = (Settings.roadName == "TGC") ? Settings.userSizesTG : Settings.userSizesNY;
		for (int i = 0; i < userSizes.length; i++) {
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for (int expCount = 0; expCount < expNum; expCount++) {
				// get users with planned routes
				allUsers = s.sbp(userSizes[i], Settings.queryDensity, Settings.interval, Settings.e, tripGetter);
			}
			runtime.add(s.sumTime / expNum );
			travelTime.add(Shower.time(allUsers));
		}
		System.out.println("runtime: "+ runtime);
		System.out.println("travelTime: "+ travelTime);
	}

	public void varyUserSizeForInd() {
		System.out.println("\n********Vary Trip Size (IND)*****************");
		ArrayList<Long> runtime = new ArrayList<>();
		ArrayList<Float> travelTime = new ArrayList<>();
		int[] userSizes = (Settings.roadName == "TGC") ? Settings.userSizesTG : Settings.userSizesNY;
		for (int i = 0; i < userSizes.length; i++) {
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for (int expCount = 0; expCount < expNum; expCount++) {
				allUsers = s.ind(userSizes[i], Settings.queryDensity, Settings.interval, Settings.e, tripGetter);
			}
			runtime.add(s.sumTime / expNum );
			travelTime.add(Shower.time(allUsers));
		}
		System.out.println("runtime: "+ runtime);
		System.out.println("travelTime: "+ travelTime);
	}

    public void varyRefineInterval() {
		System.out.println("\n********Vary Interval*****************");
		ArrayList<Long> runtime = new ArrayList<>();
		ArrayList<Float> travelTime = new ArrayList<>();
		for (int i = 0; i < Settings.intervals.length; i++) {
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for (int expCount = 0; expCount < expNum; expCount++) {
				allUsers = s.sbp(Settings.userSize, Settings.queryDensity, Settings.intervals[i], Settings.e, tripGetter);
			}
			runtime.add(s.sumTime / expNum );
			travelTime.add(Shower.time(allUsers));
		}
		System.out.println("runtime: "+ runtime);
		System.out.println("travelTime: "+ travelTime);
	}

	public void varyQueryDensity() {
		System.out.println("\n********Vary Density*****************");
		ArrayList<Long> runtime = new ArrayList<>();
		ArrayList<Float> travelTime = new ArrayList<>();
		for (int i = 0; i < Settings.queryDensitys.length; i++) {
			tripGetter = new TripGetter("data/randomUser.txt", Settings.queryDensitys[i], 0);
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for (int expCount = 0; expCount < expNum; expCount++) {
				allUsers = s.sbp(Settings.userSize, Settings.queryDensitys[i], Settings.interval, Settings.e, tripGetter);
			}
			runtime.add(s.sumTime / expNum );
			travelTime.add(Shower.time(allUsers));
		}
		System.out.println("runtime: "+ runtime);
		System.out.println("travelTime: "+ travelTime);
	}

	public void varyThreshold() {
		System.out.println("********Vary Threshold*****************");
		ArrayList<Long> runtime = new ArrayList<>();
		ArrayList<Float> travelTime = new ArrayList<>();
		for (int i = 0; i < Settings.es.length; i++) {
			// more than 20 independent trials, get the averaged result
			ArrayList<User> allUsers = new ArrayList<User>();
			for (int expCount = 0; expCount < expNum; expCount++) {
				allUsers = s.sbp(Settings.userSize, Settings.queryDensity, Settings.interval, Settings.es[i], tripGetter);
			}
			runtime.add(s.sumTime / expNum );
			travelTime.add(Shower.time(allUsers));
		}
		System.out.println("runtime: "+ runtime);
		System.out.println("travelTime: "+ travelTime);
	}
	
}
