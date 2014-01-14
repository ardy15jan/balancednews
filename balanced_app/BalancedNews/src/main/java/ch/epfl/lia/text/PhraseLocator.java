package ch.epfl.lia.text;

import android.util.Log;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.lia.model.News;

/**
 * Created by xtang on 13-11-21.
 */
public class PhraseLocator {
    private ArrayList<News.OpinionPhrase> phrases;
    private String content;
    //private static final String connector = ".*?";
    private static final String NOT_WORD = "\\W+";
    private static final String connector = "[^\\.]*?";

    public PhraseLocator(ArrayList<News.OpinionPhrase> phrases, String content){
        this.phrases = phrases;
        this.content = content.toLowerCase();
    }

    public void fillLocation(){

        int previous = 0;
        for(int i=0; i<phrases.size();){
            int repetition = 0;
            for(int j=0; j<i; j++){
                if(phrases.get(i).phrase.toLowerCase().equals(phrases.get(j).phrase.toLowerCase()))
                    repetition ++;
            }

            if(i>0){
                previous = phrases.get(i-1).index;
            }

            Tuple match = null;
            match = findMatch(content, phrases.get(i).phrase, 0, previous);

            if(match == null){
                match = findMatch(content, phrases.get(i).phrase, repetition, 0);
            }

            if(match !=null){
                phrases.get(i).sentence = match.phrase;
                phrases.get(i).index = match.index;
                i++;
            }else{
                phrases.remove(i);
            }
        }
    }

    private Tuple findMatch(String line, String phrase, int repetition, int previous){
        String phrase1 = null;
        String phrase2 = null;


        line = line.substring(previous+1, line.length());
        String[] phrases = phrase.split(" ");
        if(phrases.length == 3){
            phrase1 = phrases[1];
            phrase2 = phrases[2];
        }else{
            phrase1 = phrases[0];
            phrase2 = phrases[1];
        }

        phrase1 = phrase1.toLowerCase();
        phrase2 = phrase2.toLowerCase();

        String reg1 = phrase1+NOT_WORD+connector+phrase2+NOT_WORD;
        String reg2 = phrase2+NOT_WORD+connector+phrase1+NOT_WORD;

        Pattern pattern1 = Pattern.compile(reg1);
        Pattern pattern2 = Pattern.compile(reg2);

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);

        Tuple tuple1 = null;
        Tuple tuple2 = null;

        for(int i=0; i<repetition; i++){
            matcher1.find();
        }
        if(matcher1.find()){
            tuple1 = new Tuple(matcher1.group(), matcher1.start()+previous+1);
        }


        for(int i=0; i<repetition; i++){
            matcher2.find();
        }
        if(matcher2.find()){
            tuple2 = new Tuple(matcher2.group(), matcher2.start()+previous+1);
        }

        if(tuple1!=null && tuple2!=null){
            return tuple1.index<tuple2.index?tuple1:tuple2;
        }else if(tuple1 != null){
            return tuple1;
        }else if(tuple2 != null){
            return tuple2;
        }

        return null;
    }

    private class Tuple{
        int index;
        String phrase;
        Tuple(String phrase, int index){
            this.index = index;
            this.phrase = phrase;
        }
    }
}
