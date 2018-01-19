public class Node implements Comparable<Node>{
    
    public char Symbol;
    public int Count;
    public String Code;
    public Node left, right;
    
    public Node(){
        Symbol = '\0';
        Count = 0;
        Code = "";
        left = null;
        right = null;
    }
    
    @Override
    public int compareTo(Node n) {
        return Integer.compare(n.Count, this.Count);
    }
    
}
