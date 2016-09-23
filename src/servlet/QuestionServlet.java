package servlet;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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

		String storeFile = "audio/question.wav";
		request.setCharacterEncoding("UTF-8"); 
		String contentPath = getServletContext().getRealPath("/");  
		String askPath = contentPath + storeFile;
		
//		String storePath = "/Library/WebServer/Documents/audio/";
//		String retPath = "voiceAssistant/audio/";

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);   
        upload.setHeaderEncoding("UTF-8");
		 List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	        Map param = new HashMap();   
	        for(Object object:items){  
	            FileItem fileItem = (FileItem) object;   
	            if (fileItem.isFormField()) {   
	                param.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的   
	            }  
	        }
		String question =  request.getParameter("question");
		String teString = (String) param.get("question");
		TxtReader ask = new TxtReader();
		ask.excute(question,askPath);

		response.getWriter().print("{audio:\""+storeFile+"\"}");

	}

}
