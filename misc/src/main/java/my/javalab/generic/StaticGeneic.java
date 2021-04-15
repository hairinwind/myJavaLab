package my.javalab.generic;

import java.util.Arrays;
import java.util.List;

/**
 * static method returns generic type
 * the invoke is like StaticClass.<T>staticMethod(...)
 * If <T> contains multiple and it is too long, intelliJ would display <~> and after click, the detail is displayed
 */
public class StaticGeneic {

    public static void main(String[] args) {
        List<String> stringList = StaticGeneic.<String>convert("a", "b");
        System.out.println(stringList);

        List<Long> longList = StaticGeneic.<Long>convert(1L, 2L);
        System.out.println(longList);
    }

    public static <T> List<T> convert(T... args) {
        return Arrays.asList(args);
    }

}
