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
public class Topic {
    public int topic_id;
    public News negativeNews;
    public News positiveNews;
    public ArrayList<Integer> newsList;
    public double positivePercentage;
    public ArrayList<String> tags;
}
