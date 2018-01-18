
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args){
        
        LZ78 lz78 = new LZ78();
        
        ArrayList<Tag> Tags = lz78.CompressData("ABX");
        
        String Original = lz78.DeCompressData(Tags);
        
        System.out.println(Original);
        
    }
    
}
