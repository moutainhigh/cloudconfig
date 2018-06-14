package com.xkd.model;

/**
 * Created by dell on 2017/12/7.
 */
public class SolrPriorityBean  implements  Comparable<SolrPriorityBean>{

    private int priority;
    private String hitColumn;
    private String hitValue;

    public SolrPriorityBean(int priority, String hitColumn, String hitValue) {
        this.priority = priority;
        this.hitColumn = hitColumn;
        this.hitValue = hitValue;
    }

    @Override
    public int compareTo(SolrPriorityBean o) {
        return this.priority-o.priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getHitColumn() {
        return hitColumn;
    }

    public String getHitValue() {
        return hitValue;
    }
}
