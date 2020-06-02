package com.utils;

import com.utils.Document;
import com.utils.Field;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DocumentReader {
    // document id, offset in file
    HashMap<Integer, Integer> docOffset;

    public Document readDoc(String[] strArr, int id){
        String[] tmp;
        Field field;
        Document document = new Document(id);
        for (int i = 0; i < strArr.length; i++){
            tmp = strArr[i].split(":", 2);
//            System.out.println(tmp[0]);
            switch (tmp[0]){
                case "title":
                    field = new Field(tmp[0], tmp[1], true, true);
                    break;
                case "authors":
                    field = new Field(tmp[0], tmp[1], true, false);
                    break;
                case "url":
                    field = new Field(tmp[0], tmp[1], false, false);
                    break;
                case "abstract":
                    field = new Field(tmp[0], tmp[1], true, true);
                    break;
                default:
                    field = null;
            }
            document.addField(field);
        }
        return document;
    }



}
