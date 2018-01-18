
import java.util.ArrayList;

public class LZ78 {
    
    public ArrayList<Tag> CompressData(String Data){
        
        ArrayList<Tag> Tags = new ArrayList<>();
        
        ArrayList<String> Table = new ArrayList<>();
        Table.add("");
        
        for(int i=0; i<Data.length(); ++i){
            
            String Current = "";
            int j = i;
            
            while(j < Data.length() && Table.contains(Current)){
                Current += Data.charAt(j++);
            }
            
            if(Table.contains(Current)){
                
                int index = Table.lastIndexOf(Current);
                char Next = '\0';
                
                Tags.add(new Tag(index, Next));
                
            } else {
                
                int index = Table.lastIndexOf(Current.substring(0, Current.length()-1));
                char Next = Current.charAt(Current.length()-1);
                
                Tags.add(new Tag(index, Next));
                
            }
            
            i = j - 1;
            
        }
        
        return Tags;
        
    }
    
    public String DeCompressData(ArrayList<Tag> Tags){
        
        ArrayList<String> Table = new ArrayList<>();
        Table.add("");
        
        String Original = "";
        
        for(Tag tag: Tags){
            
            int index = tag.index;
            char Next = tag.Next;
            
            Original += Table.get(index) + Next;
            
        }
        
        return Original;
    }
    
}
