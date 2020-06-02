package com.utils;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Analyzer {
    private Set<String> stopWords;
    private SnowballStemmer stemmer;
    private SimpleTokenizer tokenizer;

    public Analyzer(){
        tokenizer = SimpleTokenizer.INSTANCE;
        stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
        stopWords =  new HashSet<>();
    }
    public Analyzer(String stopWordFilePath) throws Exception{
        tokenizer = SimpleTokenizer.INSTANCE;
        stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
        stopWords =  new HashSet<>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(stopWordFilePath), "UTF-8");
        BufferedReader bf = new BufferedReader(reader);
        String line;
        while ((line = bf.readLine()) != null){
            stopWords.add(line.toLowerCase().trim());
        }
        bf.close();
        reader.close();
    }

    public String stemWord(String word){
        return (String) stemmer.stem(word);
    }

    public ArrayList<String> processText(String text, boolean analyzed){
        text = text.toLowerCase();
        String[] strArr = tokenizer.tokenize(text);
        ArrayList<String> tokens = new ArrayList<String>();
        //filter stopwords and analyzed
        for (String word: strArr){
            if (analyzed){
                word = (String) stemmer.stem(word);
            }
            if(stopWords.contains(word)){
                continue;
            }
            if (word.length() == 1){
                continue;
            }
            tokens.add(word);
        }
        return tokens;
    }
}
