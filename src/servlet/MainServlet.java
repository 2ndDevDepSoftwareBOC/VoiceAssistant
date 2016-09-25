package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import net.sf.json.JSONObject;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.json.JSONObject;

import workFlow.Function;
import workFlow.NLPParser;

@WebServlet("/voiceAssistant/redirect")
public class MainServlet extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		ServletContext sct = getServletConfig().getServletContext();
		IndexReader indexReader = (IndexReader) sct.getAttribute("reader");
		HashMap<String, Function> functionMap = (HashMap<String, Function>) sct.getAttribute("functionMap");
		// HttpSession session = request.getSession();

		String intentWord = (String) request.getAttribute("intentWord");
		String originalStr = (String) request.getAttribute("originalStr");
		String nlpJsonStr = (String) request.getAttribute("nlpJsonStr");

		String functionName = "";
		try {
			if (originalStr.contains("中行")) {
				if (originalStr.contains("非")) {
					functionName = getFunctionNameFromLucene("跨行" + intentWord, indexReader);
				} else {
					functionName = getFunctionNameFromLucene("行内" + intentWord, indexReader);
				}
			} else {
				functionName = getFunctionNameFromLucene(originalStr + intentWord, indexReader);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Function function = (Function) functionMap.get(functionName);

		if (function == null) {
			response.getWriter().print("{\"error\":\"true\"}");
		} else {
			NLPParser parser = function.getParser();
			HashMap elementMap = parser.execute(nlpJsonStr);

			elementMap.put("functionId", function.getId());
			elementMap.put("functionName", function.getName());
			if (function.getUrlName() != null) {
				elementMap.put("urlName", function.getUrlName());
			}

			// 或者让前端判断是否跳转
			// 跳转

			JSONObject jsonObject = new JSONObject(elementMap);
			response.setCharacterEncoding("utf-8");
			System.out.println(jsonObject.toString());
			response.getWriter().print(jsonObject.toString());
		}

	}

	private String redirect(String functionName, IndexReader reader)
			throws IOException, ParseException, ParseException, ParseException {

		IndexSearcher searcher = new IndexSearcher(reader); // 3. search
		int hitsPerPage = 1;
		Query q = new QueryParser("functionName", new StandardAnalyzer()).parse(functionName);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		String url = "";

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);//
			url = d.get("url");
		}
		System.out.println("url:" + url);

		return url;

	}

	private String getFunctionNameFromLucene(String originalStr, IndexReader reader)
			throws IOException, ParseException, ParseException, ParseException {

		IndexSearcher searcher = new IndexSearcher(reader); // 3. search
		int hitsPerPage = 1;
		Query q = new QueryParser("functionName", new StandardAnalyzer()).parse(originalStr);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		String functionName = "";

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);//
			functionName = d.get("funcitonName");
		}
		// System.out.println("url:" + functionName);

		return functionName;

	}
}
