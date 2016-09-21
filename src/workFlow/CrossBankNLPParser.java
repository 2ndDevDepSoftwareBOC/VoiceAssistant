package workFlow;

import java.util.HashMap;

import org.json.JSONObject;

public class CrossBankNLPParser extends NLPParser{

	private void match(HashMap<String, String> hashMap, JSONObject jsonWord) {
		if (jsonWord.get("pos").equals("m")) {
			hashMap.put("number", jsonWord.getString("cont"));
		} else if (jsonWord.get("pos").equals("nh")) {
			hashMap.put("person", jsonWord.getString("cont"));
		}
	}
}
