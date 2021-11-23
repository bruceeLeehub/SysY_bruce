package NoneTerminal;

import MyError.Error;
import ParcelType.*;
import Tables.CodeType;
import Tables.Table;
import Tables.Code;
import Table.SymTable;
import Table.TableEntry;
import WordAnalyse.*;

import java.util.ArrayList;

public class UnaryExp {
    public UnaryOp unaryOp = null;
    public UnaryExp unaryExp = null;
    public static String name_unaryExp = "<UnaryExp>";

    // three conditions
    public PrimaryExp primaryExp = null;
    public ArrayList<Exp> funcRParamList = new ArrayList<>();
    public Ident ident = null;

    public static boolean isMyFirst(Symbol sym) {
        RegKey regKey = sym.getRegKey();
        boolean isINTCON  = regKey == RegKey.INTCON;
        boolean UnaryOp_isMyFirst = UnaryOp.isMyFirst(sym);
        boolean PrimaryExp_isMyFirst = PrimaryExp.isMyFirst(sym);
        return isINTCON || UnaryOp_isMyFirst || PrimaryExp_isMyFirst;
    }

    public static UnaryExp analyse(IdentifySymbol identifySymbol) {
        boolean judge = true;
        UnaryExp unaryExp = new UnaryExp(); // ast Tree node
        Symbol sym = identifySymbol.get_CurrentSym();
        RegKey regKey = sym.getRegKey();
        boolean isLPARENT = regKey == RegKey.LPARENT;
        boolean isIDENFR = regKey == RegKey.IDENFR;
        boolean isPLUS_MINU_NOT = (regKey == RegKey.PLUS || regKey == RegKey.MINU || regKey == RegKey.NOT);
        boolean isINTCON = regKey == RegKey.INTCON;
        if (isLPARENT) {
            // -> '(' Exp ')'
            unaryExp.primaryExp = (PrimaryExp.analyse(identifySymbol));
        } else if (isPLUS_MINU_NOT) {
            unaryExp.unaryOp = (UnaryOp.analyse(identifySymbol));
            unaryExp.unaryExp = (UnaryExp.analyse(identifySymbol));
        } else if (isIDENFR) {
            TableEntry tempEntry;
            Symbol ident_Sym = sym;
            sym = identifySymbol.getASymbol();
            regKey = sym.getRegKey();
            if (regKey == RegKey.LPARENT) {
                // -> Ident '(' [ FuncRParams ] ')'函数调用
                String name = ident_Sym.get_IdentName();
                unaryExp.ident = (new Ident(name));

                My_Int actualParamCount = new My_Int();
                // c : check name undefined
                Error.checkNameUndefined(true, ident_Sym);
                // check RParams type
                tempEntry = SymTable.get_SymByNameInAllTable(true, name);
                if (FuncRParams.TypeCheck) {
                    FuncRParams.tbEntryModel.add(SymTable.createTableEntryModel(tempEntry, 0));
                    if (LVal.in_Dims == 0) {
                        FuncRParams.TypeCheck = false;
                    }
                }
                Symbol symbol = identifySymbol.getASymbol();
                regKey = symbol.getRegKey();
                if (regKey != RegKey.RPARENT) {
                    judge = FuncRParams.analyse(identifySymbol, actualParamCount, unaryExp);
                    if (judge) {
                        // j: ')' needed
                        Symbol curSymbol = identifySymbol.get_CurrentSym();
                        regKey = curSymbol.getRegKey();
                        if (regKey == RegKey.RPARENT) {
                            identifySymbol.getASymbol();
                        }
                        else {
                            Symbol preSymbol = identifySymbol.get_PreSym();
                            int rowidx = preSymbol.getRow_Idx();
                            Error.add_ErrorOutPut(rowidx + " j");
                        }
                    }
                } else {
                    identifySymbol.getASymbol();
                }
                //  check is number of param
                Error.checkParamNumMatched(ident_Sym, actualParamCount.my_Int);
            } else {
                // PrimaryExp
                //回退一格
                identifySymbol.spitSym(1);
                unaryExp.primaryExp = (PrimaryExp.analyse(identifySymbol));
            }

        } else if (isINTCON) {
            unaryExp.primaryExp = (PrimaryExp.analyse(identifySymbol));
        }

        if (judge) {
            identifySymbol.addStr(name_unaryExp);
        }

        return unaryExp;
    }

    public UnaryExp() {
    }

    public void add_FuncRParam(Exp exp) {
        this.funcRParamList.add(exp);
    }

    public void genCode(My_Int value) {
        if (value != null) {
            // calculate it now ,is const
            if (primaryExp != null) {
                primaryExp.genCode(value);
            }
            else if (unaryOp != null) {
                unaryExp.genCode(value);
                RegKey regKey = unaryOp.getUnaryOp();
                if (regKey.equals(RegKey.MINU)) {
                    value.my_Int = value.my_Int * -1;
                }
            }
        } else {
            // get it when running program, not a const
            if (primaryExp != null) {
                primaryExp.genCode(null);
            }
            else if (ident != null) {
                // func call
                String name = ident.getId_Name();
                int y = Table.getFuncTableEntry(name).get_Ref();
                Code.addCode(CodeType.MKS,y);
                for (Exp exp : this.funcRParamList) {
                    exp.genCode(null);
                }
                Code.addCode(CodeType.CAL);
            } else {
                unaryExp.genCode(null);
                RegKey regKey = unaryOp.getUnaryOp();
                if (regKey.equals(RegKey.NOT)) {
                    Code.addCode(CodeType.NOT);
                }
                else if (regKey.equals(RegKey.MINU)) {
                    Code.addCode(CodeType.MUS);
                }
            }
        }
    }
}
