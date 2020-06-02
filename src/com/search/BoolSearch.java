package com.search;

import com.sun.deploy.util.StringUtils;
import com.utils.Term;
import opennlp.tools.util.StringUtil;
//import com.google.common.collect.Sets;

import java.lang.reflect.Array;
import java.util.*;

public class BoolSearch extends Search {
    private String name;
    private HashMap<String, Term> termCache;
    private HashSet<String> specials;

    private HashSet<String> allDocSet = new HashSet<String>();
    public BoolSearch() throws Exception{
        name = "bool query";
        termCache = new HashMap<String, Term>();
        specials = new HashSet<String>();
        specials.addAll(Arrays.asList(new String[]{"AND", "OR", "NOT", "(", ")"}));
        for(int i = 0; i < reader.getDocSize(); i ++){
            allDocSet.add(String.format("%d", i));
        }
    }
    public String[] getQuery(String query) throws Exception {
        String[] tmp = query.split(" ");
        for(int i = 0; i < tmp.length; i++){
            if (specials.contains(tmp[i])){
                continue;
            }
            tmp[i] = analyzer.stemWord(tmp[i].toLowerCase());
            Term term = reader.getTermByValue(tmp[i]);
            termCache.put(tmp[i], term);
        }
        return tmp;
    }

        public String getTermDoc(Term term){
        StringBuffer bf = new StringBuffer();
        if (term != null) {
            for(int id: term.getDocToFreq().keySet()){
                bf.append(id);
                bf.append(" ");
            }
        }
        return bf.toString().trim();
    }
    public String and_op(String arg1, String arg2){
        Set<String> id1 = new HashSet<String>(Arrays.asList(arg1.split(" ")));
        Set<String> id2 = new HashSet<String>(Arrays.asList(arg2.split(" ")));
        id1.retainAll(id2);
        return StringUtils.join(id1, " ");
    }
    public String or_op(String arg1, String arg2){
        Set<String> id1 = new HashSet<String>(Arrays.asList(arg1.split(" ")));
        Set<String> id2 = new HashSet<String>(Arrays.asList(arg2.split(" ")));
        id1.addAll(id2);
        return StringUtils.join(id1, " ");
    }
    public String not_op(String arg1){
        Set<String> id1 = new HashSet<String>(Arrays.asList(arg1.split(" ")));
        Set<String> ans = new HashSet<String>(allDocSet);
        for (String tmp: id1){
            ans.remove(tmp);
        }
        return StringUtils.join(ans, " ");
    }
    public ArrayList<Integer> parseBoolExpr(String[] expression) {
        ArrayList<String> stk = new ArrayList<String>();
        ArrayList<String> operators = new ArrayList<String>();
        String op, arg2, arg1;
        for (String token: expression){
            if (token.equals("(")){
                stk.add(token);
            }else if(token.equals(")")){
                op = operators.remove(operators.size()-1);
                System.out.println(stk.size());
                arg1 = stk.remove(stk.size()-1);
                System.out.println(stk.size());
                if (op.equals("NOT")){
                    arg1 = not_op(arg1);
                }
//                System.out.println(op);

                while (stk.size() > 0){
                    arg2 = stk.get(stk.size() - 1);
                    stk.remove(stk.size()-1);
                    if (arg2.equals("(")){
                        break;
                    }
                    if (op.equals("AND")){
                        arg1 = and_op(arg1, arg2);
                    }else if(op.equals("OR")){
                        arg1 = or_op(arg1, arg2);
                    }else{
                        System.out.println("ERROR");
                    }
                }
                stk.add(arg1);
            }else if(specials.contains(token)){
                operators.add(token);
            }else{
//                System.out.println(token);
                stk.add(getTermDoc(termCache.get(token)));
            }
        }
        String[] ans = stk.get(0).split(" ");
        ArrayList<Integer> ansAll = new ArrayList<Integer>();
        for(int i = 0; i < ans.length; i++){
            if (ans[i].length() == 0)
                continue;
            ansAll.add(Integer.parseInt(ans[i]));
        }
        return ansAll;
    }

    public ArrayList<Integer> search(String query) throws Exception{
        String[] expression = getQuery(query);
        return parseBoolExpr(expression);
    }
}
