package Tables;

public class ProgTableEntry {
    private final int paraSize;
    private final String name;

    public int getParaSize(){
        return paraSize;
    }

    public ProgTableEntry(String name, int paraSize){
        this.paraSize = paraSize;
        this.name = name;
    }

    public String getProgName(){
        return this.name;
    }

}
