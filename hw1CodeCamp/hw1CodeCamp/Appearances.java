import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int ans = 0;
		HashMap<T, Integer> dataA = new HashMap<T, Integer>();
		HashMap<T, Integer> dataB = new HashMap<T, Integer>();

		for(T iter : a){
			if(dataA.containsKey(iter)){
				int val = dataA.get(iter) + 1;
				dataA.remove(iter);
				dataA.put(iter, val);
			}else{
				dataA.put(iter, 1);
			}
		}

		for(T iter : b){
			if(dataB.containsKey(iter)){
				int val = dataB.get(iter) + 1;
				dataB.remove(iter);
				dataB.put(iter, val);
			}else{
				dataB.put(iter, 1);
			}
		}

		Iterator iter = dataA.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry elem = (Map.Entry) iter.next();
			if(dataB.containsKey(elem.getKey())){
				if(dataB.get(elem.getKey()) == elem.getValue()) ans++;
			}
		}

		return ans;
	}
	
}
