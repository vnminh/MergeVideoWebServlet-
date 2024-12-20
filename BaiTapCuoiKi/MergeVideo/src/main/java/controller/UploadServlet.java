package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.BO.MergeVideoMangerBO;
import model.BO.UploadVideoManagerBO;
import model.bean.Account;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.plaf.basic.BasicTreeUI.TreePageAction;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet/*")
@MultipartConfig(location = "D:\\MinhDUT\\KY 5, NAM 24-25\\Lap trinh mang\\Source\\temp",
				 fileSizeThreshold=1024*1024*1000, 	// 500 MB dung luong buffer cho phep truoc khi luu
				 maxFileSize=1024*1024*1024)         // 1000MB dung luong 1 file         
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String rootFolder = "D:\\MinhDUT\\KY 5, NAM 24-25\\Lap trinh mang\\Source";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String userID = ((Account)request.getSession().getAttribute("account")).getUserID();
		int pID = (request.getSession().getAttribute("pID")==null)?-1:(int)request.getSession().getAttribute("pID");
		System.out.println(userID+"in get");
		String url = request.getRequestURL().toString();
		if (url.contains("/Finish")) {
			UploadVideoManagerBO.getInstance().progressMap.remove(userID);
			return;
		}
		response.setContentType("application/json");
		Integer[] curProgress = UploadVideoManagerBO.getInstance().getStatus(userID);
//		System.out.println(curProgress[0]+" "+curProgress[1]);
		if (curProgress!=null) {
			response.getWriter().write(String.format("{\"numFileUploaded\": %d, \"totalFileUploaded\": %d, \"processID\": %d}", curProgress[0],curProgress[1],pID));
		}
		else {
			response.getWriter().write(String.format("{\"numFileUploaded\": 0, \"totalFileUploaded\": 0, \"processID\": %d}",pID));
		}
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getSession().setMaxInactiveInterval(60*60);
		String userID = ((Account)request.getSession().getAttribute("account")).getUserID();
		System.out.println(userID+"in post");
		int totalFileUpload = request.getParts().size();
		UploadVideoManagerBO.getInstance().putStatus(userID, new Integer[] {0,totalFileUpload});
		System.out.println(totalFileUpload);
		int pID = MergeVideoMangerBO.getInstance().getNextID();
		request.getSession().setAttribute("pID", pID);
		String upFolder = rootFolder+File.separator+userID+File.separator+pID+File.separator+"upload";
		File upF = new File(upFolder);
		if (!upF.exists()) {
			upF.mkdirs();
		}
		PrintWriter fileListPrinter = new PrintWriter(upFolder+File.separator+"file_list.txt");
		
		for (Part part: request.getParts()) {
			String filePath = upFolder+File.separator+getFileName(part);
			fileListPrinter.println(String.format("file '%s'", filePath));
			UploadVideoManagerBO.getInstance().postTask(userID, part, filePath, request, response);
		}
		fileListPrinter.close();
	}
	
	private static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		UploadVideoManagerBO.getInstance().destroy();
	}
}
