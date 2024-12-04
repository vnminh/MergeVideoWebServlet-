package model.BO;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.bean.UpVideoToHDHandler;

public class UpVideoToHDManagerBO {
	public HashMap<Integer, UpVideoToHDHandler> progressManagerMap = new HashMap<Integer, UpVideoToHDHandler>();
	private ExecutorService executorService=Executors.newFixedThreadPool(10);
	private static UpVideoToHDManagerBO instance;
	
	public static UpVideoToHDManagerBO getInstance() {
		if (instance==null) {
			instance = new UpVideoToHDManagerBO();
		}
		return instance;
	}
	
	public void postTask(String userID, int pID) {
		UpVideoToHDHandler handler = new UpVideoToHDHandler(userID, pID);
		progressManagerMap.put(pID, handler);
		executorService.submit(handler);
	}
}
