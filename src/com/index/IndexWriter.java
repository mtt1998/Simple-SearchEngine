package com.index;

import com.utils.Document;
import com.utils.Term;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexWriter {
    //need inplementation 对于VSM的余弦相似度等独立方式需要前向索引
    public void writeForwardIndex(ArrayList<Document> allDocs) throws Exception{
        //termid, count
    }

    public void writeInvertIndex(ArrayList<Term> allTerms, int docNum) throws Exception{
        //词典,term总数

        //termid, term出现次数, 到dictionary的指针，倒排表指针
        FileWriter termfw = new FileWriter("G:\\SearchEngine\\files\\termDict.txt");
        BufferedWriter termbw = new BufferedWriter(termfw);
        FileWriter indexfw = new FileWriter("G:\\SearchEngine\\files\\termIndex.txt");
        BufferedWriter indexbw = new BufferedWriter(indexfw);
        FileWriter freqfw = new FileWriter("G:\\SearchEngine\\files\\docFreq.txt");
        BufferedWriter freqbw = new BufferedWriter(freqfw);
        int i = 0;
        int termPointer = 0;
        int freqPointer = 0;
        FileWriter debugfw = new FileWriter("G:\\SearchEngine\\files\\debug.txt");
        BufferedWriter debugbw = new BufferedWriter(debugfw);
        //写索引信息
        HashMap<Integer, Integer> doc2freq = null;
        String freqLine = null;
        StringBuffer freqBuff = null;
        indexbw.write(String.format("%d %d\r\n", docNum, allTerms.size()));

//        System.out.println("========================");

        for(Term term: allTerms){
//            System.out.println(term.getValue());
            //写词典
            debugbw.write(term.getValue()+"\n");
            termbw.write(term.getValue());
            //写倒排表
            doc2freq = term.getDocToFreq();
            freqBuff = new StringBuffer();
//            freqLine = String.format("%d", term.getDf());
            for(int docId: doc2freq.keySet()){
                freqBuff.append(String.format(" %d %d", docId, doc2freq.get(docId)));
            }
            freqLine = freqBuff.toString();
            freqbw.write(freqLine);
            //写索引表
            indexbw.write(String.format("%d %d %d %d\r\n", i, term.getTotalFreq(), termPointer, freqPointer));
            //更新指针
            termPointer += term.getValue().getBytes("UTF-8").length;
            freqPointer += freqLine.getBytes("UTF-8").length;
            i += 1;
        }
        termbw.close();
        termfw.close();
        freqbw.close();
        freqfw.close();
        indexbw.close();
        indexfw.close();
        debugbw.close();
        debugfw.close();
    }
}
