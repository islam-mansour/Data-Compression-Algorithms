
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args){
        
        LZW lzw = new LZW();
        
        ArrayList<Integer> ret = lzw.CompressData("ABX");
        
        for(Integer x: ret)
            System.out.println(x);
    
    }
    
}
