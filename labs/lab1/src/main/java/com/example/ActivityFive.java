package com.example;

import java.util.HashMap;
import java.util.Map;

public class ActivityFive {

    private static HashMap<String, Integer> list_of_stems_with_frequencies = new HashMap<>();
    public static HashMap<String, Integer> term_frequency_computing(String[] list_of_stems) {
        list_of_stems_with_frequencies.clear(); // reset before computing

        for (String stem : list_of_stems) {
            list_of_stems_with_frequencies.put(
                    stem,
                    list_of_stems_with_frequencies.getOrDefault(stem, 0) + 1
            );
        }

        System.out.println("=== List of Stems with Frequencies ===");
        for (Map.Entry<String, Integer> entry : list_of_stems_with_frequencies.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        return list_of_stems_with_frequencies;
    }

    public static double computing_TF(String term) {
        if (!list_of_stems_with_frequencies.containsKey(term)) {
            return 0.0;
        }

        int ft_d = list_of_stems_with_frequencies.get(term);
        if (ft_d <= 0) return 0.0;

        double tf = 1 + Math.log(ft_d);
        return tf;
    }
}
