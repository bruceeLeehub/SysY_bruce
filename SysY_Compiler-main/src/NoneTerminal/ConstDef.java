package NoneTerminal;


import MyError.Error;
import ParcelType.MyInt;
import ParcelType.MyString;
import Tables.ArrTable;
import Table.DataType;
import Table.SymTable;
import Tables.Obj;
import Tables.Table;
import Tables.Typ;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class ConstDef {
    public static String name = "<ConstDef>";

    private Ident ident;
    private ArrayList<ConstExp> constExpList;
    private ConstInitVal constInitVal;

    public ConstDef(){
        this.ident = null;
        this.constExpList = new ArrayList<>();
        this.constInitVal = null;
    }

    public void setIdent(Ident ident){
        this.ident = ident;
    }

    public void addConstExp(ConstExp constExp){
        this.constExpList.add(constExp);
    }

    public void setConstInitVal(ConstInitVal constInitVal){
        this.constInitVal = constInitVal;
    }

    public void genCode(){
        MyInt value = new MyInt();
        ArrayList<Integer> dimList = new ArrayList<>();
        ArrayList<Integer> constIniValue = new ArrayList<>();
        for(ConstExp constExp : constExpList){
            constExp.genCode(value);
            dimList.add(value.myInt);
        }

        this.constInitVal.genCode(constIniValue);
        if(constExpList.size() != 0){       // this is an array
            Table.addTeToCurrentTable(ident.getIdentName(), Obj.CONST_OBJ, Typ.INT_TYP, dimList.size(),
                    ArrTable.createAnEntry(dimList, constIniValue), Table.getCurLayer(), 0);
        }else{      // a normal const variable
            Table.addTeToCurrentTable(ident.getIdentName(), Obj.CONST_OBJ, Typ.INT_TYP, 0,
                    0, Table.getCurLayer(), constIniValue.get(0));
        }
    }

    public static ConstDef analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        int dims = 0;
        MyString identName = new MyString();
        ConstDef constDef = new ConstDef();


        constDef.setIdent(Ident.analyse(identifySymbol, identName));
        // ERROR: name Duplicated define -- type b
        Error.checkIfDupDef(false, identifySymbol.getPreSym());

        while (judge && identifySymbol.getCurSym().getRegKey() == RegKey.LBRACK) {
            dims++;
            identifySymbol.getASymbol();
            constDef.addConstExp(ConstExp.analyse(identifySymbol));
            // ERROR -- k: ']' needed
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RBRACK)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " k");
            else identifySymbol.getASymbol();

        }
        // create a const var
        if (CompUnit.isNameDupDef == false)
            SymTable.insertTabEntryIntoCurTab(false, identName.string, true, DataType.INT_DATA, dims);
        else CompUnit.isNameDupDef = false;

        if (judge) {
            judge &= identifySymbol.getCurSym().getRegKey() == RegKey.ASSIGN;
        }
        if (judge) {
            identifySymbol.getASymbol();
            constDef.setConstInitVal(ConstInitVal.analyse(identifySymbol, null));
        }

        if (judge) {
            identifySymbol.addStr(name);
        }
        return constDef;
    }
}
