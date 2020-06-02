package com.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {



    public static void main(String[] args) throws Exception {

        VSMSearch searcher = new VSMSearch();
        String query = "Question Generation sequence-to-sequence copy";
        int topK = 20;

        ArrayList<Map.Entry<Integer, Double>> rankDocs = searcher.search(query);
        System.out.println("Query: " + query);
        System.out.println("total results number : " + rankDocs.size());
        for(int i = 0; i < rankDocs.size(); i++){
            System.out.println("rank " + (i + 1));
            System.out.println("score: " + rankDocs.get(i).getValue());
            System.out.println(searcher.getDoc(rankDocs.get(i).getKey()));
            System.out.println("=================================");
            if(i + 1 == topK)
                break;
        }

//        String query = "AND ( NOT ( OR ( type classification ) ) Entity Linking Dan Roth )";
//        int topK = 20;
//
//        BoolSearch searcher = new BoolSearch();
//        ArrayList<Integer> rankDocs = searcher.search(query);
//        System.out.println("Query: " + query);
//        System.out.println("total results number : " + rankDocs.size());
//        for(int i = 0; i < rankDocs.size(); i++){
//            System.out.println("rank " + (i + 1));
//            System.out.println(searcher.getDoc(rankDocs.get(i)));
//            System.out.println("=================================");
//            if(i + 1 == topK)
//                break;
//        }
    }
}
