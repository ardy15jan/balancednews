/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author xtang
 */
public class Answer {
    public int understanding;
    public String description;
    public int topic_id;

    public Answer(int understanding, String description){
        this.understanding = understanding;
        this.description = description;
    }
}
