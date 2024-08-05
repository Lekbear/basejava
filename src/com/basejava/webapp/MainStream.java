package com.basejava.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        MainStream mainStream = new MainStream();
        System.out.println(mainStream.minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(mainStream.minValue(new int[]{9, 8}));
        System.out.println(mainStream.oddOrEven(Arrays.asList(1, 2, 3, 3, 2, 3)));
        System.out.println(mainStream.oddOrEven(Arrays.asList(2, 3, 3, 2, 3)));
    }

    int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (l, r) -> l * 10 + r);
    }

    List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream()
                .collect(Collectors.groupingBy(e -> e % 2 == 1));
        return map.get(true).size() % 2 == 1 ? map.get(false) : map.get(true);
    }
}
