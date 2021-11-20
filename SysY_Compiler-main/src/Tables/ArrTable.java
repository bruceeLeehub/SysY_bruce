package Tables;

import java.util.ArrayList;

public class ArrTable {
    private static ArrayList<ArrTableEntry> ArrTable;

    public static void createArrTable(){
        ArrTable = new ArrayList<>();
    }

    public static int getConstArrayValue(int addr, ArrayList<Integer> dimValue){
        int diff = 0;
        int pre = 1;
        ArrTableEntry arrTableEntry = ArrTable.get(addr);
        for(int i = arrTableEntry.getUpperBounds().size() - 1; i >=0 ; i--){
            diff += dimValue.get(i) * pre;
            pre = arrTableEntry.getUpperBounds().get(i);
        }
        return arrTableEntry.getConstArray().get(diff - 1);
    }

    public static ArrayList<ArrTableEntry> getArrTable(){
        return ArrTable;
    }

    public static int createAnEntry(ArrayList<Integer> upperBounds, ArrayList<Integer> constArray){
        ArrTable.add(new ArrTableEntry(upperBounds, constArray));
        return ArrTable.size() - 1;
    }

    public static int createAnEntry(ArrayList<Integer> upperBounds){
        ArrTable.add(new ArrTableEntry(upperBounds));
        return ArrTable.size() - 1;
    }

}
