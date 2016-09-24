package filter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import nlp.*;
import workFlow.*;

@WebFilter(urlPatterns={"/voiceAssistant/redirect"}, asyncSupported=true, dispatcherTypes={DispatcherType.REQUEST}
         )  

/**
 * 
 * @将文本转成结构化文本
 *
 */
public class Filter2Str2Function implements Filter {  
   
   @Override  
   public void destroy() {  
      System.out.println("destory Filter2Str2Function");  
   }  
   
   @Override  
   public void doFilter(ServletRequest request, ServletResponse response,  
         FilterChain chain) throws IOException, ServletException {  
      System.out.println("init Filter2Str2Function");  
      
      String originalStr = (String) request.getAttribute("originalStr");
      
      TLPAllTaskProcessor tlpProcessor = new TLPAllTaskProcessor();
      String nlpJsonStr = tlpProcessor.process(originalStr);//词性分析及依赖关系
      String intentWord = getIntentWord(nlpJsonStr);
      
      request.setAttribute("intentWord", intentWord);
      request.setAttribute("nlpJsonStr", nlpJsonStr);
      
      chain.doFilter(request, response);
   }  
   
   
   /**
    * 确定服务大类
    * @param jsonStr
    * @return
    */
   public String getIntentWord(String jsonStr) {

		JSONArray jsonArray = new JSONArray(jsonStr); // 段落的列表
		JSONArray jsonWordArray = jsonArray.getJSONArray(0).getJSONArray(0); //每个取第一个元素，词的列表

		int wordId = 0;
		JSONObject jsonWord = null;
		String rootWord="";
		while (wordId < jsonWordArray.length()) {
			
			jsonWord = jsonWordArray.getJSONObject(wordId);
			if (jsonWord.get("semrelate").equals("Root")) {
				rootWord = jsonWord.getString("cont");
				break;
			}
			wordId++;
		}

		 WordVector wordVector = new WordVector();
	     return  wordVector.matchWordSet(rootWord); // 规则硬匹配，后面改word2vec
	}
   
   @Override  
   public void init(FilterConfig filterConfig) throws ServletException {  
    //  String param1 = filterConfig.getInitParameter("param1");  
   }  
   
} 