package com.search;

import com.index.IndexReader;
import com.utils.Analyzer;
import com.utils.Term;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class VSMSearch extends Search{
    private String name;

    public VSMSearch() throws Exception {
        name = "VSM";
    }

    //HashMap(doc id, freq)
    public ArrayList<Term> getQuery(String str) throws Exception{
        ArrayList<String> tokens = analyzer.processText(str, true);
        ArrayList<Term> terms = new ArrayList<Term>();
        HashMap<String, Term> termMap = new HashMap<String, Term>();
        for(String w: tokens){
            if (termMap.containsKey(w)){
                terms.add(termMap.get(w));
            }else{
                Term t = reader.getTermByValue(w);
                if (t != null){
                    terms.add(t);
                    termMap.put(w, t);
                }
                System.out.println(w);

            }
        }
        return terms;
    }

    public double calNorm(HashMap<String, Double> vec){
        double len = 0.0;
        for(double val: vec.values()){
            len += val * val;
        }
        return Math.sqrt(len);
    }

    public double calSimilarity(HashMap<String, Double> v1, HashMap<String, Double> v2){
        double score = 0.0;
        for (Map.Entry<String, Double> entry: v1.entrySet()){
            score += v2.get(entry.getKey()) * entry.getValue();
        }
//        score = score / (calNorm(v1) * calNorm(v2));
        return score;
    }

    public ArrayList<Map.Entry<Integer, Double>> search(String str) throws Exception{
        ArrayList<Term> queryTerms = getQuery(str);
        HashMap<String, Term> termMap = new HashMap<String, Term>();
        HashMap<String, Double> idfMap = new HashMap<String, Double>();
        //doc scores
        HashMap<Integer, Double> candidateDocs = new HashMap<Integer, Double>();
        //
        HashMap<String, Double> queryVec = new HashMap<String, Double>();
        HashMap<String, Double> docVec;

        for(Term term: queryTerms){
            String token = term.getValue();
            if (!termMap.containsKey(token)) {
                termMap.put(token, term);
                idfMap.put(token, Math.log10(reader.getDocSize() / (double)term.getDf()));
            }
            for(int docId: term.getDocToFreq().keySet()){
                candidateDocs.put(docId, 0.0);
            }
            queryVec.put(token, queryVec.getOrDefault(token,0.0) + idfMap.get(token));
        }

        for(int docId: candidateDocs.keySet()){
            docVec = new HashMap<String, Double>();
            for (Map.Entry<String, Term> entry: termMap.entrySet()){
                Term term = entry.getValue();
                 docVec.put(entry.getKey(), term.getTf(docId) * idfMap.get(entry.getKey()));
            }
            candidateDocs.put(docId, calSimilarity(queryVec, docVec));
        }
        ArrayList<Map.Entry<Integer, Double>> rankDocs = new ArrayList<>();
        rankDocs.addAll(candidateDocs.entrySet());
        Collections.sort(rankDocs, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return ((o2.getValue() - o1.getValue() == 0) ? 0
                        : (o2.getValue() - o1.getValue() > 0) ? 1
                        : -1);            }
        });
        return rankDocs;
    }
}

