package WordAnalyse;

import java.util.ArrayList;

public class IdentifySymbol {
    private int cur_Ptr;
    private int spit_Num;
    private final ArrayList<String> output;
    private int add_Times;
    private final ArrayList<Symbol> symbolsRecord;
    private final SourceCode source_Code;

    public boolean isEndLine() {
        Symbol symbol = this.getASymbol();
        if (symbol != null) {
            this.spitSym(1);
            return false;
        } else {
            return true;
        }
    }

    public Symbol getNearest_PreIdent() {
//        Symbol ret_Sym = null;
//        int pre_Ptr = cur_Ptr - 2; // p r e
//        while(pre_Ptr >= 0 && ret_Sym == null){
//            ret_Sym = this.symbolsRecord.get(pre_Ptr);
//            if(ret_Sym.getRegKey() == RegKey.IDENFR) {
//                return ret_Sym;
//            }
//            pre_Ptr = pre_Ptr - 1;
//        }
//        return ret_Sym;
        int pre_ptr = cur_Ptr - 2; // pre
        Symbol ret_Sym = null;
        while(pre_ptr >= 0 && ret_Sym == null){
            boolean isID = (this.symbolsRecord.get(pre_ptr).getRegKey() == RegKey.IDENFR);
            if(isID)
                ret_Sym = this.symbolsRecord.get(pre_ptr);
            pre_ptr = pre_ptr - 1;
        }
        return ret_Sym;
    }

    public Symbol extra_IdentOrKeyword(SourceCode code) {
        StringBuilder stringValue = new StringBuilder();
        char cur_Char;
        while (!code.isEnd()) {
            cur_Char = code.getNext_Char();
            boolean isIdOrKey = (Character.isDigit(cur_Char) || Character.isAlphabetic(cur_Char) || cur_Char == '_');
            if (isIdOrKey) {
                stringValue.append(cur_Char);
            }
            else {
                break;
            }
        }
        String s = stringValue.toString();
        code.back_Ward();
        return new Symbol(s, code.get_RowIdx());
    }


    public Symbol get_PreSym(){ return symbolsRecord.get(cur_Ptr - 2); }

    public void add_Times(int i) {
        this.add_Times = this.add_Times + i;
    }

    public void addStr(String s) {
        this.add_Times = this.add_Times + 1;
        // this.output.add(this.curPtr + this.add_Times - 2, s);
    }

    public void spitSym(int spitNum) {
        this.cur_Ptr = this.cur_Ptr - spitNum;
        this.spit_Num = this.spit_Num + spitNum;
    }




    private Symbol extra_Single(SourceCode code) throws Wrong {
        char curCode = code.getNext_Char();
        boolean isSingle = (curCode == '+' || curCode == '-' || curCode == '*' ||
                curCode == '%' || curCode == ';' || curCode == ',' || curCode == '(' ||
                curCode == ')' || curCode == '[' || curCode == ']' || curCode == '{' || curCode == '}');
        if (!isSingle) {
            throw new Wrong(code.get_RowIdx());
        } else {
            String s = String.valueOf(curCode);
            return new Symbol(s, code.get_RowIdx());
        }
    }

    private Symbol extra_Even(SourceCode code) throws Wrong {
        char curCode = code.getNext_Char();
        StringBuilder stringValue = new StringBuilder();
        stringValue.append(curCode);

        if (code.isEnd()) {
            if (curCode == '&' || curCode == '|') {////////////////////////////////////////
                throw new Wrong(code.get_RowIdx());
            } else {
                String s = stringValue.toString();
                return new Symbol(s, code.get_RowIdx());
            }
        }

        char curCode_2 = code.getNext_Char();
        if (curCode == '&' || curCode == '|') {
            if (curCode == curCode_2) {
                stringValue.append(curCode);
            } else {
                code.back_Ward();
            }
            String s = stringValue.toString();
            return new Symbol(s, code.get_RowIdx());
        } else {
            if (curCode_2 == '=') {
                stringValue.append(curCode_2);
            } else {
                code.back_Ward();
            }
            String s = stringValue.toString();
            return new Symbol(s, code.get_RowIdx());
        }
    }


