public class Pair {
    
    private String key;
    private Block value;
    
    public Pair(){
        key = "";
        value = new Block();
    }
    
    public Pair(String key, Block value){
        
        this.key = key;
        this.value = value;
        
    }
    
    public String getKey(){
        return key;
    }
    
    public Block getValue(){
        return value;
    }
    
}
