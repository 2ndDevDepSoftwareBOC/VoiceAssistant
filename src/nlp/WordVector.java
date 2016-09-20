package nlp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class WordVector {

	HashMap<String, HashSet<String>> wordsSet;
	
	public WordVector() {
		HashSet<String> hashSet = new HashSet<>();
		hashSet.add("转账");
		hashSet.add("汇款");
		hashSet.add("还钱");
		hashSet.add("转给");
		hashSet.add("转");
		hashSet.add("汇");
		hashSet.add("给");
		wordsSet.put("转账", hashSet);
	}
	
	public String matchWordSet(String word) {
		String matchedWord = "";

		Iterator<Entry<String, HashSet<String>>> iter = wordsSet.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, HashSet<String>> entry = (Entry<String, HashSet<String>>) iter.next();
			HashSet<String> hashSet = entry.getValue();
			if (hashSet.contains(word)) {
				matchedWord = entry.getKey();
				break;
			}
		}
		
		return matchedWord.equals("") ? null : matchedWord;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
