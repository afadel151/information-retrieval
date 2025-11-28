package com.emp.web_indexer;

public class PositionInfo {
    private final int position;
    private final String tag;
    private final double tagWeight;

    public PositionInfo(int position, String tag, double tagWeight) {
        this.position = position;
        this.tag = tag;
        this.tagWeight = tagWeight;
    }
}
