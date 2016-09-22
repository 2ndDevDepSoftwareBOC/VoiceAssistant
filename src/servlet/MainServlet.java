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

import net.sf.json.JSONObject;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

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
		// HttpSession session = request.getSession();

		String functionId = (String) request.getAttribute("functionId");
		String functionName = (String) request.getAttribute("functionName");
		String originalStr = (String) request.getAttribute("originalStr");
		String nlpJsonStr = (String) request.getAttribute("nlpJsonStr");

		HashMap<String, Function> functionMap = (HashMap<String, Function>) sct.getAttribute("functionMap");
		Function function = (Function) functionMap.get(functionName);
		NLPParser parser = function.getParser();

		HashMap<String, String> elementMap = parser.execute(originalStr, functionName, nlpJsonStr);
		// 或者让前端判断是否跳转
		// 跳转
		String urlName = "";
		try {
			urlName = redirect(originalStr, (IndexReader) sct.getAttribute("reader"));// 跳转url
		} catch (ParseException e) {
			e.printStackTrace();
		}

		elementMap.put("urlName", urlName);

		response.getWriter().print(JSONObject.fromObject(elementMap));

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
}
