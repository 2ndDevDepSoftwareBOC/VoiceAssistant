package workFlow;

import java.util.HashMap;

import org.json.JSONObject;

import nlp.FormatChecker;
import nlp.NumberFormatConvertor;

public class BankTransferNLPParser extends NLPParser {

	public static void main(String[] args) throws Exception {
//		HashMap m = new HashMap<>();
//		m.put("aa", "aa");
//		m.put("bb", "bb");
//		JSONObject jsonObject = new JSONObject(m);
//		System.out.println(jsonObject.toString());
	}

	@Override
	protected void match(HashMap<String, String> hashMap, JSONObject jsonWord) {
		if (jsonWord.get("pos").equals("m")) {
			String number = jsonWord.getString("cont");
			if (!FormatChecker.isNumber(number)) {
				number = NumberFormatConvertor.chineseStrToArabStr(number);
			}
			hashMap.put("number", number);
		} else if (jsonWord.get("pos").equals("nh")) {
			hashMap.put("person", jsonWord.getString("cont"));
		}
	}

}
