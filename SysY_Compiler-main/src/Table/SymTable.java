package Table;

import java.util.ArrayList;

public class SymTable {
    private static ArrayList<AggTable> tableStack = new ArrayList<>();

    private static int tablePtr = -1;

    public static AggTable createNewTable() {
        AggTable newTable = new AggTable();
        tableStack.add(newTable);
        tablePtr++;
        return newTable;
    }

    public static void removeOuterTable() {
        tableStack.remove(tablePtr--);
    }

    public static void insTabEnIntoCurTab(boolean isFunc, String name, boolean isConst, DataType dType, int dims) {
        tableStack.get(tablePtr).getRightTable(isFunc).put(name, new TableEntry(isConst, dType, dims));
    }

    public static void insTabEnIntoPreTab(boolean isFunc, String name, boolean isConst, DataType dType, int dims, ArrayList<TableEntry> paramList) {
        tableStack.get(tablePtr - 1).getRightTable(isFunc).put(name, new TableEntry(isConst, dType, dims, paramList));
    }

    public static void addParam(ArrayList<TableEntry> paramList, boolean isConst, DataType dType, int dims) {
        paramList.add(new TableEntry(isConst, dType, dims, paramList));
    }

    public static boolean curTabContainsName(boolean isFunc, String name) {
        return tableStack.get(tablePtr).getRightTable(isFunc).containsKey(name);
    }

    public static int getParamsExpected(String funcName){
        int ret = 0;
        for(int stackPtr = tablePtr; stackPtr >= 0; stackPtr--){
            if(tableStack.get(stackPtr).getFuncTable().containsKey(funcName)){
                ret = tableStack.get(stackPtr).getFuncTable().get(funcName).getParamNums();
                break;
            }
        }
        return ret;
    }

    public static TableEntry getSymByNameFromAllTab(boolean isFunc, String identName) {
        TableEntry te = null;
        for(int ptr = tablePtr; ptr >= 0 && te == null; ptr--){
            te = tableStack.get(ptr).getRightTable(isFunc).get(identName);
        }
        return te;
    }


    public static TableEntry createTbEntryModel(TableEntry tmpEntry, int dims) {
        TableEntry te = new TableEntry(tmpEntry.isConst(), tmpEntry.getDType(), tmpEntry.getDims(), tmpEntry.getParamList());
        if(tmpEntry.getDims() == dims){
            te.setDims(0);
        }else if(tmpEntry.getDims() == 2 && dims == 1){
            te.setDims(1);
        }
        return te;
    }

    public static boolean allTabContainsName(boolean isFunc, String identName) {
        boolean ret = false;
        for(int ptr = tablePtr; ptr >= 0 && ret == false; ptr--){
            ret |= tableStack.get(ptr).getRightTable(isFunc).containsKey(identName);
        }
        return ret;
    }
}
