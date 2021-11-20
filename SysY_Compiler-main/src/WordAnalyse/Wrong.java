package WordAnalyse;

public class Wrong extends Exception{
    private int row;
    public Wrong(int row){
        this.row = row;
    }

    public int getRow(){
        return row;
    }
}
