
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private HashMap<T, Set<T>> data;
	
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		data = new HashMap<>();
		for(int i = 0; i < rules.size() - 1; i++){
			if(data.containsKey(rules.get(i))){
				data.get(rules.get(i)).add(rules.get(i + 1));
			}else{
				Set<T> val = new HashSet<T>();
				val.add(rules.get(i + 1));
				data.put(rules.get(i), val);
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if(!data.containsKey(elem)) return null;
		return data.get(elem);
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		for(int i = 0; i < list.size() - 1; i++){
			Set<T> pos = this.noFollow(list.get(i) );
			if(pos != null && pos.contains(list.get(i + 1))){
				list.remove(i + 1);
				i--;
			}
		}
	}
}
