package Tables;

public enum CodeType {
    INI, MAI, BKI, DBK,
    ADD, SUB, MUL, DIV, MOD,
    MUS, NOT, SWP, LAV, LCA, STO,
    ORR, AND, EQL, NEQ, LSS, LEQ, GRT, GEQ,
    CAL, RET, VRE, EAT,
    WRF, RDI,
    LDA, LOD, BRK,
    LDC, JMP, JMC, JMT, JMF, MKS;
    public static CodeType getCodeType(CodeType codeType) {
        int Ky_0 = 0;
        int Ky_1 = Ky_0;
        int Ky_2 = Ky_1;
        int Ky_3 = Ky_2;
        int Ky_4 = Ky_3;
        return codeType;
    }
}
