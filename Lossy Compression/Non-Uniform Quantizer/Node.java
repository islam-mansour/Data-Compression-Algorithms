package non.uniform;

import java.util.ArrayList;

public class Node {
    private double Average;
    private ArrayList<Integer> List;
    
    public Node(){
        this.List = new ArrayList<>();
    }
    public Node(ArrayList<Integer> List){
        this.List = new ArrayList<>();
        
        for(Integer x: List)
            this.List.add(x);
        
        CalculateAverage();
        
    }
    
    public Node(double Average){
        this.Average = Average;
        this.List = new ArrayList<Integer>();
    }
    
    public Node(double Average, ArrayList<Integer> List){
        this.Average = Average;
        this.List = new ArrayList<>();
        
        for(Integer x: List)
            this.List.add(x);
    }
    
    public void setAverage(double Average){
        this.Average = Average;
    }
    public void setList(ArrayList<Integer> List){
        for(Integer x: List)
            this.List.add(x);
    }
    
    public double getAverage(){
        return this.Average;
    }
    public ArrayList<Integer> getList(){
        return this.List;
    }
    
    private void CalculateAverage(){
        
        double sum = 0;
        
        for(Integer x: this.List)
            sum += x;
        
        setAverage(sum/this.List.size());
        
    }
    
}
