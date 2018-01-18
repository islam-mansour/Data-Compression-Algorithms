
import java.util.ArrayList;

public class Node {
    
    private ArrayList<Block> List;
    private Block Average;
    
    public Node(){
        List = new ArrayList<Block>();
        Average = new Block();
    }
    
    public Node(Block Average){
        this.Average = new Block();
        this.setAverage(Average);
        this.List = new ArrayList<Block>();
    }
    
    public Node(ArrayList<Block> List){
        this.List = new ArrayList<Block>();
        Average = new Block();
        
        for(int i=0; i<List.size(); ++i){
            
            this.List.add(List.get(i));
        }
        
        CalculateAverage();
        
    }
    
    public void setAverage(Block Average){
        
        for(int i=0; i<Average.getList().size(); ++i){
            
            this.Average.getList().add(Average.getList().get(i));
            
        }
        
    }
    
    public ArrayList<Block> getList(){
        return this.List;
    }
    
    public Block getAverage(){
        return this.Average;
    }
    
    public void CalculateAverage(){
        
        ArrayList<Double> list = new ArrayList<Double>();
        for(int i=0; i<List.get(0).getList().size(); ++i)
            list.add(0.0);
        
        for(int i=0; i<List.size(); ++i){
            
            for(int j=0; j<List.get(i).getList().size(); ++j){
                
                list.set(j, list.get(j)+List.get(i).getList().get(j));
                
            }
            
        }
        
        for(int i=0; i<list.size(); ++i){
            list.set(i, list.get(i)/List.size());
        }
        
        this.Average = new Block(list);
        
    }
    
}
