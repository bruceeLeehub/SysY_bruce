package NoneTerminal;

import MyError.Error;
import ParcelType.MyInt;
import ParcelType.MyString;
import Table.DataType;
import Table.SymTable;
import Table.TableEntry;
import Tables.ArrTable;
import Tables.Obj;
import Tables.Table;
import Tables.Typ;
import WordAnalyse.IdentifySymbol;
import WordAnalyse.RegKey;
import WordAnalyse.Symbol;

import java.util.ArrayList;

public class FuncFParam {
    public static String name = "<FuncFParam>";

    private BType bType;
    private Ident ident;
    private ArrayList<ConstExp> constExpList;

    public FuncFParam() {
        this.ident = null;
        this.bType = null;
        this.constExpList = null;
    }

    public void setIdent(Ident ident) {
        this.ident = ident;
    }

    public void addConstExp(ConstExp constExp) {
        this.constExpList.add(constExp);
    }

    public void setBType(BType bType) {
        this.bType = bType;
    }

    public void newConstExp() {
        this.constExpList = new ArrayList<>();
    }

    public void genCode() {
        if (constExpList == null) {       // var param
            Table.addTeToCurrentTable(ident.getIdentName(), Obj.VAR_OBJ, Typ.INT_TYP, 0,
                    0, Table.getCurLayer(), 1, true);
        } else {      // array param

            ArrayList<Integer> dimList = new ArrayList<>();
            dimList.add(0);    // it says that this is a param
            MyInt value = new MyInt();
            for (ConstExp constExp : constExpList) {
                constExp.genCode(value);
                dimList.add(value.myInt);
            }

            // array param ref need to be done until running time
            Table.addTeToCurrentTable(ident.getIdentName(), Obj.VAR_OBJ, Typ.INT_TYP,
                    dimList.size(), ArrTable.createAnEntry(dimList), Table.getCurLayer(), 1, true);
        }

    }

    public static FuncFParam analyse(IdentifySymbol identifySymbol, ArrayList<TableEntry> paramList) {
        Symbol sym;
        boolean judge = true;
        int dims = 0;
        MyString identName = new MyString();
        FuncFParam funcFParam = new FuncFParam();

        funcFParam.setBType(BType.analyse(identifySymbol));
        if (judge) {
            funcFParam.setIdent(Ident.analyse(identifySymbol, identName));
        }
        // ERROR: name Duplicated define -- type b
        Error.checkIfDupDef(false, identifySymbol.getPreSym());
        if (judge) {
            sym = identifySymbol.getCurSym();
            if (sym.getRegKey() == RegKey.LBRACK) {
                dims++; // dims++
                identifySymbol.getASymbol();
                funcFParam.newConstExp();
                // ERROR -- k: ']' needed
                if (identifySymbol.getCurSym().getRegKey() != RegKey.RBRACK)
                    Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " k");
                else identifySymbol.getASymbol();
                while (judge && identifySymbol.getCurSym().getRegKey() == RegKey.LBRACK) {
                    dims++; // dims++
                    identifySymbol.getASymbol();
                    funcFParam.addConstExp(ConstExp.analyse(identifySymbol));
                    // ERROR -- k: ']' needed
                    if (identifySymbol.getCurSym().getRegKey() != RegKey.RBRACK)
                        Error.addErrorOutPut(identifySymbol.getPreSym().getRow_Idx() + " k");
                    else identifySymbol.getASymbol();

                }
            }
        }
        // as a param & as a local var of the following func
        SymTable.addParam(paramList, false, DataType.DT_INT, dims);
        SymTable.insTabEnIntoCurTab(false, identName.string, false, DataType.DT_INT, dims);

        if (judge) {
            identifySymbol.addStr(name);
        }
        return funcFParam;
    }
}
