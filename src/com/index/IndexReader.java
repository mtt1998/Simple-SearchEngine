package com.index;

import com.utils.Term;

import java.io.*;
import java.util.HashMap;

public class IndexReader {

    private int[] termPointers;
    private int[] freqPointers;
    private int[] termFreq;
    private int termSize;
    private int docSize;

    private String indexFilePath = "G:\\SearchEngine\\files\\termIndex.txt";
    private String termFilePath = "G:\\SearchEngine\\files\\termDict.txt";
    private String freqFilePath = "G:\\SearchEngine\\files\\docFreq.txt";

    public IndexReader() throws Exception{
        FileReader fr = new FileReader(indexFilePath);
        BufferedReader br = new BufferedReader(fr);
        String strLine = null;
        boolean isFirst = true;
        while((strLine = br.readLine()) != null)
        {
            strLine = strLine.trim();
            String[] tmp = strLine.split(" ");
            if (isFirst){
                isFirst = false;
                docSize = Integer.parseInt(tmp[0]);
                termSize = Integer.parseInt(tmp[1]);
                termPointers = new int[termSize];
                freqPointers = new int[termSize];
                termFreq = new int[termSize];
            }else{
                int id = Integer.parseInt(tmp[0]);
                termFreq[id] = Integer.parseInt(tmp[1]);
                termPointers[id] = Integer.parseInt(tmp[2]);
                freqPointers[id] = Integer.parseInt(tmp[3]);
//                System.out.println(tmp[0] + " " + termPointers[id]);
            }
        }
        fr.close();
        br.close();
    }

    public int getDocSize(){
        return docSize;
    }

    public String randomRead(String filePath, int pointer, int readLen) throws Exception{
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
        raf.seek(pointer);
        byte[] buff = new byte[102400];
        if(readLen == -1){
            raf.read(buff);
        }else{
            int i = 0;
            while(i < readLen)
            {
                buff[i++] = raf.readByte();
                raf.seek(pointer + i);
            }
        }
        raf.close();
        String str = new String(buff,"UTF-8");
        return str.trim();
    }

    //RandomAccessFile支持任意位置的读写，防止内存不够
    public String getTermValue(int id) throws Exception{
        int len = -1;
        if (id + 1 < termSize){
            len = termPointers[id + 1] - termPointers[id];
        }
        return randomRead(termFilePath, termPointers[id], len);
    }

    public HashMap<Integer, Integer> parseDocFreq(String str){
        String[] tmp = str.split(" +"); //正则表达式???
        //item id, freq
        HashMap<Integer, Integer> docFreqMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < tmp.length; i += 2){
            docFreqMap.put(Integer.parseInt(tmp[i]), Integer.parseInt(tmp[i + 1]));
        }
        return docFreqMap;
    }

    public HashMap<Integer, Integer> getDocFreqMap(int id) throws Exception{
        int len = -1;
        if (id + 1 < termSize){
            len = freqPointers[id + 1] - freqPointers[id];
        }
        String tmp = randomRead(freqFilePath, freqPointers[id], len);
        return parseDocFreq(tmp);
    }

    public Term getTermById(int id) throws Exception {
        String value = getTermValue(id);
        Term term = new Term(value);
        term.setDocToFreq(getDocFreqMap(id));
        term.setTotalFreq(termFreq[id]);
        return term;
    }

    public Term getTermByValue(String key) throws Exception{
        int low = 0;
        int high = termSize - 1;
        int middle = 0;			//定义middle

//        System.out.println(termSize);
//        for(int id = 0; id < termSize; id ++){
//            System.out.println(id + " " + getTermById(id).getValue());
//        }

        String middle_value = null;
        while(low <= high){
            middle = (low + high) / 2;
            middle_value = getTermValue(middle);
            if(middle_value.compareTo(key) == 0){
                return getTermById(middle);
            }else if(middle_value.compareTo(key) > 0){
                //比关键字小则关键字在右区域
                high = middle - 1;
            }else{
                low = middle + 1;
            }
        }
        return null;
    }

}
