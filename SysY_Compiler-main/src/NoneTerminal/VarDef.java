package NoneTerminal;

import MyError.Error;
import ParcelType.MyInt;
import ParcelType.MyString;
import Table.*;
import Tables.*;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class VarDef {
    public static String name = "<VarDef>";

    private Ident ident;
    private ArrayList<ConstExp> constExpList;
    private InitVal initVal;

    public VarDef() {
        this.ident = null;
        this.constExpList = new ArrayList<>();
        this.initVal = null;
    }

    public void setIdent(Ident ident) {
        this.ident = ident;
    }

    public void addConstExp(ConstExp constExp) {
        this.constExpList.add(constExp);
    }

    public void setInitVal(InitVal initVal) {
        this.initVal = initVal;
    }

    public void buildDefaultIniValue(ArrayList<Integer> dimList, ArrayList<Integer> iniValue) {
        int totalNum = 1;
        if (dimList.size() != 0) {    // this is an array
            for (int i : dimList)
                totalNum *= i;
            for (int i = 0; i < totalNum; i++)
                iniValue.add(0);
        } else {
            iniValue.add(0);
        }
    }

    public void genCode() {
        MyInt value = new MyInt();
        ArrayList<Integer> dimList = new ArrayList<>();
        ArrayList<Integer> iniValue = new ArrayList<>();
        int sizeOfArray = 1;
        for (ConstExp constExp : constExpList) {
            constExp.genCode(value);
            dimList.add(value.myInt);
            sizeOfArray *= value.myInt;
        }

        if (this.initVal != null)
            this.initVal.genCode();
        else
            buildDefaultIniValue(dimList, iniValue);

        if (dimList.size() != 0) {    // this is an array
            Table.addTeToCurrentTable(ident.getIdentName(), Obj.VAR_OBJ, Typ.INT_TYP,
                    dimList.size(), ArrTable.createAnEntry(dimList), Table.getCurLayer(), sizeOfArray);
            if(this.initVal == null){
                for(int k : iniValue)
                    Code.addCode(CodeType.LDC, 0);  // initial value with 0
            }   // else if iniValue.size != 0, it says that this initial procdure has been done
        } else {
            Table.addTeToCurrentTable(ident.getIdentName(), Obj.VAR_OBJ, Typ.INT_TYP,
                    dimList.size(), 0, Table.getCurLayer(), 1);
            if(this.initVal == null)
                Code.addCode(CodeType.LDC, 0);
        }
    }


    public static VarDef analyse(IdentifySymbol identifySymbol) {
        Symbol sym;
        boolean judge = true;
        int dims = 0;
        MyString identName = new MyString();
        VarDef varDef = new VarDef();

        varDef.setIdent(Ident.analyse(identifySymbol, identName));
        // ERROR: name Duplicated define -- type b
        Error.checkIfDupDef(false, identifySymbol.getPreSym());

        while (judge && identifySymbol.getCurSym().getRegKey() == RegKey.LBRACK) {
            dims++;
            identifySymbol.getASymbol();
            varDef.addConstExp(ConstExp.analyse(identifySymbol));
            // ERROR -- k: ']' needed
            if (identifySymbol.getCurSym().getRegKey() != RegKey.RBRACK)
                Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " k");
            else identifySymbol.getASymbol();

        }
        if (judge) {
            // create a const var
            if (CompUnit.isNameDupDef == false)
                SymTable.insTabEnIntoCurTab(false, identName.string, false, DataType.DT_INT, dims);
            else CompUnit.isNameDupDef = false;

            if (identifySymbol.getCurSym().getRegKey() == RegKey.ASSIGN) {
                identifySymbol.getASymbol();
                varDef.setInitVal(InitVal.analyse(identifySymbol, null));
            }
        }

        if (judge) {
            identifySymbol.addStr(name);
        }
        return varDef;
    }
}
