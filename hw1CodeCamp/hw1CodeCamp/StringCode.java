import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int ans = 0;
		int cur = 0;
		char mem = 'a';
		for(int i = 0; i < str.length(); i++){
			if(str.charAt(i) == mem) cur++;
			else{
				mem = str.charAt(i);
				if(cur > ans) ans = cur;
				cur = 1;
			}
		}
		return ans;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String ans = "";
		if(str.equals(ans)) return str;
		for(int i = 0 ; i < str.length() - 1; i++){
			if(Character.isDigit(str.charAt(i))) {
				for (int j = 0; j < Integer.parseInt(String.valueOf(str.charAt(i))); j++) {
					ans = ans + str.charAt(i + 1);
				}
			}else{
				ans = ans + str.charAt(i);
			}
		}
		if(!Character.isDigit(str.charAt(str.length() - 1))) ans = ans + str.charAt(str.length() - 1);
		return ans;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> d1 = new HashSet<>();
		HashSet<String> d2 = new HashSet<>();

		for(int i = 0; i < a.length() - len + 1; i++){
			d1.add(a.substring(i, i + len));
		}

		for(int i = 0; i < b.length() - len + 1; i++){
			d2.add(b.substring(i, i + len));
		}

		for(String mem : d2){
			if(d1.contains(mem)) return true;
		}

		return false;
	}
}
