package precomputation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

/*
 * Set weight (minmial travel time) of each road segment on the road network
 */
public class WeightGenerator {

	// raw road network txt
	public String filepath;
	// updated road network txt with minimal travel time
	public String newFilePath;

	public WeightGenerator(String filepath, String newFilePath)
	{
		this.filepath = filepath;
		this.newFilePath = newFilePath;
	}

	// set and store weight (the minimal travel time) of each road segment
	public  void setWeight()
	{
		ArrayList<String> edgeArrayList = new ArrayList<String>();
		try {
			File file = new File(filepath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			while((lineString = reader.readLine()) != null)
			{
				String id = lineString.split(" ")[0];
				String startIdString = lineString.split(" ")[1];
				String endIdString = lineString.split(" ")[2];
				edgeArrayList.add(id+" "+startIdString+" "+endIdString);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {
			File file =new File(newFilePath);
			Writer out =new FileWriter(file);
			Random randomer = new Random(1);
			for(int i= 0;i<edgeArrayList.size();i++)
			{
				// seconds (minimal travle time)
				int weightRandom = 50+randomer.nextInt(51);
				// capacity of each road segment
				int capacityRandom = 20+randomer.nextInt(81);
				String data= edgeArrayList.get(i)+" "+weightRandom+" "+capacityRandom+ "\n";
				out.write(data);
			}
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}


	
	public static void main(String[] args) {
		String rawPath = "data/NYC.txt";
		String newPath = "data/NYC.txt";
		new  WeightGenerator(rawPath,newPath).setWeight();
	}
	
}
