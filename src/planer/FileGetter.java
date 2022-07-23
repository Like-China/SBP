package planer;

import java.io.*;

/**
 * Get the lower-bound of travel time from a vertex to another
 */
public class FileGetter {
	
	/**
	 * Get the lower-bound of travel time from a vertex to another
	 * @param filepath  the file path that stores the lower-bound of travel time from a vertex to another
	 * @param nodeSize  the number of vertexs
	 * @return the lower-bound of travel time from a vertex to another
	 */
	public static float[] getDistMap(String filepath, int nodeSize)
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
