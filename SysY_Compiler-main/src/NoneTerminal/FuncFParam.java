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
    public static String name_funcFParam = "<FuncFParam>";

    public BType bType = null;
    public ArrayList<ConstExp> constExp_List = null;

    public void genCode() {
        if (constExp_List == null) {       // var param
            String name = ident.getId_Name();
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
            String name = ident.getId_Name();
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

    public void new_ConstExp() {
        this.constExp_List = new ArrayList<>();
    }

    public static FuncFParam analyse(IdentifySymbol identifySymbol, ArrayList<TableEntry> paramList) {
        MyString identName = new MyString();

        FuncFParam funcFParam = new FuncFParam();
        funcFParam.bType = (BType.analyse(identifySymbol));
        funcFParam.ident = (Ident.analyse(identifySymbol, identName));

        // ERROR: name Duplicated define -- type b
        boolean isFunc = false;
        Symbol preSymbol = identifySymbol.get_PreSym();
        Error.checkIfDupDef(isFunc, preSymbol);
        int dims = 0;
        Symbol sym = identifySymbol.get_CurrentSym();
        if (sym.getRegKey() == RegKey.LBRACK) {
            identifySymbol.getASymbol();
            dims = dims + 1; // dims++
            funcFParam.new_ConstExp();
            //k: ']' needed
            Symbol curSymbol = identifySymbol.get_CurrentSym();
            RegKey regKey = curSymbol.getRegKey();
            boolean isRBRACK = regKey == RegKey.RBRACK;
            if (isRBRACK) {
                identifySymbol.getASymbol();
            }
            else {
                Symbol pre_Symbol = identifySymbol.get_PreSym();
                int rowidx = pre_Symbol.getRow_Idx();
                Error.add_ErrorOutPut(rowidx + " k");
            }
            while (identifySymbol.get_CurrentSym().getRegKey() == RegKey.LBRACK) {
                identifySymbol.getASymbol();
                dims = dims + 1; // dims++
                ConstExp constExp = ConstExp.analyse(identifySymbol);
                funcFParam.constExp_List.add(constExp);
                //funcFParam.addConstExp(ConstExp.analyse(identifySymbol));

                // k: ']' needed
                curSymbol = identifySymbol.get_CurrentSym();
                regKey = curSymbol.getRegKey();
                isRBRACK = regKey == RegKey.RBRACK;
                if (isRBRACK) {
                    identifySymbol.getASymbol();
                }
                else {
                    Symbol pre_Symbol = identifySymbol.get_PreSym();
                    int rowidx = pre_Symbol.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " k");
                }
            }
        }
        identifySymbol.addStr(name_funcFParam);
        boolean isConst = false;
        isFunc = false;
        SymTable.add_Param(paramList, isConst, DataType.INT_DATA, dims);
        SymTable.insertTabEntryIntoCurTab(isFunc, identName.string, isConst, DataType.INT_DATA, dims);
        return funcFParam;
    }

    public FuncFParam() {
    }
}
