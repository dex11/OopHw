// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
    private Taboo<Character> tab;

    public void testTaboo1() {
        List<Character> lst = new ArrayList<>();
        lst.add('a');
        lst.add('b');
        lst.add('a');
        lst.add('b');
        lst.add('c');
        tab = new Taboo<>(lst);
        Set<Character> s = new HashSet<>();
        s.add('a');
        s.add('c');
        assertEquals(s, tab.noFollow('b'));
    }

    public void testTaboo2() {
        List<Character> lst = new ArrayList<>();
        lst.add('a');
        lst.add('b');
        lst.add('a');
        lst.add('b');
        lst.add('c');
        tab = new Taboo<>(lst);
        Set<Character> s = new HashSet<>();
        s.add('a');
        s.add('c');
        assertEquals(null, tab.noFollow('x'));
    }

    public void testTaboo3() {
        List<Character> lst = new ArrayList<>();
        lst.add('a');
        lst.add('c');
        lst.add('a');
        lst.add('b');
        tab = new Taboo<>(lst);

        List<Character> s = new ArrayList<>();
        s.add('a');
        s.add('c');
        s.add('b');
        s.add('x');
        s.add('c');
        s.add('a');

        List<Character> ans = new ArrayList<>();
        ans.add('a');
        ans.add('x');
        ans.add('c');
        tab.reduce(s);

        assertEquals(ans, s);
    }
}
