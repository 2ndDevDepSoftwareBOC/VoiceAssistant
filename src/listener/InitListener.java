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
			HashMap<String, Function> functionMap = new HashMap<String, Function>();

			String function0Id = "100100";
			String function0Name = "转账";
			Function function0 = new Function(function0Id, function0Name, null, null);
			function0.setParser(new BankTransferNLPParser());
			functionMap.put(function0Name, function0);

			String function1Id = "100101";
			String function1Name = "中行内转账汇款";
			String function1UrlName = "innerbankTransfer";
			Function function1 = new Function(function1Id, function1Name, function1UrlName, null, null);
			function1.setParser(new InnerBankTransferNLPParser());
			functionMap.put(function1Name, function1);

			String function2Id = "100102";
			String function2Name = "跨行转账汇款";
			String function2UrlName = "interbankTransfer";
			Function function2 = new Function(function2Id, function2Name, function2UrlName, null, null);
			function2.setParser(new InterBankTransferNLPParser());
			functionMap.put(function2Name, function2);
			
			String function5Id = "100103";
			String function5Name = "外币跨境汇款";
			String function5UrlName = "crossborderTransfer";
			Function function5 = new Function(function5Id, function5Name, function5UrlName, null, null);
			function5.setParser(new CrossborderTransferNLPParser());
			functionMap.put(function5Name, function5);
			
			String function3Id = "101100";
			String function3Name = "账户概览";
			String function3UrlName = "checkBalance";
			Function function3 = new Function(function3Id, function3Name, function3UrlName, null, null);
			function3.setParser(new CheckBalanceNLPParser());
			functionMap.put(function3Name, function3);
			
			String function4Id = "101101";
			String function4Name = "交易明细";
			String function4UrlName = "transactionDetail";
			Function function4 = new Function(function4Id, function4Name, function4UrlName, null, null);
			function4.setParser(new TransactionDetailNLPParser());
			functionMap.put(function4Name, function4);
			
			String function6Id = "102100";
			String function6Name = "信用卡账单查询";
			String function6UrlName = "queryBill";
			Function function6 = new Function(function6Id, function6Name, function6UrlName, null, null);
			function6.setParser(new QueryBillNLPParser());
			functionMap.put(function6Name, function6);
			
			String function7Id = "102101";
			String function7Name = "信用卡还款";
			String function7UrlName = "paybackBill";
			Function function7 = new Function(function7Id, function7Name, function7UrlName, null, null);
			function7.setParser(new PaybackBillNLPParser());
			functionMap.put(function7Name, function7);

			sct.setAttribute("functionMap", functionMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}