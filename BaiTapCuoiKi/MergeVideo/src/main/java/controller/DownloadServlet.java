package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.Account;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String rootFolder = "D:\\MinhDUT\\KY 5, NAM 24-25\\Lap trinh mang\\Source";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = ((Account)request.getSession().getAttribute("account")).getUserID();
		int pID = Integer.parseInt(request.getParameter("processID"));
		String outFilePath = rootFolder+File.separator+userID+File.separator+pID+File.separator+"output"+File.separator+"output.mp4";
		File downloadFile = new File(outFilePath);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadFile));
		ServletContext sctx = getServletContext();
		String mimeType = sctx.getMimeType(outFilePath);
		
		response.setContentType(mimeType != null? mimeType:"application/octet-stream");
		response.setContentLength((int) downloadFile.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"output.mp4\"");
		
		ServletOutputStream sos = response.getOutputStream();
		byte[] buffer = new byte[65536];
		while ((bis.read(buffer))!=-1) {
			sos.write(buffer);
		}
		sos.flush();
		sos.close();
		bis.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
