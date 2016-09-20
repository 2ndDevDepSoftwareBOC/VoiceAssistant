package workFlow;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class NLPParser {
	
	public abstract void match(HashMap<String, String> hashMap, JSONObject jsonWord);

	public HashMap<String, String> execute(String originalStr, String functionName, String jsonStr){

		HashMap<String, String> retMap = new HashMap<String, String>();
		retMap.put("originalStr", originalStr);
		retMap.put("function", functionName);

		JSONArray jsonArray = new JSONArray(jsonStr); // 段落的列表
		JSONArray jsonWordArray = jsonArray.getJSONArray(0).getJSONArray(0); //每个取第一个元素，词的列表
		
		int wordId = 0;
		JSONObject jsonWord = null;
		while (wordId < jsonWordArray.length()) {
			
			jsonWord = jsonWordArray.getJSONObject(wordId);
			match(retMap, jsonWord);
			wordId++;
		}

		return retMap;
	}
}
