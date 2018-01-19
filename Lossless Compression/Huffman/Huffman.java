
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Huffman {
    
    private ArrayList<Node> Symbols;
    
    public String CompressData(String Data){
        
        ArrayList<Node> Nodes = new ArrayList<>();
        
        for(int i=0; i<Data.length(); ++i){
            
            int index = getNodeIndex(Nodes, Data.charAt(i));
            
            if(index == -1){
                
                Node New = new Node();
                New.Symbol = Data.charAt(i);
                New.Count = 1;
                
                Nodes.add(New);
                
            } else {
                
                Nodes.get(index).Count++;
                
            }
            
        }
        
        while(Nodes.size() != 1){
            
            Collections.sort(Nodes);
            
            Node New = new Node();
            
            New.Count = Nodes.get(0).Count + Nodes.get(1).Count;
            New.left = Nodes.get(0);
            New.right = Nodes.get(1);
            
            Nodes.remove(0);
            Nodes.remove(0);
            
            Nodes.add(New);
            
        }
        
        Symbols = new ArrayList<>();
        
        putCodes(Nodes.get(0), "");
        
        String ret = "";
        
        for(int i=0; i<Symbols.size(); ++i)
            ret += Symbols.get(i).Symbol+"-"+Symbols.get(i).Code+"-";
        
        ret+="/";
        
        for(int i=0; i<Data.length(); ++i){
            
            int ind = getNodeIndex(Symbols, Data.charAt(i));
            
            ret += Symbols.get(ind).Code + "-";
            
        }
        
        return ret;
    }
    
    public String DeCompressData(String Data){
        
        String[] x = Data.split("/");
        String[] y = x[0].split("-");
        String[] codes = x[1].split("-");
        
        ArrayList<Node> Symbols = new ArrayList<>();
        
        for(int i=0; i<y.length; i+=2){
            
            Node New = new Node();
            New.Symbol = y[i].charAt(0);
            New.Code = y[i+1];
            
            Symbols.add(New);
            
        }
        
        String Original = "";
        
        for(int i=0; i<codes.length; ++i){
            
            int ind = getNodeIndex(Symbols, codes[i]);
            
            Original += Symbols.get(ind).Symbol;
            
        }
        
        return Original;
        
    }
    
    private void putCodes(Node Current, String Code){
        
        Current.Code = Code;
        
        if(Current.left == null && Current.right == null){
            Symbols.add(Current);
            return;
        }
        
        if(Current.left != null)
            putCodes(Current.left, Code+"0");
        if(Current.right != null)
            putCodes(Current.right, Code+"1");
        
    }
    
    private int getNodeIndex(ArrayList<Node> Nodes, String Code){
        
        for(int i=0; i<Nodes.size(); ++i){
            
            if(Nodes.get(i).Code.equals(Code))
                return i;
            
        }
        
        return -1;
    }
    
    private int getNodeIndex(ArrayList<Node> Nodes, char Symbol){
        
        for(int i=0; i<Nodes.size(); ++i){
            
            if(Nodes.get(i).Symbol == Symbol)
                return i;
            
        }
        
        return -1;
    }
    
}
