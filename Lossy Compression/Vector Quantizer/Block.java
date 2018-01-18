
import java.util.ArrayList;

public class Block {
    
    private ArrayList<Double> List;
    
    public Block(){
        this.List = new ArrayList<Double>();
    }
    
    public Block(ArrayList<Double> List){
        this.List = new ArrayList<Double>();
        for(int i=0; i<List.size(); ++i){
            this.List.add(List.get(i));
        }
    }
    
    public ArrayList<Double> getList(){
        return this.List;
    }
    
}
