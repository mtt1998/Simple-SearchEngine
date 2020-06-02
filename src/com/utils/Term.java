package com.utils;


import java.util.ArrayList;
import java.util.HashMap;

public class Term implements Comparable<Term>{
    private String value;
    private int totalFreq;
    private HashMap<Integer, Integer> docToFreq;
//    private HashMap<Integer, ArrayList<Integer>> docToPos;
    //invert index: term ,document id, => posting list term, document id, count
    public Term(String value){
        this.value = value;
        totalFreq = 0;
        docToFreq = new HashMap<Integer, Integer>();
    }

    public HashMap<Integer, Integer> getDocToFreq(){
        return docToFreq;
    }

    public void setDocToFreq(HashMap<Integer, Integer> docToFreq){
        this.docToFreq = docToFreq;
    }

    public void setTotalFreq(int totalFreq){
        this.totalFreq = totalFreq;
    }

    public int getTf(int docId){
        return docToFreq.getOrDefault(docId, 0);
    }

    public double getDf(){
        return docToFreq.size();
    }

    public void add(int docId){
        docToFreq.put(docId, docToFreq.getOrDefault(docId, 0) + 1);
        totalFreq += 1;
    }


    public String getValue(){
        return value;
    }

    @Override
    public int compareTo(Term t2) {
        return this.value.compareTo(t2.getValue());
    }

    public int getTotalFreq() {
        return totalFreq;
    }
}
