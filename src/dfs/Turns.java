package dfs;


/**
 * Generate all the route combinations
 */
public class Turns {
    
    /**
     * Get the route combinations of each two trips
     * @param array1 routes of trip 1
     * @param array2 routes of trip 2
     * @return the route combinations of each two trips
     */
    public static String[] doubleTurns(String [] array1,String[] array2){
        String [] target=new String[array1.length*array2.length];
        for (int i = 0,a1=0,a2=0; i <array1.length*array2.length; i++) {
            target[i]=array1[a1]+","+array2[a2];
            a2++;
            if(a2==array2.length){
                a2=0;
                a1++;
            }
        }
        return target;
    }
    /**
     * Get the route combinations of all trips
     * @param arrays routes of all trips
     * @return the route combinations of all trips
     */
    public static String[] turns(String[] ...arrays){
        if(arrays.length==1){
            return arrays[0];
        }
        if(arrays.length==0){
            return null;
        }
        int count=0;
        for (int i = 0; i < arrays.length; i++) {
            count*=arrays[i].length;
        }
        String target[]=new String[count];
        for (int i = 0; i < arrays.length; i++) {
            if(i==0){
                target=doubleTurns(arrays[0],arrays[1]);
                i++;
            }else{
                target=doubleTurns(target,arrays[i]);
            }
        }
        return target;
    }
}