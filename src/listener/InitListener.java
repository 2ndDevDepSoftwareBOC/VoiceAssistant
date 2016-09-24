package listener;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import workFlow.*;

public class InitListener implements ServletContextListener {
	// 实现其中的销毁函数

	private static void addDoc(IndexWriter w, String functionName, String url) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("functionName", functionName, Field.Store.YES));// no
																				// norms
																				// 取消权重功能

		doc.add(new StringField("url", url, Field.Store.YES));
		w.addDocument(doc);
	}

	private Directory indexAll() throws Exception {

		Directory index = new RAMDirectory();
		try {

			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter w = new IndexWriter(index, config);

			addDoc(w, "账户概览", "checkBalance");
			addDoc(w, "交易明细", "transactionDetail");

			addDoc(w, "中行内转账汇款", "innerbankTransfer");
			addDoc(w, "外币跨境汇款", "crossborderTransfer");
			addDoc(w, "跨行转账汇款", "interbankTransfer");

			addDoc(w, "信用卡还款", "paybackBill");
			addDoc(w, "信用卡账单查询", "queryBill");

			w.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return index;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {

			ServletContext sct = sce.getServletContext();

			// 创建导航索引
			Directory index = indexAll();
			sct.setAttribute("reader", DirectoryReader.open(index));

			// ------------------初始化交互引擎----------------------

			String function0Id = "100100";
			String function0Name = "转账";
			Function function0 = new Function(function0Id, function0Name, null, null);
			function0.setParser(new BankTransferNLPParser());
			
			String function1Id = "100101";
			String function1Name = "行内转账";
			String function1UrlName = "innerbankTransfer";
			Function function1 = new Function(function1Id, function1Name, function1UrlName, null, null);
			function1.setParser(new InnerBankTransferNLPParser());

			String function2Id = "100102";
			String function2Name = "跨行转账";
			String function2UrlName = "interbankTransfer";
			Function function2 = new Function(function2Id, function2Name, function2UrlName, null, null);
			function2.setParser(new InterBankTransferNLPParser());

			HashMap<String, Function> functionMap = new HashMap<String, Function>();
			functionMap.put(function0Name, function0);
			functionMap.put(function1Name, function1);
			functionMap.put(function2Name, function2);

			sct.setAttribute("functionMap", functionMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}