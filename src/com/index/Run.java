package com.index;

import java.util.ArrayList;
import java.util.Map;

public class Run {
    public static void main(String[] args) throws Exception {
        IndexBuilder builder = new IndexBuilder();
        builder.buildIndex("G://SearchEngine//papers.txt");
    }
}
