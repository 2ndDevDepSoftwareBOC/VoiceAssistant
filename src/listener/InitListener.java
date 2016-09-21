package listener;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import workFlow.CrossBankNLPParser;
import workFlow.Function;



public class InitListener implements ServletContextListener{   
	// 实现其中的销毁函数   


	private static void addDoc(IndexWriter w,String functionName, String url ) throws IOException { 
		Document doc = new Document(); 
		doc.add(new TextField("functionName", functionName, Field.Store.YES));//no norms 取消权重功能 

		doc.add(new StringField("url", url, Field.Store.YES)); 
		w.addDocument(doc); 
	} 



	private  Directory indexAll() throws Exception { 


		Directory index  = new RAMDirectory() ; 
		try { 

			Analyzer analyzer = new StandardAnalyzer(); 
			IndexWriterConfig config = new IndexWriterConfig( analyzer); 
			IndexWriter w = new IndexWriter(index, config);

			addDoc(w, "账户概览", "checkBalance");
			addDoc(w, "交易明细","transactionDetail");
			
			addDoc(w, "中行内转账汇款", "innerbankTransfer");
			addDoc(w, "外币跨境汇款","crossborderTransfer");
			addDoc(w, "跨行转账汇款", "interbankTransfer");
			
			addDoc(w, "信用卡还款","paybackBill");
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
			
			//创建导航索引
			ServletContext sct=sce.getServletContext(); 
			Directory index = indexAll(); 
			sct.setAttribute("reader", 	 DirectoryReader.open(index));   
						
			
			//------------------初始化交互引擎----------------------
			
			Function function1 = new Function( "100101","跨行转账", null, null);
			function1.setParser(new CrossBankNLPParser());
			
			Map funcitonMap = new HashMap();
			sct.setAttribute("跨行转账",function1);   
			
			
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 

}  