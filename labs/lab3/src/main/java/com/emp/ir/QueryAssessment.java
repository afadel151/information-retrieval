package com.emp.ir;

import java.util.HashSet;

public record QueryAssessment(int queryID, int relevantDocs,HashSet<Integer> relevantDocIds) {
    
}
