package NoneTerminal;

import MyError.Error;
import ParcelType.*;
import Table.*;
import Tables.ArrTable;
import Tables.Obj;
import Tables.Table;
import Tables.Typ;
import WordAnalyse.*;

import java.util.ArrayList;

public class FuncFParam {
    public Ident ident = null;
    public static String name = "<FuncFParam>";

    public BType bType = null;
    public ArrayList<ConstExp> constExp_List = null;

    public void genCode() {
        if (constExp_List == null) {       // var param
            String name = ident.getIdentName();
            Obj obj_var = Obj.VAR_OBJ;
            Typ type = Typ.INT_TYP;
            int dims = 0;
            int ref = 0;
            int level = Table.getCurLayer();
            int addr = 1;
            boolean isPara = true;
            Table.addTeToCurrentTable(name, obj_var, type, dims, ref,level, addr, isPara);
        } else {      // array param
            My_Int value = new My_Int();
            ArrayList<Integer> dimList = new ArrayList<>();
            dimList.add(0);    // it says that this is a param
            for (ConstExp constExp : this.constExp_List) {
                constExp.genCode(value);
                dimList.add(value.my_Int);
            }

            // array param ref need to be done until running time
            String name = ident.getIdentName();
            Obj obj_var = Obj.VAR_OBJ;
            Typ type = Typ.INT_TYP;
            int dims = dimList.size();
            int ref = ArrTable.createAnEntry(dimList);
            int level = Table.getCurLayer();
            int addr = 1;
            boolean isPara = true;
            Table.addTeToCurrentTable(name, obj_var, type,
                    dims, ref, level, addr, isPara);
        }

    }

    public void newConstExp() {
        this.constExp_List = new ArrayList<>();
    }

    public static FuncFParam analyse(IdentifySymbol identifySymbol, ArrayList<TableEntry> paramList) {
        Symbol sym;
        int dims = 0;
        MyString identName = new MyString();
        FuncFParam funcFParam = new FuncFParam();

        funcFParam.bType = (BType.analyse(identifySymbol));
        funcFParam.ident = (Ident.analyse(identifySymbol, identName));

        // ERROR: name Duplicated define -- type b
        Error.checkIfDupDef(false, identifySymbol.get_PreSym());
        sym = identifySymbol.get_CurrentSym();
        if (sym.getRegKey() == RegKey.LBRACK) {
            dims++; // dims++
            identifySymbol.getASymbol();
            funcFParam.newConstExp();
            //k: ']' needed
            if (identifySymbol.get_CurrentSym().getRegKey() != RegKey.RBRACK)
                Error.addErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " k");
            else identifySymbol.getASymbol();
            while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
                dims++; // dims++
                identifySymbol.getASymbol();
                funcFParam.constExp_List.add(ConstExp.analyse(identifySymbol));
                //funcFParam.addConstExp(ConstExp.analyse(identifySymbol));

                // k: ']' needed
                if (identifySymbol.get_CurrentSym().getRegKey() != RegKey.RBRACK)
                    Error.addErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " k");
                else identifySymbol.getASymbol();

            }
        }
        // as a param & as a local var of the following func
        SymTable.add_Param(paramList, false, DataType.INT_DATA, dims);
        SymTable.insertTabEntryIntoCurTab(false, identName.string, false, DataType.INT_DATA, dims);

        identifySymbol.addStr(name);
        return funcFParam;
    }

    public FuncFParam() {
    }
}
