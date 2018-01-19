public class Main {
    
    public static void main(String[] args){
        
        Huffman h = new Huffman();
        String ret = h.CompressData("abxxc9");
        String Original = h.DeCompressData(ret);
        //System.out.println(Original);
        
    }
    
}
