package WordAnalyse;

public class Symbol {
    private final int row_Idx;
    private final RegKey regKey;
    private final String string_Value;

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.regKey);
        s.append(" ");
        s.append(this.string_Value);
        return s.toString();
    }

    public Symbol(RegKey regKey, String string_Value, int row_Idx) {
        this.regKey = regKey;
        this.string_Value = string_Value;
        this.row_Idx = row_Idx;
    }

    public Symbol(String value, int row_Idx) {
        this.string_Value = value;
        this.regKey = RegKey.getRegKey(value);
        this.row_Idx = row_Idx;
    }

    public int getRow_Idx(){
        return row_Idx;
    }

    public String get_IdentName(){
        return string_Value;
    }

    public RegKey getRegKey(){
        return regKey;
    }
}
