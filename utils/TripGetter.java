package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Get trip queries
 */
public class TripGetter {
	// The query arrival rate
	public int queryDensity = 50;
	// The departure time of the first trip query
	public float minDepartureTime = 0f;

	// The file that stores the simulated trip queries
	public String userRequestFilepath = "data/randomUser.txt";

	// The starts,ends and departure time of trips
	public ArrayList<Integer> starts = new ArrayList<Integer>();
	public ArrayList<Integer> ends = new ArrayList<Integer>();
	public ArrayList<Float> startTimes = new ArrayList<Float>();

	public TripGetter(String userRequestFilepath, int queryDensity, float minDepartureTime) {
		// TODO Auto-generated constructor stub
		this.userRequestFilepath = userRequestFilepath;
		this.queryDensity = queryDensity;
		this.minDepartureTime = minDepartureTime;
		getInfo();
	}

	/**
	 * Get simulated trip queries (String format)
	 * 
	 * @return String[i] is the trip query i
	 */
	public ArrayList<String> readTrips() {
		ArrayList<String> userRequest = new ArrayList<String>();
		try {
			File file = new File(userRequestFilepath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			while ((lineString = reader.readLine()) != null) {
				userRequest.add(lineString);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Read Error");
			e.printStackTrace();
		}
		return userRequest;
	}

	/**
	 * Get the starts,ends and departure time of trips
	 * Update time information based on the queryDensity
	 */
	public void getInfo() {
		ArrayList<String> userRequest = readTrips();
		for (int i = 0; i < userRequest.size(); i++) {
			starts.add(Integer.parseInt(userRequest.get(i).split(" ")[2]));
			ends.add(Integer.parseInt(userRequest.get(i).split(" ")[3]));
		}
		// update the departure time
		try {
			File file = new File(userRequestFilepath);
			Writer out = new FileWriter(file);
			for (int i = 0; i < starts.size(); i++) {
				if (i % queryDensity == 0) {
					minDepartureTime += 1;
				}
				String data = i + " " + minDepartureTime + " " + starts.get(i) + " " + ends.get(i) + "\n";
				out.write(data);
				startTimes.add(minDepartureTime);
			}
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TripGetter u = new TripGetter("data/randomUser.txt", 10, 10);
	}

}
