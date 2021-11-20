package NoneTerminal;

import ParcelType.MyString;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

public class Ident {
    public static String name = "<Ident>";

    private String identName;

    public Ident(){
        this.identName = null;
    }
    public Ident(String identName){
        this.identName = identName;
    }

    public void setIdentName(String identName){
        this.identName = identName;
    }

    public String getIdentName(){
        return this.identName;
    }

    public static Ident analyse(IdentifySymbol identifySymbol, MyString name){
        Symbol sym = identifySymbol.getCurSym();
        Ident ident = new Ident();
        if(sym.getRegKey() == RegKey.IDENFR){
            name.string = sym.getIdentName();
            ident.setIdentName(name.string);
            identifySymbol.getASymbol();
            return ident;
        }else{
            return null;
        }
    }
}
