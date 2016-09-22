package servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tts.TxtReader;
@WebServlet("/voiceAssistant/question")
/**
 * 专门负责文本-->音频翻译
 * 例如：“先生您需要办理什么业务”，“100”
 * @author Administrator
 *
 */
public class QuestionServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuestionServlet() {
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
		String contentPath = getServletContext().getRealPath("/");  
		String askPath = contentPath + "/audio";

		String question =  request.getParameter("question");

		TxtReader ask = new TxtReader();
		ask.excute(question,askPath);

		response.getWriter().print("{audio:\""+askPath+"\"}");

	}

}
