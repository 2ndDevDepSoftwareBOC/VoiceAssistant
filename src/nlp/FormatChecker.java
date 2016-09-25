package nlp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatChecker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(FormatChecker.isNumber("0.112"));
	}

	public static boolean isNumber(String s) {
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(s);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
