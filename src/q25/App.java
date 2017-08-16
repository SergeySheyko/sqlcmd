package q25;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by s.sheyko on 11.08.2017.
 */
public class App {
    public static void main(String[] args) {
        Set<Boolean> set = new HashSet<>();
        set.addAll(Arrays.asList(set.isEmpty(), set.isEmpty()));//[true]
        set.retainAll(Arrays.asList(set.containsAll(Arrays.asList(set.isEmpty(), set.isEmpty()))));
        System.out.println(set.size());
    }
}
