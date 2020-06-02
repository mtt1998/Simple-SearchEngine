package com.index;

import com.utils.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class IndexBuilder {
    // term dictionary: string to term id
    // term (document id, frequence) list
    private int termSize;
    private HashMap<String, Term> dictionary;
    private Analyzer analyzer;
    private DocumentReader docReader;
    private IndexWriter writer;

    public IndexBuilder() throws Exception{
        termSize = 0;
        dictionary = new HashMap<>();
        docReader = new DocumentReader();
        analyzer = new Analyzer();
        writer = new IndexWriter();
    }

    public void addDocument(Document doc){
        Field field = null;
        ArrayList<String> tokens = null;
        Term term = null;
        for (int i = 0; i < doc.getFieldNum(); i++){
            field = doc.getField(i);
            if (field.isIndexed()){
                tokens = analyzer.processText(field.getValue(), field.isAnalyzed());
                for(String w: tokens){
//                    System.out.println(w);
                    doc.addWord(w); //建立正向索引, tf需要
                     if(dictionary.containsKey(w)){
                         dictionary.get(w).add(doc.getId());
                     }else{
                         term = new Term(w);
                         term.add(doc.getId());
                         dictionary.put(w, term);
                         termSize += 1;
                     }
                }
            }
        }
    }

    public void buildIndex(String allDocFilePath) throws Exception{
        //读入所有的document
        InputStreamReader reader = new InputStreamReader(new FileInputStream(allDocFilePath), "UTF-8");
        BufferedReader bf = new BufferedReader(reader);
        ArrayList<Document> allDocs = new ArrayList<Document>();
        String line;
        int docId = 0;
        while ((line = bf.readLine()) != null){
            allDocs.add(docReader.readDoc(line.split("\t"), docId));
            docId += 1;
//            if (docId > 20)
//                break;
        }
        bf.close();
        reader.close();
        //把document加入index(正向,构建docid [term frequency], 逆向, 构建 term string=>[docid, frequency])
        for (Document doc: allDocs){
            addDocument(doc);
        }
        //保存index allDocs: document list, dictionary, term list
        //获取term的字典序
        ArrayList<Term> allTerms = new ArrayList<Term>(dictionary.values());
        Collections.sort(allTerms);
        //写入词典：词典
//        writer.writeForwardIndex(allDocs);
        writer.writeInvertIndex(allTerms, allDocs.size());
    }

}
