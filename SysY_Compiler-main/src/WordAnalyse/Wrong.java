package WordAnalyse;

public class Wrong extends Exception{
    private final int row;
    public int wrongRow(){
        return this.row;
    }
    public Wrong(int row){
        this.row = row;
    }
}
