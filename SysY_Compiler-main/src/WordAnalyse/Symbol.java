package WordAnalyse;

public class Symbol {
    private RegKey regKey;
    private String stringValue;
    private int rowIdx;

    public Symbol(RegKey regKey, String stringValue, int rowIdx) {
        this.regKey = regKey;
        this.stringValue = stringValue;
        this.rowIdx = rowIdx;
    }

    public RegKey getRegKey(){
        return this.regKey;
    }

    public Symbol(String value, int rowIdx) {
        this.stringValue = value;
        this.regKey = RegKey.getRegKey(value);
        this.rowIdx = rowIdx;
    }

    public int getRowIdx(){
        return this.rowIdx;
    }

    public String getIdentName(){
        return this.stringValue;
    }

    @Override
    public String toString() {
        return this.regKey + " " + this.stringValue;
    }

}
