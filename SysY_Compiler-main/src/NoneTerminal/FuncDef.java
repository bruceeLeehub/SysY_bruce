package NoneTerminal;

import ParcelType.*;
import Table.DataType;
import Table.SymTable;
import Table.TableEntry;
import Tables.*;
import WordAnalyse.*;
import MyError.Error;

import java.util.ArrayList;

public class FuncDef {
    public final ArrayList<FuncFParam> funcFParamList = new ArrayList<>();
    public Block funcBlock = null;

    public static boolean InFuncDef = false;
    public static boolean isFunEnd = false;


    public FuncType funcType = null;
    public Ident ident = null;

    public static boolean funcBlockST = false;
    public static boolean haveReturnValue = false;
    public static String name_funcDef = "<FuncDef>";


    public void add_FuncFParam(FuncFParam funcFParam) {
        this.funcFParamList.add(funcFParam);
    }

    public static FuncDef analyse(IdentifySymbol identifySymbol) {
        isFunEnd = false;
        funcBlockST = true;

        MyString identName;
        My_DataType dataType;
        identName = new MyString();
        dataType = new My_DataType();

        ArrayList<TableEntry> paramList;
        paramList = new ArrayList<>();

        FuncDef funcDef;    // ast tree node
        funcDef = new FuncDef();    // ast tree node
        InFuncDef = true;

        FuncType funcType = FuncType.analyse(identifySymbol,dataType);
        funcDef.funcType = funcType;

        Ident ident = Ident.analyse(identifySymbol, identName);
        funcDef.ident = ident;
        Symbol preSymbol = identifySymbol.get_PreSym();
        Error.checkIfDupDef(true, preSymbol);// name Duplicated -- type b
        // record does it have a return value
        haveReturnValue = dataType.dataType == DataType.INT_DATA;
        // create symTable stack of the following func
        SymTable.create_NewTable();

        Symbol symbol = identifySymbol.get_CurrentSym();
        boolean judge = symbol.getRegKey() == RegKey.LPARENT;
        if (judge) {
            symbol = identifySymbol.getASymbol();
            RegKey regKey = symbol.getRegKey();
            boolean isRPARENT = (RegKey.RPARENT == regKey);
            boolean isLBRACE = (regKey == RegKey.LBRACE);
            if (isRPARENT || isLBRACE) {
                if (isLBRACE) {
                    // j: ')' needed
                    Symbol curSymbol = identifySymbol.get_CurrentSym();
                    RegKey regKey1 = curSymbol.getRegKey();
                    if (regKey1 != RegKey.RPARENT) {
                        Symbol Symbol2 = identifySymbol.get_PreSym();
                        int rowIdx = Symbol2.getRow_Idx();
                        Error.add_ErrorOutPut(rowIdx + " j");
                    }
                }
                else if (isRPARENT){
                    identifySymbol.getASymbol();
                }
                // insert funcDef into previous stmTable
                if (!CompUnit.isNameDuplicateDef) {
                    boolean isFunc = true;
                    String name = identName.string;
                    boolean isConst = false;
                    int dims = 0;
                    SymTable.insertTabEntryIntoPreTab(isFunc, name, isConst, dataType.dataType, dims, paramList);
                }
                else {
                    CompUnit.isNameDuplicateDef = false;
                }
                Block block = Block.analyse(identifySymbol);
                funcDef.funcBlock = block;
                // g: func have return value don't have return stmt in the end
                boolean noReturnError = FuncDef.haveReturnValue && !Block.hasReturnStmt;
                if (noReturnError) {
                    Symbol symbol_pre = identifySymbol.get_PreSym();
                    int rowidx = symbol_pre.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " g");
                }
            } else {
                judge = FuncFParams.analyse(identifySymbol, paramList, funcDef);
                // j: ')' needed
                Symbol curSymbol = identifySymbol.get_CurrentSym();
                RegKey regKey1 = curSymbol.getRegKey();
                boolean isRPARENT1 = (regKey1 == RegKey.RPARENT);
                if (isRPARENT1) {
                    identifySymbol.getASymbol();
                }
                else {
                    Error.add_ErrorOutPut(identifySymbol.get_PreSym().getRow_Idx() + " j");
                }
                // insert funcDef into previous stmTable
                if (CompUnit.isNameDuplicateDef) {
                    CompUnit.isNameDuplicateDef = false;
                }
                else {
                    boolean isFunc = true;
                    String name = identName.string;
                    boolean isConst = false;
                    int dims = 0;
                    SymTable.insertTabEntryIntoPreTab(isFunc, name, isConst, dataType.dataType, dims, paramList);
                }
                funcDef.funcBlock = (Block.analyse(identifySymbol));
                // -- g: func have return value don't have return stmt in the end
                boolean noReturnError = (FuncDef.haveReturnValue && !Block.hasReturnStmt);
                if (noReturnError) {
                    Symbol pre_Symbol = identifySymbol.get_PreSym();
                    int rowidx = pre_Symbol.getRow_Idx();
                    Error.add_ErrorOutPut(rowidx + " g");
                }
            }
        }
        InFuncDef = false;
        if (judge) {
            identifySymbol.addStr(name_funcDef);
        }
        return funcDef;
        // remove the current function table stack
        // SymTable.popOutterTable();
        // Block has done the remove job even when a table is built here
    }

    public FuncDef() {
    }

    public void genCode() {
        String name = ident.getId_Name();
        int paraSize = funcFParamList.size();
        int funcRef = ProgTable.insertProg_Entry(name,paraSize);
        Typ typ;
        RegKey regKey = this.funcType.getRegKey();
        boolean isVoid = regKey.equals(RegKey.VOIDTK);
        if (isVoid) {
            typ = Typ.VOID_TYP;
        }
        else {
            typ = Typ.INT_TYP;
        }

        name = ident.getId_Name();
        Obj objFunc = Obj.FUNC_OBJ;
        Typ type = typ;
        int dims = 0;
        int ref = funcRef;
        int level = Table.getCurLayer();
        int adr = Code.get_NextFreeRoom();
        Table.addTeToCurrentTable(name, objFunc, type, dims,ref, level, adr);
        Table.createANewLayer();
        for (FuncFParam parameter : this.funcFParamList) {
            parameter.genCode();
        }
        Tables.TableEntry tableEntry = Table.getFuncTableEntry(ident.getId_Name());
        this.funcBlock.genCode(ident.getId_Name(),tableEntry);
    }
}
