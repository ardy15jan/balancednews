/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.News.OpinionPhrase;

/**
 *
 * @author xtang
 */
public class OpinionFinder {
     
    private static final String POSITIVE_TAG = "(P)";
    private static final String NEGATIVE_TAG = "(N)";
    
    public static ArrayList<OpinionPhrase> getPhrases(String originPhrases){
        originPhrases = originPhrases.substring(originPhrases.indexOf("]")+1);
        String[] phrases = originPhrases.split(", ");
        
        ArrayList<OpinionPhrase> opinionPhrases = new ArrayList<OpinionPhrase>();
        for(String s: phrases){
            OpinionPhrase opinionPhrase = new OpinionPhrase();
            if(s.indexOf(POSITIVE_TAG) != -1){
                opinionPhrase.isPositive = true;
                opinionPhrase.phrase = s.substring(0, s.indexOf(POSITIVE_TAG));
                opinionPhrase.sentence = "";
                opinionPhrases.add(opinionPhrase);
            }
            
            if(s.indexOf(NEGATIVE_TAG) != -1){
                opinionPhrase.isPositive = false;
                opinionPhrase.phrase = s.substring(0, s.indexOf(NEGATIVE_TAG));
                opinionPhrase.sentence = "";
                opinionPhrases.add(opinionPhrase);
            }
        }
        
        return opinionPhrases;
    
    }
    
    
     
}
