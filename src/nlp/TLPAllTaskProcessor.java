package nlp;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONTokener;

public class TLPAllTaskProcessor {

	private final String urlGetBase = "http://api.ltp-cloud.com/analysis/?";
	private final String apiKey = "J7W9W8w5FV7p5GpHfnXqGy4noJWNDHmh3cGlPfVp";
	private final String format = "json";
	private final String pattern = "all";

	public String process(String originalStr) throws IOException {
		String encodeStr = URLEncoder.encode(originalStr, "utf-8");
		
		// "http://api.ltp-cloud.com/analysis/?api_key=YourApiKey&text=我是中国人。&pattern=dp&format=plain"
		String urlStr = urlGetBase 
				+ "api_key=" + apiKey 
				+ "&text=" + encodeStr 
				+ "&pattern=" + pattern 
				+ "&format=" + format;
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		conn.connect();

		InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "utf-8");
		
		JSONTokener jsonTokener = new JSONTokener(inputStreamReader);
		JSONArray jsonArray = new JSONArray(jsonTokener);
		inputStreamReader.close();
		
		return jsonArray.toString();
	}
	
	/*
	 * 
	 * 	[
		 [
		  [
		   {
		    "id": 0, 
		    "cont": "我", 
		    "pos": "r", 
		    "ne": "O", 
		    "parent": 1, 
		    "relate": "SBV", 
		    "semparent": 1, 
		    "semrelate": "Aft", 
		    "arg": []
		   }, 
		   {
		    "id": 1, 
		    "cont": "爱", 
		    "pos": "v", 
		    "ne": "O", 
		    "parent": -1, 
		    "relate": "HED", 
		    "semparent": -1, 
		    "semrelate": "Root", 
		    "arg": [
		     {
		      "id": 0, 
		      "type": "A0", 
		      "beg": 0, 
		      "end": 0
		     }, 
		     {
		      "id": 1, 
		      "type": "A1", 
		      "beg": 2, 
		      "end": 3
		     }
		    ]
		   }, 
		   {
		    "id": 2, 
		    "cont": "北京", 
		    "pos": "ns", 
		    "ne": "B-Ns", 
		    "parent": 3, 
		    "relate": "ATT", 
		    "semparent": 3, 
		    "semrelate": "Nmod", 
		    "arg": []
		   }, 
		   {
		    "id": 3, 
		    "cont": "天安门", 
		    "pos": "ns", 
		    "ne": "E-Ns", 
		    "parent": 1, 
		    "relate": "VOB", 
		    "semparent": 1, 
		    "semrelate": "Cont", 
		    "arg": []
		   }
		  ]
		 ]
		]
	 * */
	
	public static void main(String[] args) throws IOException {
		//String testStr = "我要转给李朋1000元钱";
		String testStr = "我要查询借记卡余额";
		TLPAllTaskProcessor allTaskProcessor = new TLPAllTaskProcessor();
		System.out.println(allTaskProcessor.process(testStr));
	}

}
