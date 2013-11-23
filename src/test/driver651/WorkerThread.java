package driver651;

import java.io.IOException;

import org.apache.lucene.search.FieldCacheImpl;

public class WorkerThread extends Thread{

	public FieldCacheImpl cache;
	public WorkerThread(FieldCacheImpl arg)
	{
		cache=arg;
	}
	public void run()
	{
		int[] ints=null;
		for(int j=0;j<Driver651.NUM_FIELDS; j++){
			try {
				ints = cache.getInts(Driver651.reader, "theField"+j);// purely test the speed of the cache.get().
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		    
//		    boolean assertion = true;
//		    assertion =  assertion && (ints.length == Driver651.NUM_DOCS);
//		    
//		    for (int i = 0; i < ints.length; i++) {
//		    	assertion = assertion && (ints[i] == (Integer.MAX_VALUE - i));
//		    }
//		    
//		    System.out.println("assertion is: " + assertion);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