    private Symbol extra_Num(SourceCode code) {
        StringBuilder stringValue = new StringBuilder();
        while (!code.isEnd()) {
            char cur_Char = code.getNext_Char();
            if (Character.isDigit(cur_Char)) {
                stringValue.append(cur_Char);
            } else {
                break;
            }
        }
        String s = stringValue.toString();
        code.back_Ward();
        return new Symbol(RegKey.INTCON, s, code.get_RowIdx());
    }

    private Symbol extra_ComOrDiv(SourceCode code) {
        char curChar;
        curChar = code.getNext_Char();
        Symbol symbol = null;
        if (!code.isEnd()) {
            curChar = code.getNext_Char();
        } else {
            return new Symbol("/", code.get_RowIdx());
        }

        if (curChar == '*') {
            code.jumpOverMulLineComment();
        } else if (curChar == '/') {
            code.jumpOverLineComments();
        } else {
            code.back_Ward();
            symbol = new Symbol("/", code.get_RowIdx());
        }

        return symbol;
    }

    private Symbol extra_FString(SourceCode code) throws Wrong {
        char curCode = code.getNext_Char();
        StringBuilder value = new StringBuilder("\"");

        while (!code.isEnd()) {
            curCode = code.getNext_Char();
            value.append(curCode);
            if(curCode == '\"'){
                break;
            }
        }
        String s = value.toString();
        int rowIdx = code.get_RowIdx();
        Symbol ret = new Symbol(RegKey.STRCON,s,rowIdx);
        return ret;
    }
    public Symbol getASymbol() {
        if (this.spit_Num != 0) {
            this.spit_Num -= 1;
            Symbol symbol = this.symbolsRecord.get(this.cur_Ptr);
            this.cur_Ptr += 1;
            return symbol;
        } else {
            SourceCode code = this.source_Code;
            Symbol symbol = null;
            char curChar;
            while (symbol == null && !code.isEnd()) {
                curChar = code.getNext_Char();
                boolean isSpace = (curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t');
                if (isSpace) continue;
                code.back_Ward();
                try {
                    if (curChar == '_' || Character.isAlphabetic(curChar)) {
                        symbol = extra_IdentOrKeyword(code);
                    } else if (curChar == '&' || curChar == '|' ||
                            curChar == '!' || curChar == '<' ||
                            curChar == '>' || curChar == '=') {
                        symbol = extra_Even(code);
                    } else if (curChar == '/') {
                        symbol = extra_ComOrDiv(code);
                    } else if (Character.isDigit(curChar)) {
                        symbol = extra_Num(code);
                    } else if (curChar == '\"') {
                        symbol = extra_FString(code);
                    } else {
                        symbol = extra_Single(code);
                    }
                } catch (Wrong e) {
                    int row = e.wrongRow();
                    System.out.println("error in line " + row);
                }
            }
            if (symbol != null) {
                this.cur_Ptr += 1;
                this.symbolsRecord.add(symbol);
                // this.output.add(this.curPtr + this.add_Times - 1, symbol.toString());
            }
            return symbol;
        }
    }

    public ArrayList<Symbol> getSymbols() {
        return symbolsRecord;
    }

    public Symbol get_CurrentSym() {
        return symbolsRecord.get(cur_Ptr - 1);
    }

    public IdentifySymbol(ArrayList<Symbol> symbolsRecord, SourceCode source_Code, ArrayList<String> output) {
        this.symbolsRecord = symbolsRecord;
        this.cur_Ptr = 0;
        this.source_Code = source_Code;
        this.spit_Num = 0;
        this.output = output;
        this.add_Times = 0;
    }
}
