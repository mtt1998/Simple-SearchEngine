package com.utils;

import java.util.ArrayList;
import java.util.HashMap;

//读document,建立fields
//对于每个document的每个field，使用analyzed得到term, document id, position id, 建立posting list
public class Document {
    private int id; //document id
    private ArrayList<Field> fieldList; //fields in document
    private HashMap<String, Integer> wordFreq; //all term frequency

    public Document(int id){
        this.id = id;
        fieldList = new ArrayList<Field>();
        wordFreq = new HashMap<String, Integer>();
    }


    public void addField(Field field){
        fieldList.add(field);
    }

    public int getFieldNum(){
        return fieldList.size();
    }

    public HashMap<String, Integer> getWordFreq() {
        return wordFreq;
    }

    public Field getField(int i){
        if (i < getFieldNum()){
            return fieldList.get(i);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void addWord(String word){
        if (wordFreq.containsKey(word)){
            wordFreq.put(word, wordFreq.get(word) + 1);
        }else{
            wordFreq.put(word, 1);
        }
    }

//    public HashMap<String, Double> getDocTf(){
//        return null;
//    }
}
