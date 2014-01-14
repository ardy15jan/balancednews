/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author xtang
 */
public class TextProcessor {
    public static String processContent(String content, String title){
        content = content.trim();
        content=getRidOfTitle(content, title);
        content=deleteHeader(content);
        
        content=addLineBreak(content);
        content=content.trim();
        
        return content;
    }
    
    public static String toHTML(String content){
        return content.replace("\n", "<br\\>");
    }
    
    private static String getRidOfTitle(String content, String title){
         title=title.replace("'", "");
         if(title.indexOf(" - ") != -1)
             title=title.substring(0, title.indexOf(" - "));
         return content.replace(title, "");
    }
    
    private static String getResourceName(String title){
       String[] array=title.split(" - ");
       return array[array.length-1];
    }
    
    private static String addLineBreak(String content){
        return content.replaceAll("\\n+", "\n \n");
    }
    
    /*
    private static String deleteHeader(String content, String title){
        String resourceName=getResourceName(title);
        String result=content;
        if(resourceName.equals("SmartMoney.com")){
            result=deletePara(content,2);
        }else if(resourceName.equals("Bloomberg")){
            result=deletePara(content,2);
        }else if(resourceName.equals("Yahoo! Finance")){
            result=deletePara(content,7);
        }else if(resourceName.equals("SmartMoney")){
            result=deletePara(content,2);
        }
        
        return result;
    }
    */
    
    //Find the first paragraph which has more than 10 words. 
    private static String deleteHeader(String content){
        int index = content.indexOf("\n");
        if(index == -1)
            return content;
        String paragraph = content.substring(0, index);
        if(paragraph.split(" ").length>10)
            return content;
        else{
            content = deletePara(content, 1);
            return deleteHeader(content);
        }
         
    }
    
    private static String deletePara(String content, int num){
        
        for(int i=0;i<num;i++){
            int index=content.indexOf("\n")+1;
            if(index>content.length()) break;
            content=content.substring(content.indexOf("\n")+1, content.length());
        }
        return content;
    }
    
    public static String getFirstNPara(String content, int n){
        
        int index=0;
        
        for(int i=0;i<n;i++){
            index=content.indexOf("\n",index+1);
            if(index>content.length()-1)
                break;
        }
        
        return content.substring(0,index+1);
    }
    
}
