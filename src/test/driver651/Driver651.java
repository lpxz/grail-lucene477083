package driver651;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.FieldCacheImpl;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.English;

import java.io.IOException;

import junit.framework.TestCase;

public class Driver651  {
  protected static IndexReader reader;
  public static final int NUM_DOCS = 100;

  public static final int NUM_FIELDS = 1000;
  Driver651(String s) {
  }



  public static void main(String[] args) throws Exception {
	  //@FieldCacheImpl.java
	  int threadNo = Integer.parseInt(args[0]);
	  // 138 vs 179  					507 (original)  all-threads-one-cache
	  // 1295		1779					one-thread-one-cache
	  RAMDirectory directory = new RAMDirectory();
	    IndexWriter writer= new IndexWriter(directory, new WhitespaceAnalyzer(), true);
	    int theInt = Integer.MAX_VALUE;
	    for(int j = 0; j< NUM_FIELDS; j++)
	    {
	    	for (int i = 0; i < NUM_DOCS; i++){
	  	      Document doc = new Document();
	  	      doc.add(new Field("theField"+j, String.valueOf(theInt--), Field.Store.NO, Field.Index.UN_TOKENIZED));// notice the field "theFieldj"
	  	      writer.addDocument(doc);
	  	    }
	    }
	  
	  writer.close();
	  reader = IndexReader.open(directory);
	  
	 
	  FieldCacheImpl cache = new FieldCacheImpl();// move it out of the loop, then you get the all-threads-one-cache scenario!
	  
	  WorkerThread[] workers = new  WorkerThread[threadNo];
	  for(int i=0;i<threadNo; i++)
	  {

		  workers[i] = new  WorkerThread(cache);
	  }
	  long start = System.currentTimeMillis();
	  for(int i=0;i<threadNo; i++)
	  {
		  workers[i].start();
	  }
	  
	  for(int i=0;i<threadNo; i++)
	  {
		  workers[i].join();
	  }
	  
	  long end = System.currentTimeMillis();
	  System.out.println("duration: " + (end-start));
  }
}