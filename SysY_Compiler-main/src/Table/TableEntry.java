package Table;

import java.util.ArrayList;

public class TableEntry {
    private boolean isConst;
    private DataType dType;
    private int dims;
    private ArrayList<TableEntry> paramList;

    public TableEntry(boolean isConst, DataType dType, int dims){
        this.isConst = isConst;
        this.dType = dType;
        this.dims = dims;
        this.paramList = null;
    }

    public TableEntry(boolean isConst, DataType dType, int dims, ArrayList<TableEntry> paramList){
        this.isConst = isConst;
        this.dType = dType;
        this.dims = dims;
        this.paramList = paramList;
    }

    public boolean isConst(){
        return this.isConst;
    }

    public DataType getDType(){
        return this.dType;
    }

    public int getDims(){
        return this.dims;
    }

    public void setDims(int n){
        this.dims = n;
    }

    public ArrayList<TableEntry> getParamList(){
        return this.paramList;
    }

    public int getParamNums(){
        return paramList.size();
    }
}
