
import java.util.ArrayList;

public class LZW {
    
    private ArrayList<String> Table;
    
    public LZW(){
        Table = new ArrayList<>();
    }
    
    public ArrayList<Integer> CompressData(String Data){
        
        ArrayList<Integer> ret = new ArrayList<>();
        
        init();
        
       for(int i=0; i<Data.length(); ++i){
           
           int j = i;
           String Current = "";
           
           while(j < Data.length() && Table.contains(Current)){
               Current += Data.charAt(j++);
           }
           
           if(Table.contains(Current)){
               
               ret.add(Table.indexOf(Current));
               
           } else {
               
               ret.add(Table.indexOf(Current.substring(0, Current.length()-1)));
               
               Table.add(Current);
               
           }
           
           i = j-1;
           
       }
       
       return ret;
        
        
    }
    
    private void init(){
        
        Table.clear();
        
        for(int i=0; i<128; ++i){
            Table.add((char)i + "");
        }
    }
    
}
