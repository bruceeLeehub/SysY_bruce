package Table;

import java.util.ArrayList;

public class TableEntry {
    private int dims;
    private ArrayList<TableEntry> paramsList;
    private final boolean isConst;
    private final DataType dataType;

    public int get_ParamSize(){
        return paramsList.size();
    }

    public boolean isConst(){
        return isConst;
    }

    public DataType get_DType(){
        return dataType;
    }

    public TableEntry(boolean isConst, DataType dataType, int dims, ArrayList<TableEntry> paramsList){
        this.paramsList = paramsList;
        this.isConst = isConst;
        this.dims = dims;
        this.dataType = dataType;
    }

    public int get_Dims(){
        return dims;
    }

    public void set_Dims(int _Dims){
        dims = _Dims;
    }

    public ArrayList<TableEntry> get_ParamsList(){
        return paramsList;
    }


    public TableEntry(boolean isConst, DataType dataType, int dims){
        this.paramsList = null;
        this.isConst = isConst;
        this.dims = dims;
        this.dataType = dataType;
    }
}
