/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author xtang
 */
public class News {
    public String title;
    public String content;
    public int opinionValue;
    public int id;
    public String url;
    public ArrayList<OpinionPhrase> phrases;
    
    public static class OpinionPhrase{
        public String phrase;
        public boolean isPositive;
        public String sentence;
        public int index;
    }
}
