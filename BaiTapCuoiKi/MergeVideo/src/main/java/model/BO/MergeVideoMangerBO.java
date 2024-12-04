package model.BO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.bean.MergeVideoHandler;

public class MergeVideoMangerBO {
	public HashMap<Integer, MergeVideoHandler> progressManagerMap = new HashMap<Integer, MergeVideoHandler>();
	private ExecutorService executorService=Executors.newFixedThreadPool(10);
	private int nextID = 1;
	private static MergeVideoMangerBO instance;
	
	synchronized public int getNextID() {
		return nextID++;
	}
	
	public static MergeVideoMangerBO getInstance() {
		if (instance==null) {
			instance = new MergeVideoMangerBO();
		}
		return instance;
	}
	
	public void postTask(String userID, int pID) {
		MergeVideoHandler handler = new MergeVideoHandler(userID, pID);
		progressManagerMap.put(pID, handler);
		executorService.submit(handler);
	}
	
	
}
