package servlet;


import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/voiceAssistant/answner")
public class AnswnerServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnswnerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8"); 
		ServletContext sct=getServletConfig().getServletContext();   

		String answner = (String) request.getAttribute("answner");
		//保存输入
		response.getWriter().print("{text:\""+answner+"\"}");

	}



}
