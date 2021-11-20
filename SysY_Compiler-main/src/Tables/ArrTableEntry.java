package Tables;

import java.util.ArrayList;

public class ArrTableEntry {
    private ArrayList<Integer> upperBounds;
    private ArrayList<Integer> constArray;

    public ArrTableEntry(ArrayList<Integer> upperBounds, ArrayList<Integer> constArray) {
        this.upperBounds = upperBounds;
        this.constArray = constArray;
    }

    public ArrTableEntry(ArrayList<Integer> upperBounds) {
        this.upperBounds = upperBounds;
        this.constArray = null;
    }

    public ArrayList<Integer> getUpperBounds() {
        return upperBounds;
    }

    public ArrayList<Integer> getConstArray() {
        return constArray;
    }
}