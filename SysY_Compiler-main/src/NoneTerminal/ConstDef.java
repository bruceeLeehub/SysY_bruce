package NoneTerminal;


import MyError.Error;
import ParcelType.*;
import Tables.*;
import WordAnalyse.*;
import Table.DataType;
import Table.SymTable;

import java.util.ArrayList;

public class ConstDef {
    private final ArrayList<ConstExp> constExp_List;
    public ConstInitVal constInitVal = null;
    public static String name_constDef = "<ConstDef>";

    public Ident ident = null;


    public void add_ConstExp(ConstExp constExp){
        constExp_List.add(constExp);
    }

    public static ConstDef analyse(IdentifySymbol identSymbol) {
        MyString ident_Name = new MyString();
        int dims = 0;
        ConstDef constDef = new ConstDef();


        Ident ident = Ident.analyse(identSymbol, ident_Name);
        constDef.ident = ident;
        // ERROR: name Duplicated define -- type b
        Symbol symbol = identSymbol.get_PreSym();
        boolean isFunction = false;
        Error.checkIfDupDef(isFunction, symbol);

        while (identSymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
            identSymbol.getASymbol();
            dims = dims + 1;
            ConstExp constExp = ConstExp.analyse(identSymbol);
            constDef.add_ConstExp(constExp);
            //  ']' needed (error
            if (identSymbol.get_CurrentSym().getRegKey() == RegKey.RBRACK) {
                identSymbol.getASymbol();
            }
            else {
                Symbol symbol2 = identSymbol.get_PreSym();
                int index = symbol2.getRow_Idx();
                Error.add_ErrorOutPut(index + " k");
            }
        }

        // create a const var
        if (CompUnit.isNameDuplicateDef) {
            CompUnit.isNameDuplicateDef = false;
        }
        else {
            boolean isFunc = false;
            String name = ident_Name.string;
            boolean isConst = true;
            DataType dataType = DataType.INT_DATA;

            SymTable.insertTabEntryIntoCurTab(isFunc, name, isConst, dataType, dims);
        }

        Symbol symbol1 = identSymbol.get_CurrentSym();
        RegKey regKey = symbol1.getRegKey();
        boolean hasAssign = (regKey == RegKey.ASSIGN);
        if (hasAssign) {
            identSymbol.getASymbol();
            ConstInitVal constInitVal = ConstInitVal.analyse(identSymbol,null);
            constDef.constInitVal = constInitVal;
        }
        if (hasAssign) {
            identSymbol.addStr(name_constDef);
        }
        return constDef;
    }

    public void genCode(){
        My_Int value = new My_Int();
        ArrayList<Integer> dimList = new ArrayList<>();
        ArrayList<Integer> constIniValueList = new ArrayList<>();

        this.constInitVal.genCode(constIniValueList);
        for(ConstExp constExp : this.constExp_List){
            constExp.genCode(value);
            dimList.add(value.my_Int);
        }
        if(constExp_List.isEmpty()){      // A NORMAL CONST VAR
            String name = ident.getId_Name();
            Obj obj_const = Obj.CONST_OBJ;
            Typ type = Typ.INT_TYP;
            int dimSize = 0;
            int ref = 0;
            int level = Table.getCurLayer();
            int addr = constIniValueList.get(0);
            Table.addTeToCurrentTable(name, obj_const, type, dimSize, ref, level, addr);
        }else{       // AN ARRAY
            String name = ident.getId_Name();
            Obj obj_const = Obj.CONST_OBJ;
            Typ type = Typ.INT_TYP;
            int dimSize = dimList.size();
            int ref = ArrTable.createAnEntry(dimList,constIniValueList);
            int level = Table.getCurLayer();
            int addr = 0;
            Table.addTeToCurrentTable(name, obj_const, type, dimSize, ref, level, addr);
        }
    }

    public ConstDef(){
        this.constExp_List = new ArrayList<>();
    }
}
