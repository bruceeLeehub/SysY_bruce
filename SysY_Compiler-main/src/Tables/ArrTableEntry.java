package Tables;

import java.util.ArrayList;

public class ArrTableEntry {
    private final ArrayList<Integer> upper_Bounds;
    private final ArrayList<Integer> const_Array;

    public ArrTableEntry(ArrayList<Integer> upper_Bounds) {
        this.upper_Bounds = upper_Bounds;
        this.const_Array = null;
    }

    public ArrayList<Integer> getConst_Array() {
        return this.const_Array;
    }

    public ArrTableEntry(ArrayList<Integer> upper_Bounds, ArrayList<Integer> const_Array) {
        this.upper_Bounds = upper_Bounds;
        this.const_Array = const_Array;
    }

    public ArrayList<Integer> getUpper_Bounds() {
        return this.upper_Bounds;
    }
}