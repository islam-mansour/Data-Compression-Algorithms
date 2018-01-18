package non.uniform;

public class Object {
    
    private double Lower, Upper, Q, QInverse;
    
    public Object(double Lower, double Upper, double Q, double QInverse){
        this.Lower    = Lower;
        this.Upper    = Upper;
        this.Q        = Q;
        this.QInverse = QInverse;
    }
    
    public boolean inRange(double value){
        return (value >= this.Lower && value < this.Upper);
    }
    
    public double getLower(){
        return this.Lower;
    }
    
    public double getUpper(){
        return this.Upper;
    }
    
    public double getQ(){
        return this.Q;
    }
    
    public double getQInverse(){
        return this.QInverse;
    }
    
    
}
