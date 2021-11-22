package NoneTerminal;

import MyError.Error;
import ParcelType.My_Int;
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
        My_Int value = new My_Int();
        ArrayList<Integer> dimList = new ArrayList<>();
        ArrayList<Integer> iniValue = new ArrayList<>();
        int sizeOfArray = 1;
        for (ConstExp constExp : constExpList) {
            constExp.genCode(value);
            dimList.add(value.my_Int);
            sizeOfArray *= value.my_Int;
        }

        if (this.initVal != null)
            this.initVal.genCode();
        else
            buildDefaultIniValue(dimList, iniValue);

        if (dimList.size() != 0) {    // this is an array
            Table.addTeToCurrentTable(ident.getId_Name(), Obj.VAR_OBJ, Typ.INT_TYP,
                    dimList.size(), ArrTable.createAnEntry(dimList), Table.getCurLayer(), sizeOfArray);
            if(this.initVal == null){
                for(int k : iniValue)
                    Code.addCode(CodeType.LDC, 0);  // initial value with 0
            }   // else if iniValue.size != 0, it says that this initial procdure has been done
        } else {
            Table.addTeToCurrentTable(ident.getId_Name(), Obj.VAR_OBJ, Typ.INT_TYP,
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
        Error.checkIfDupDef(false, identifySymbol.get_PreSym());

        while (judge && identifySymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
            dims++;
            identifySymbol.getASymbol();
            varDef.addConstExp(ConstExp.analyse(identifySymbol));
            // ERROR -- k: ']' needed
            if (identifySymbol.get_CurrentSym().getRegKey() != RegKey.RBRACK)
                Error.addErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " k");
            else identifySymbol.getASymbol();

        }
        if (judge) {
            // create a const var
            if (CompUnit.isNameDuplicateDef == false)
                SymTable.insertTabEntryIntoCurTab(false, identName.string, false, DataType.INT_DATA, dims);
            else CompUnit.isNameDuplicateDef = false;

            if (identifySymbol.get_CurrentSym().getRegKey() == RegKey.ASSIGN) {
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
