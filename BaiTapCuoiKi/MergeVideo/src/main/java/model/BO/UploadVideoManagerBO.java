package model.BO;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.plaf.ProgressBarUI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class UploadVideoManagerBO {
	private static UploadVideoManagerBO instance = null;
	private ExecutorService executorService=Executors.newFixedThreadPool(2);
    public Map<String, Integer[]> progressMap = new ConcurrentHashMap<String, Integer[]>();
    private static Object key = new Object();
    private UploadVideoManagerBO() {
		// TODO Auto-generated constructor stub
    	
    }
    
    public static UploadVideoManagerBO getInstance() {
    	if (instance == null) {
    		instance = new UploadVideoManagerBO();
    	}
    	return instance;
    }
	
	
    public void putStatus(String userID, Integer[] status) {
    	progressMap.put(userID, status);
    }
    
    public Integer[] getStatus(String userID) {
		return progressMap.get(userID);
	}
	public void postTask(String userID, Part part, String filePath, HttpServletRequest request, HttpServletResponse response) {
		executorService.submit(()->{
			processFileUpload(userID, part, filePath, request, response);
		});
	}
    
	public void destroy() {
		if (executorService != null && !executorService.isShutdown()) {
			System.out.println("on destroy");
            executorService.shutdown();
        }
	}
	
	private void processFileUpload(String userID, Part part, String filePath, HttpServletRequest request, HttpServletResponse response) {
        try {
        	System.out.println("File " + filePath + " is going to be uploaded.");
        	part.write(filePath);
//        	InputStream is = part.getInputStream();
//            FileOutputStream fos = new FileOutputStream(filePath);
//            int b;
//            while ((b = is.read())!=-1) {
//            	fos.write(b);
//            }
//            fos.close();
//            is.close();
            System.out.println("File " + filePath + " uploaded successfully.");
            Integer[] curProgress;
            synchronized (key) {
            	curProgress = progressMap.get(userID);
            	curProgress[0]++;
            	System.out.println(curProgress[0]+"/"+curProgress[1]);
                progressMap.put(userID, curProgress); 
			}
        	
			
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}
