package com.emp.web_indexer;

import java.util.List;

public class Posting {
    private final int docId;
    private final int termFrequency;
    private final List<PositionInfo> positions;   

    public Posting(int docId,int termFreq,List<PositionInfo> positions){
        this.docId = docId;
        this.termFrequency = termFreq;
        this.positions = positions;
    }

}
