
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class VectorQuantizer {
    
    private int n_leafs;
    private int[][] px;
    private ArrayList<Double> Pixels;
    private ArrayList<Block> Blocks;
    private ArrayList<Node> Nodes;
    private int blockRow;
    private int blockCol;
    private int bits;
    Node root;
    
    public VectorQuantizer(){
        this.n_leafs = 0;
        this.blockRow = 0;
        this.blockCol = 0;
        this.px = null;
        this.Pixels = new ArrayList<Double>();
        this.Blocks = new ArrayList<Block>();
        this.Nodes = new ArrayList<Node>();
        this.root = new Node();
        this.bits = 0;
    }
    
    public VectorQuantizer(int n_vectors,int blockRow ,int blockCol, int[][] px){
        this.bits = (int)Math.ceil(Math.log(n_vectors)/Math.log(2));
        this.n_leafs = n_vectors;
        this.px = px;
        this.blockRow = blockRow;
        this.blockCol = blockCol;
        this.Nodes = new ArrayList<Node>();
        
        this.Pixels = new ArrayList<Double>();
        for(int i=0; i<px.length; ++i)
        {
            for(int j=0; j<px[i].length; ++j)
            {
                Pixels.add((double)px[i][j]);
            }
        }
        
        this.Blocks = new ArrayList<Block>();
        
        for(int i=0; i<px.length; i+=blockRow)
        {
            for(int j=0; j<px[i].length; j+=blockCol)
            {
                Blocks.add(getBlock(i,j));
            }
        }
        
        root = new Node(Blocks);
        Nodes.add(root);
        
    }
    
    public void CompressImage(String path) throws IOException{
        
        int n;
        while((n=Nodes.size()) < n_leafs){
            for(int i=0; i<n; ++i){
                Node Current = Nodes.get(0);

                Block leftSibling  = getSiblingBlock(Current.getAverage(), -1);
                Block rightSibling = getSiblingBlock(Current.getAverage(), +1);
                
                Nodes.remove(Current);
                Nodes.add(new Node(leftSibling));
                Nodes.add(new Node(rightSibling));
                
            }
            
            splitBlocks(Nodes);
            
            
            for(int i=0; i<Nodes.size(); ++i)
                Nodes.get(i).CalculateAverage();
            
            
        }
        
        
        
        for(int it=0; it<5; ++it){
            
            ArrayList<Node> tmpNodes = new ArrayList<Node>();
            
            copyNodesAverages(tmpNodes);
            splitBlocks(tmpNodes);
            
            if(equalToNodes(tmpNodes)){
                break;
            } else {
                setNodes(tmpNodes);
            }
            
        }
        
        FileWriter out = new FileWriter(path);
        
        for(int i=0; i<n_leafs; ++i){
            
            out.write(getBinary(i, bits) + "-");
            for(int j=0; j<Nodes.get(i).getAverage().getList().size(); ++j){
                
                if (i==n_leafs-1 && j==Nodes.get(i).getAverage().getList().size()-1)
                    out.write(Nodes.get(i).getAverage().getList().get(j) + "");
                else
                    out.write(Nodes.get(i).getAverage().getList().get(j) + "-");
            }
            
        }
        
        out.write("/");
        
        for(int i=0; i<Blocks.size(); ++i){
            
            for(int j=0; j<Nodes.size(); ++j){
                
                if (inNode(Nodes.get(j), Blocks.get(i))){
                    if(i == Blocks.size()-1)
                        out.write(getBinary(j, bits) + "");
                    else
                        out.write(getBinary(j, bits) + "-");
                }
                
            }
            
        }
        
        
        out.close();
        
    }
    
    public int[][] deCompressImage(String path) throws FileNotFoundException, IOException{
        
        BufferedReader in = new BufferedReader(new FileReader(new File(path)));
        String Data = "";
        String line;
        while((line = in.readLine()) != null){
            Data += line;
        }
        in.close();
        
        String[] x = Data.split("/");
        String[] t = x[0].split("-");
        
        ArrayList<Pair> Table = new ArrayList<Pair>();
        for(int i=0; i<t.length; i+=(blockCol*blockRow)+1){
            String key = t[i];
            ArrayList<Double> list = new ArrayList<Double>();
            for(int j=i+1; j<=i+(blockCol*blockRow); ++j){
                list.add(Double.parseDouble(t[j]));
            }
            Block value = new Block(list);
            
            Table.add(new Pair(key, value));
            
        }
        
        String[] v = x[1].split("-");
        for(int i=0; i<v.length; ++i){
            for(int j=0;j<Table.size(); ++j){
                
                if(v[i].equals(Table.get(j).getKey())){
                    Blocks.set(i, Table.get(j).getValue());
                    break;
                }
                
            }
        }
        
        int k=0;
        for(int i=0; i<px.length; i+=blockRow)
        {
            for(int j=0; j<px[i].length; j+=blockCol)
            {
                putBlock(i,j,Blocks.get(k).getList());
                k++;
            }
        }
        
        return px;
        
    }
    
    private void putBlock(int I,int J,ArrayList<Double> list){
        int m=0;
        for(int i=I; i<I+this.blockRow; ++i)
        {
            for(int j=J; j<J+this.blockCol; ++j)
            {
                
                if (inPxs(i,j))
                {
                    px[i][j] = (int) Math.floor(list.get(m));
                    m++;
                }
            }
        }
    }
    
    
    private boolean equalBlocks(Block B1, Block B2){
        return (getDiff(B1, B2) == 0);
    }
    
    private boolean inNode(Node Current, Block B){
        for(int i=0; i<Current.getList().size(); ++i){
            if(equalBlocks(Current.getList().get(i), B)){
                return true;
            }
        }
        return false;
    }
    
    private String getBinary(int x, int n){
        
        String ret = "";
        
        while(x!=0){
            
            int r = x%2;
            if (r == 0)
                ret = "0" + ret;
            else
                ret = "1" + ret;
            x/=2;
        }
        while(ret.length() < n){
            ret = "0" + ret;
        }
        return ret;
    }
    
    private void setNodes(ArrayList<Node> tmpNodes){
    
        Nodes.clear();
        
        for(int i=0; i<tmpNodes.size(); ++i){
            
            Nodes.add( new Node(tmpNodes.get(i).getList()) );
            
        }
    
    }
    
    private boolean equalToNodes(ArrayList<Node> tmpNodes){
        
        for(int i=0; i<Nodes.size(); ++i){
            
            for(int j=0; j<Nodes.get(i).getList().size(); ++j){
                
                if(!tmpNodes.get(i).getList().contains(Nodes.get(i).getList().get(j))){
                    return false;
                }
                
            }
            
        }
        
        return true;
    }
    
    private void copyNodesAverages(ArrayList<Node> tmpNodes){
        
        for(int i=0; i<Nodes.size(); ++i){
            
            tmpNodes.add( new Node(Nodes.get(i).getAverage()) );
         
        }
        
    }
    
    private void splitBlocks(ArrayList<Node> N){
        
        for(int i=0; i<Blocks.size(); ++i){
                
            Node nearest = N.get(0);
            for(int j=1; j<N.size(); ++j){

                if(getDiff(Blocks.get(i),N.get(j).getAverage()) < getDiff(Blocks.get(i),nearest.getAverage())){
                    nearest = N.get(j);
                }

            }

            nearest.getList().add(Blocks.get(i));

        }
        
    }
    
    private double getDiff(Block B1, Block B2){
        
        double diff = 0;
        
        for(int i=0; i<B1.getList().size(); ++i){
            diff += Math.abs(B1.getList().get(i) - B2.getList().get(i));
        }
        
        return diff;
        
    }
    
    private Block getSiblingBlock(Block Current, int value){
        
        Block ret = new Block();
        
        for(int i=0; i<Current.getList().size(); ++i){
            
            ret.getList().add(Current.getList().get(i) + value);
            
        }
        
        return ret;
        
    }
    
    private boolean inPxs(int i,int j){
        return (i>=0&&i<px.length && j>=0&&j<px[0].length);
    }
    
    private Block getBlock(int I,int J){
        
        ArrayList<Double> List = new ArrayList<Double>();
        
        for(int i=I; i<I+this.blockRow; ++i)
        {
            for(int j=J; j<J+this.blockCol; ++j)
            {
                
                if (!inPxs(i,j))
                {
                    List.add(0.0);
                }
                else
                {
                    List.add((double)px[i][j]);
                }
            }
        }
        
        return (new Block(List));
        
    }
    
}
