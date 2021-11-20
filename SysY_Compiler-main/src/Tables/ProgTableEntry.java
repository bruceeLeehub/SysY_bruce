package Tables;

public class ProgTableEntry {
    private final int paraSize;
    private final String name;

    public ProgTableEntry(String name, int paraSize){
        this.name = name;
        this.paraSize = paraSize;
    }

    public String getProName(){
        return this.name;
    }

    public int getParaSize(){
        return this.paraSize;
    }
}
