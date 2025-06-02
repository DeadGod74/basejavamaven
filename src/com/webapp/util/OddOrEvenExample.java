package com.webapp.util;

import java.util.List;
import java.util.stream.Collectors;

public class OddOrEvenExample {
    public static List<Integer> oddOrEven(List<Integer> integers) {
        // считаем сумму чисел
        int sum = integers.stream().mapToInt(Integer::intValue).sum();

        // фильтруем список в зависимости от четности суммы
        return integers.stream()
                .filter(num -> (sum % 2 == 0) ? (num % 2 != 0) : (num % 2 == 0))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> numbers1 = List.of(1, 2, 3, 4, 5); //15 = нечетное, удаляем 1,3,5
        System.out.println(oddOrEven(numbers1)); // вывод: [2, 4]

        List<Integer> numbers2 = List.of(2, 4, 6, 8); //20 = четное, удаляем всё
        System.out.println(oddOrEven(numbers2)); // вывод: []

        List<Integer> numbers3 = List.of(1, 3, 3, 1, 4); //12 = четное, удаляем 4
        System.out.println(oddOrEven(numbers3)); // вывод: [1, 3, 3, 1]

        List<Integer> numbers4 = List.of(2, 3, 4); //9 = нечетное, удаляем 3
        System.out.println(oddOrEven(numbers4)); // вывод: [2, 4]
    }
}
