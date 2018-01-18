package non.uniform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NonUniform {
    
    private ArrayList<Integer> List;
    private ArrayList<Node> Nodes;
    private int n_leafs;
    private ArrayList<Object> Table;
    int[][] pixels;

    public NonUniform(){
        List = new ArrayList<Integer>();
        Nodes = new ArrayList<Node>();
        n_leafs = 0;
    }
    public NonUniform(int bits, int[][] pixels) {
        
        this.n_leafs = (int)Math.pow(2, bits);
        this.pixels = pixels;
        
        Nodes = new ArrayList<Node>();
        List = new ArrayList<>();
        
        for(int[] x: pixels){
            for(int y: x){
                List.add(y);
            }
        }
        
        Node root = new Node(List);
        Nodes.add(root);
        
    }
    
    public void CompressImage() throws IOException{
        
        int n;
        while( (n=Nodes.size()) < n_leafs){
            
            for(int i=0; i<n; ++i){
                
                Node Current = Nodes.get(0);
                
                double splitLeft, splitRight;
                splitLeft  = Current.getAverage() - 1;
                splitRight = Current.getAverage() + 1;
                
                ArrayList<Integer> leftList, rightList;
                leftList = new ArrayList<Integer>();
                rightList = new ArrayList<Integer>();
                
                for(Integer x: Current.getList()){
                    if ( Math.abs(x - splitLeft) <= Math.abs(x - splitRight)){
                        leftList.add(x);
                    } else {
                        rightList.add(x);
                    }
                }
                
                Node leftNode, rightNode;
                leftNode  = new Node(leftList);
                rightNode = new Node(rightList);
                Nodes.remove(Current);
                Nodes.add(leftNode);
                Nodes.add(rightNode);
                
            }
            
        }
        
        
        for(int it=0; it<5; ++it){
            ArrayList<Node> tmpNodes = new ArrayList<Node>();

            CopyNodesAverages(tmpNodes);

            for(int j=0; j<List.size(); ++j){

                int currentValue = List.get(j);

                Node nearestNode = new Node();
                nearestNode = tmpNodes.get(0);

                for(int k=1; k<tmpNodes.size(); ++k){
                    if ( Math.abs(currentValue - tmpNodes.get(k).getAverage()) 
                         < Math.abs(currentValue - nearestNode.getAverage())){

                        nearestNode = tmpNodes.get(k);

                    }
                }
            }
            
            if (CompareWithNodes(tmpNodes) == true){
                break;
            } else {
                setNodes(tmpNodes);
            }
            
        }
        
        
        
        Table = new ArrayList<Object>();
        double low = 0;

        for(int i=0; i<Nodes.size(); ++i){
            
            if (i == Nodes.size()-1){
                
                double high;
                high = Math.pow(2, Math.log(Nodes.get(i).getAverage())/Math.log(2) + 1);
                Table.add(new Object(low, high, i, Nodes.get(i).getAverage()));
                
                break;
                
            }
            
            double avg = ( (Nodes.get(i).getAverage()+Nodes.get(i+1).getAverage()) ) / 2;
            Table.add(new Object(low, avg, i, Nodes.get(i).getAverage()));
            low = avg;
            
        }
        
        FileWriter out = new FileWriter("out.txt");
        
        for(int i=0; i<Table.size(); ++i){
            double lower = Table.get(i).getLower();
            double upper = Table.get(i).getUpper();
            double Q = Table.get(i).getQ();
            double QInverse = Table.get(i).getQInverse();
            if (i == Table.size()-1) {
                out.write(lower + "-" + upper + "-" + Q + "-" + QInverse);
            } else {
                out.write(lower + "-" + upper + "-" + Q + "-" + QInverse + "-");
            }
        }
        
        out.write("/");
        
        for(int i=0; i<List.size(); ++i){
            
            int j;
            for(j=0; j<Table.size(); ++j){
                
                if ( Table.get(j).inRange(List.get(i)) ){
                    break;
                }
            }
         
            
            if(i == List.size()-1){
                out.write(Table.get(j).getQ() + "");
            }
            else{
                out.write(Table.get(j).getQ() + "-");
            }
            
        }
        
        out.close();
        
    }
    
    public int[][] DeCompressImage() throws FileNotFoundException, IOException{
        
        //System.out.print(List.size() + " ");
        
        List.clear();
        Table.clear();
        
        BufferedReader in = new BufferedReader(new FileReader(new File("out.txt")));
        String Data = "";
        String line;
        while((line = in.readLine()) != null){
            Data += line;
        }
        in.close();
        
        String table,pix;
        String[] x = Data.split("/");
        table = x[0];
        pix = x[1];
        
        String[] T = table.split("-");
        String[] pxs = pix.split("-");
        
        for(int i=0; i<T.length; i+=4){
            double lower = Double.parseDouble(T[i]);
            double upper = Double.parseDouble(T[i+1]);
            double Q     = Double.parseDouble(T[i+2]);
            double QInverse = Double.parseDouble(T[i+3]);
            //System.out.println(Q);
            Table.add(new Object(lower, upper, Q, QInverse));
        }
        //System.out.println(pxs.length == pixels.length*pixels[0].length);
        for(int i=0; i<pxs.length; ++i){
            
            double px = Double.parseDouble(pxs[i]);
            
            for(int j=0; j<Table.size(); ++j){
                
                //System.out.println(px + " " + Table.get(j).getQ());
                if (px == Table.get(j).getQ()){
                    List.add((int)Table.get(j).getQInverse());
                    break;
                }
                
            }
          
        }
        //System.out.println(List.size() + " " + pixels.length*pixels[0].length);
        for(int i=0; i<List.size(); ++i){
            
            int I = i / this.pixels[0].length;
            int J = i % this.pixels[0].length;
            
            this.pixels[I][J] = List.get(i);
            
        }
        
        
        
        return this.pixels;
        
    }
    
    
    
    private void CopyNodesAverages(ArrayList<Node> tmpNodes){
        
        for(int i=0; i<Nodes.size(); ++i){
            tmpNodes.add(new Node(Nodes.get(i).getAverage()));
        }
        
    }
    
    private boolean CompareWithNodes(ArrayList<Node> tmpNodes){
        
        for(int i=0; i<tmpNodes.size(); ++i){
            
            Node n = getNode(tmpNodes.get(i).getAverage());
            
            if (!equalLists(n.getList(), tmpNodes.get(i).getList())){
                return false;
            }
            
        }
        
        return true;
            
    }
    
    private Node getNode(double Average){
        
        for(int i=0; i<Nodes.size(); ++i){
            if(Nodes.get(i).getAverage() == Average)
                return Nodes.get(i);
        }
        
        return null;
    }
    
    private boolean equalLists(ArrayList<Integer> L1, ArrayList<Integer> L2){
        
        if (L1.size() != L2.size())
            return false;
        
        for(int i=0; i<L1.size(); ++i){
            if (!L2.contains(L1.get(i))){
                return false;
            }
        }
        
        return true;
    }
    
    private void setNodes(ArrayList<Node> tmpNodes){
        
        Nodes.clear();
        
        for(int i=0; i<tmpNodes.size(); ++i)
            Nodes.add(tmpNodes.get(i));
        
    }
    
    
}
