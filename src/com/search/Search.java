package com.search;

import com.index.IndexReader;
import com.utils.Analyzer;
import com.utils.Term;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Search {

    protected IndexReader reader;
    protected Analyzer analyzer;

    protected String[] pages;
    private String docFilePath = "G://SearchEngine//papers.txt";

    public Search() throws Exception {
        reader = new IndexReader();
        analyzer = new Analyzer();
        pages = new String[reader.getDocSize()];
        FileReader fr = new FileReader(docFilePath);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        int i = 0;
        while ((line = br.readLine()) != null){
            pages[i ++] = line.trim();
            if (i >= reader.getDocSize()){
                break;
            }
        }
        br.close();
        fr.close();
    }

    public String getDoc(int docId){
        return pages[docId];
    }
}


