package com.webapp.util;

import java.util.Arrays;

public class MinValueExample {
    public static int minValue(int[] values) {
        return Arrays.stream(values) // создаем поток из массива
                .distinct() // выбираем уникальные элементы
                .sorted() // сортируем элементы по возрастанию
                .reduce(0, (result, value) -> result * 10 + value); // собираем минимальное число
    }

    public static void main(String[] args) {
        int[] values1 = {1, 2, 3, 3, 2, 3, 1};
        System.out.println(minValue(values1)); // вывод: 123

        int[] values2 = {9, 8, 8, 9};
        System.out.println(minValue(values2)); // вывод: 89
    }
}
