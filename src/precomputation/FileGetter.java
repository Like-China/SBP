package precomputation;

import java.io.*;

public class FileGetter {
	

	public static float[] getDistMap(String filepath,int nodeSize)
	{
		float[] dists = new  float[nodeSize];
		try {
			File file = new File(filepath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String lineString = null;
			int sonId = 0;
			while((lineString = reader.readLine()) != null)
			{
				float dist = Float.parseFloat(lineString);
				dists[sonId++] = dist;
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dists;
	}

		
}
