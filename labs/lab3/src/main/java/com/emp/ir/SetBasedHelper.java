package com.emp.ir;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetBasedHelper {

    public static void main(String[] args) throws IOException {
        String folderPath = "results";
        Map<String, Map<Integer, QueryResult>> allResults = ResultParser.parseAllSystems(folderPath);
        String assessmentsPath = "assessments.txt";
        Map<Integer, QueryAssessment> assessments = AssessmentParser.parseAssessments(assessmentsPath);
        for (var systemNumber : allResults.keySet()) {
            System.out.println("System ->" + systemNumber);
            for (var queryId : allResults.get(systemNumber).keySet()) {
                QueryResult queryResults = allResults.get(systemNumber).get(queryId);
                QueryAssessment queryAssessments = assessments.get(queryId);

                System.out.println("Query:" + queryId);
                System.out.println("Recall :" + calculateRecall(queryResults, queryAssessments));
                System.out.println("Precision :" + calculatePrecision(queryResults, queryAssessments));
                System.out.println("F1-score :" + calculateF1(queryResults, queryAssessments));
            }
        }

    }

    public static double calculateRecall(QueryResult result, QueryAssessment assessment) {
        List<Integer> querydocIds = result.docIds();
        Set<Integer> releventDocIds = assessment.relevantDocIds();
        Set<Integer> intersectionSet = new HashSet<>(releventDocIds);
        intersectionSet.retainAll(querydocIds);
        return (double) intersectionSet.size() / (double) releventDocIds.size();
    }

    public static double calculatePrecision(QueryResult result, QueryAssessment assessment) {
        List<Integer> querydocIds = result.docIds();
        Set<Integer> releventDocIds = assessment.relevantDocIds();
        Set<Integer> intersectionSet = new HashSet<>(releventDocIds);
        intersectionSet.retainAll(querydocIds);
        return (double) intersectionSet.size() / (double) querydocIds.size();
    }

    public static double calculateF1(QueryResult result, QueryAssessment assessment) 
    {
        
        double precision = calculatePrecision( result,  assessment);
        double recall = calculateRecall( result,  assessment);
        return (2*precision*recall)/(precision+ recall);
    }
}
