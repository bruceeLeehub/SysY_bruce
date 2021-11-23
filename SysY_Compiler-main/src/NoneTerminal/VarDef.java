package NoneTerminal;

import MyError.Error;
import ParcelType.*;
import Table.*;
import Tables.*;
import WordAnalyse.*;

import java.util.ArrayList;

public class VarDef {
    public Ident ident = null;
    public InitVal initVal = null;
    public static String name_varDef = "<VarDef>";
    public ArrayList<ConstExp> constExp_List = new ArrayList<>();

    public static VarDef analyse(IdentifySymbol identifySymbol) {
        VarDef varDef = new VarDef();
        MyString identName = new MyString();
        int dims = 0;
        Ident ident = Ident.analyse(identifySymbol,identName);
        varDef.ident = ident;
        // b: Duplicated name define
        Symbol preSymbol = identifySymbol.get_PreSym();
        Error.checkIfDupDef(false, preSymbol);

        while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
            identifySymbol.getASymbol();
            dims = dims + 1;
            ConstExp constExp = ConstExp.analyse(identifySymbol);
            varDef.constExp_List.add(constExp);
            // k: ']' missing
            Symbol symbol = identifySymbol.get_CurrentSym();
            RegKey regKey = symbol.getRegKey();
            if (regKey == RegKey.RBRACK) {
                identifySymbol.getASymbol();
            }
            else {
                Symbol sym = identifySymbol.get_PreSym();
                int rowidx = sym.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " k");
            }

        }
        // CREATE const_var
        if (CompUnit.isNameDuplicateDef) {
            CompUnit.isNameDuplicateDef = false;
        }
        else {
            SymTable.insertTabEntryIntoCurTab(false, identName.string, false, DataType.INT_DATA, dims);
        }

        Symbol curSymbol = identifySymbol.get_CurrentSym();
        RegKey regKey = curSymbol.getRegKey();
        boolean isASSIGN = regKey == RegKey.ASSIGN;
        if (isASSIGN) {
            identifySymbol.getASymbol();
            varDef.initVal = (InitVal.analyse(identifySymbol, null));
        }

        identifySymbol.addStr(name_varDef);
        return varDef;
    }

    public void buildDefaultIniValue(ArrayList<Integer> dimList, ArrayList<Integer> iniValue) {
        int totalNum = 1;
        if (dimList.size() == 0) {
            iniValue.add(0);
        } else {
            // this is an array
            for (int i : dimList) {
                totalNum = i * totalNum;
            }
            for (int i = 0; i < totalNum; i++) {
                iniValue.add(0);
            }
        }
    }

    public VarDef() {
    }

    public void genCode() {
        int sizeOfArray = 1;
        ArrayList<Integer> dim_List = new ArrayList<>();
        ArrayList<Integer> initValue = new ArrayList<>();
        My_Int value = new My_Int();
        for (ConstExp constExp : this.constExp_List) {
            constExp.genCode(value);
            sizeOfArray = sizeOfArray * value.my_Int;
            dim_List.add(value.my_Int);
        }

        if (this.initVal == null) {
            buildDefaultIniValue(dim_List, initValue);
        }
        else {
            this.initVal.genCode();
        }

        if (dim_List.size() == 0) {
            String name = ident.getId_Name();
            Obj obj_var = Obj.VAR_OBJ;
            Typ type = Typ.INT_TYP;
            int dims = dim_List.size();
            int ref = 0;
            int level = Table.getCurLayer();
            int addr = 1;
            Table.addTeToCurrentTable(name, obj_var, type, dims, ref, level, addr);
            if(this.initVal == null) {
                Code.addCode(CodeType.LDC, 0);
            }
        } else {    // this is an array
            String name = ident.getId_Name();
            Obj obj_var = Obj.VAR_OBJ;
            Typ type = Typ.INT_TYP;
            int dims = dim_List.size();
            int ref = ArrTable.createAnEntry(dim_List);
            int level = Table.getCurLayer();
            int addr = sizeOfArray;
            Table.addTeToCurrentTable(name, obj_var, type, dims, ref, level, addr);
            if(this.initVal == null){
                for(int i : initValue) {
                    Code.addCode(CodeType.LDC, 0);
                }  // 0 initial value
            }
        }
    }
}
