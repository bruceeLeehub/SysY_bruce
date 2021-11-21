package NoneTerminal;

import ParcelType.My_DataType;
import Table.DataType;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class FuncType {
    public static String name = "<FuncType>";

    private RegKey regKey;
    public FuncType(RegKey regKey){
        this.regKey = regKey;
    }

    public RegKey getRegKey(){
        return this.regKey;
    }

    public static FuncType analyse(IdentifySymbol identifySymbol, My_DataType dataType) {
        Symbol sym = identifySymbol.get_CurrentSym();
        boolean judge = true;
        FuncType funcType = new FuncType(sym.getRegKey());
        judge &= (sym.getRegKey() == RegKey.VOIDTK || sym.getRegKey() == RegKey.INTTK);
        if (judge) {
            dataType.dataType = DataType.VOID_DATA;
            if (sym.getRegKey() == RegKey.INTTK) dataType.dataType = DataType.INT_DATA;
            identifySymbol.getASymbol();
        }

        if (judge) {
            identifySymbol.addStr(name);
        }
        return funcType;
    }
}
