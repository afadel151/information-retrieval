package com.emp.ir;

import java.io.IOException;
import java.util.Map;

public class RankBasedHelper {

    public static void main(String[] args) throws IOException {
        String folderPath = "results";
        int k = 5;
        Map<String, Map<Integer, QueryResult>> allResults = ResultParser.parseAllSystems(folderPath);
        String assessmentsPath = "assessments.txt";
        Map<Integer, QueryAssessment> assessments = AssessmentParser.parseAssessments(assessmentsPath);
        for (var systemNumber : allResults.keySet()) {
            System.out.println("System ->" + systemNumber);
            for (var queryId : allResults.get(systemNumber).keySet()) {
                QueryResult queryResults = allResults.get(systemNumber).get(queryId);
                QueryAssessment queryAssessments = assessments.get(queryId);

                System.out.println("Query:" + queryId);
                System.out.println("Recall :" + calculateRecallAtK(k, queryResults, queryAssessments));
                System.out.println("Precision :" + calculatePrecisionAtK(k, queryResults, queryAssessments));
                System.out.println("F1-score :" + calculateF1AtK(k, queryResults, queryAssessments));
            }
        }

    }

    public static double calculateRecallAtK(int K, QueryResult result, QueryAssessment assessment) {
        double x = 0;
        return x;

    }

    public static double calculatePrecisionAtK(int K, QueryResult result, QueryAssessment assessment) {
        double x = 0;
        return x;
    }

    public static double calculateF1AtK(int K, QueryResult result, QueryAssessment assessment) {
        double x = 0;
        return x;
    }
}
