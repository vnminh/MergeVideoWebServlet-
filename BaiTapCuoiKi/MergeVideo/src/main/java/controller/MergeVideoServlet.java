package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BO.MergeVideoMangerBO;
import model.BO.UpVideoToHDManagerBO;
import model.bean.Account;

import java.io.IOException;

/**
 * Servlet implementation class MergeVideoServlet
 */
@WebServlet("/MergeVideoServlet")
public class MergeVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MergeVideoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = ((Account)request.getSession().getAttribute("account")).getUserID();
		int pID = Integer.parseInt(request.getParameter("pID"));
		double mergeStatus = (MergeVideoMangerBO.getInstance().progressManagerMap.get(pID)==null)?0:MergeVideoMangerBO.getInstance().progressManagerMap.get(pID).getStatus();
		double upHDStatus = (UpVideoToHDManagerBO.getInstance().progressManagerMap.get(pID)==null)?0:UpVideoToHDManagerBO.getInstance().progressManagerMap.get(pID).getStatus();
		response.getWriter().write(String.format("{\"progressMerge\": %.2f, \"progressHD\": %.2f}", mergeStatus,upHDStatus));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = ((Account)request.getSession().getAttribute("account")).getUserID();
		int pID = Integer.parseInt(request.getParameter("pID"));
		MergeVideoMangerBO.getInstance().postTask(userID, pID);
		response.sendRedirect("processPage.jsp");
	}

}
