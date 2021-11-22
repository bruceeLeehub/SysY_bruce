package NoneTerminal;

import ParcelType.*;
import WordAnalyse.*;

public class Ident {
    private String id_Name = null;
    public static String name_ident = "<---Ident--->";

    public Ident(String id_Name){
        this.id_Name = id_Name;
    }

    public static Ident analyse(IdentifySymbol identifySymbol, MyString name){
        Symbol symbol = identifySymbol.get_CurrentSym();
        RegKey regKey = symbol.getRegKey();
        boolean isIdent = (regKey == RegKey.IDENFR);
        if(isIdent){
            Ident ident = new Ident();
            name.string = symbol.get_IdentName();
            ident.id_Name = (name.string);
            identifySymbol.getASymbol();
            return ident;
        }else{
            return null;
        }
    }
    public Ident(){
    }

    public String getId_Name(){
        return id_Name;
    }
}
