package NoneTerminal;

import WordAnalyse.*;

public class BType {
    public static String name_bType = "<---BType--->";

    private final RegKey regKey;

    public RegKey getRegKey() {
        return regKey;
    }

    public static BType analyse(IdentifySymbol identSym){
        Symbol sym = identSym.get_CurrentSym();
        BType bType = new BType();
        boolean isInt = (sym.getRegKey() == RegKey.INTTK);
        if(!isInt){
            return null;
        }
        identSym.getASymbol();
        return bType;
    }
    public BType(){
        regKey = RegKey.INTTK;
    }
}
