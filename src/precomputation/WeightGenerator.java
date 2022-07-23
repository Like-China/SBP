package precomputation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

public class WeightGenerator {

	public static void swapWeight(String filepath, String newFilePath)
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
				int weightRandom = 1+randomer.nextInt(10);
				int capacityRandom = 20+randomer.nextInt(80);
				String data= edgeArrayList.get(i)+" "+weightRandom+" "+capacityRandom+ "\n";
				out.write(data);
			}
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
}
