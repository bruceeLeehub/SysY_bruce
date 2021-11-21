package Table;

import java.util.ArrayList;
import java.util.TreeMap;

public class SymTable {
    private static int table_Ptr = -1;
    private static final ArrayList<AggTable> table_Stack = new ArrayList<>();

    public static void popOutterTable() {
        table_Stack.remove(table_Ptr);
        table_Ptr = table_Ptr - 1;
    }

    public static TableEntry get_SymByNameInAllTable(boolean isFunction, String idName) {
        int tablePtr = table_Ptr;
        TableEntry tableEntry = null;
        for(; tableEntry == null && tablePtr >= 0; tablePtr--){
            AggTable aggTable = table_Stack.get(tablePtr);
            TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunction);
            tableEntry = right_table.get(idName);
            ///tableEntry = table_Stack.get(tablePtr).get_RightTable(isFunction).get(idName);
        }
        return tableEntry;
    }


    public static boolean NameContainedInAllTable(boolean isFunc, String identName) {
        boolean haveName = false;
        int tablePtr = table_Ptr;
        for(; tablePtr >= 0; tablePtr--){
            AggTable aggTable = table_Stack.get(tablePtr);
            TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunc);
            haveName = right_table.containsKey(identName);
            if (haveName) {
                return true;
            }
            //haveName |= table_Stack.get(tablePtr).get_RightTable(isFunc).containsKey(identName);
        }
        return haveName;
    }

    public static void createNewTable() {
        table_Ptr = table_Ptr + 1;
        table_Stack.add(new AggTable());
    }

    public static TableEntry createTableEntryModel(TableEntry tempEntry, int dims) {
        boolean isConst = tempEntry.isConst();
        DataType dataType = tempEntry.getDType();
        int Dims = tempEntry.getDims();
        ArrayList<TableEntry> paramList = tempEntry.getParamList();
        TableEntry tableEntry = new TableEntry(isConst, dataType, Dims, paramList);
//        if(tempEntry.getDims() == dims){
//            tableEntry.setDims(0);
//        }else if(tempEntry.getDims() == 2 && dims == 1){
//            tableEntry.setDims(1);
//        }
        if(tempEntry.getDims() == 2 && dims == 1){
            tableEntry.setDims(1);
        }else if(tempEntry.getDims() == dims){
            tableEntry.setDims(0);
        }
        return tableEntry;
    }

    public static void add_Param(ArrayList<TableEntry> paramList, boolean isConst, DataType dataType, int dims) {
        TableEntry tableEntry = new TableEntry(isConst, dataType, dims, paramList);
        paramList.add(tableEntry);
    }

    public static boolean currentTableContainName(boolean isFunction, String name) {
        AggTable aggTable = table_Stack.get(table_Ptr);
        TreeMap<String,TableEntry> right_table= aggTable.get_RightTable(isFunction);
        boolean hasName = right_table.containsKey(name);
        return hasName;
        //return table_Stack.get(table_Ptr).get_RightTable(isFunction).containsKey(name);
    }

    public static void insertTabEntryIntoCurTab(boolean isFunction, String name, boolean isConst, DataType dataType, int dims) {
        AggTable aggTable = table_Stack.get(table_Ptr);
        TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunction);
        Table.TableEntry tableEntry = new TableEntry(isConst, dataType, dims);
        right_table.put(name,tableEntry);
        //table_Stack.get(table_Ptr).get_RightTable(isFunction).put(name, new TableEntry(isConst, dataType, dims));
    }

    public static void insertTabEntryIntoPreTab(boolean isFunction, String name, boolean isConst, DataType dType, int dims, ArrayList<TableEntry> paramList) {
        AggTable aggTable = table_Stack.get(table_Ptr - 1);
        TreeMap<String,TableEntry> right_table = aggTable.get_RightTable(isFunction);
        TableEntry tableEntry = new TableEntry(isConst, dType, dims, paramList);
        right_table.put(name,tableEntry);
        //table_Stack.get(table_Ptr - 1).get_RightTable(isFunction).put(name, new TableEntry(isConst, dType, dims, paramList));
    }

    public static int get_ExpectedParamsNum(String functionName){
        int stack_ptr = table_Ptr;
        int paramNum = 0;
        for(; stack_ptr >= 0; stack_ptr--){
            AggTable aggTable = table_Stack.get(stack_ptr);
            TreeMap<String,TableEntry> funcTable = aggTable.get_FuncTable();
            boolean hasFuncName = (funcTable.containsKey(functionName));
            if(hasFuncName){
                TableEntry tableEntry = funcTable.get(functionName);
                paramNum = tableEntry.getParamNums();
                return paramNum;
                //paramNum = table_Stack.get(stack_ptr).get_FuncTable().get(functionName).getParamNums();
                //break;
            }
        }
        return paramNum;
    }
}
