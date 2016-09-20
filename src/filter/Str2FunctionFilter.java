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

import nlp.*;
import workFlow.*;

@WebFilter(urlPatterns={"/engine/*"}, asyncSupported=true, dispatcherTypes={DispatcherType.REQUEST}
         )  

/**
 * 
 * @将声音转换为文本
 *
 */
public class Str2FunctionFilter implements Filter {  
   
   @Override  
   public void destroy() {  
      System.out.println("destory filter……");  
   }  
   
   @Override  
   public void doFilter(ServletRequest request, ServletResponse response,  
         FilterChain chain) throws IOException, ServletException {  
      System.out.println("Str2FunctionFilter filter……");  
      
      String plainStr = (String) request.getAttribute("plainStr");
      
      TLPAllTaskProcessor tlpProcessor = new TLPAllTaskProcessor();
      String jsonStr = tlpProcessor.process(plainStr);
      
      NLPParser nlpParser = new NLPParser(); 
      String rootWord = nlpParser.getRootWord(jsonStr);

      WordVector wordVector = new WordVector();
      String functionName = wordVector.matchWordSet(rootWord); // 硬匹配，后面改word2vec

      HashMap<String, String> functionElementMap = null;
      if (functionName.equals("转账")) { // 根据rootword确定业务规则

            functionElementMap = ((CrossBankNLPParser)nlpParser).execute(plainStr, functionName, jsonStr);
      }
   }  
   
   @Override  
   public void init(FilterConfig filterConfig) throws ServletException {  
    //  String param1 = filterConfig.getInitParameter("param1");  
   }  
   
} 