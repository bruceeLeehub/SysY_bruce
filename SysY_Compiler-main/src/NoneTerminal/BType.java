package NoneTerminal;

import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class BType {
    public static String name = "<BType>";

    private RegKey regKey;

    public BType(){
        this.regKey = RegKey.INTTK;
    }

    public static BType analyse(IdentifySymbol identifySymbol){
        Symbol sym;
        BType bType = new BType();

        sym = identifySymbol.getCurSym();
        if(sym.getRegKey() == RegKey.INTTK){
            identifySymbol.getASymbol();
            return bType;
        }
        return null;
    }
}
