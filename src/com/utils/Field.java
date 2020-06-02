package com.utils;

// 文档由不同的field组成，每个field包含name, value, 状态(是否被索引, 是否需要分词，是否)
public class Field {
    private String name;
    private String value;
    private boolean indexed;
    private boolean analyzed;

    public Field(String name, String value) {
        this.name = name;
        this.value = value;
        this.indexed = false;
        this.analyzed = false;
    }

    public Field(String name, String value, boolean indexed, boolean analyzed) {
        this.name = name;
        this.value = value;
        this.analyzed = analyzed;
        this.indexed = indexed;
    }
//    public ArrayList<String> getTermStream(){}


    public boolean isIndexed() {
        return indexed;
    }

    public boolean isAnalyzed(){
        return analyzed;
    }

    public String getValue(){
        return value;
    }
}
