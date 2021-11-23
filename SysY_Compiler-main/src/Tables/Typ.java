package Tables;

public enum Typ {
    VOID_TYP, INT_TYP;

    public static Typ getTyp(Typ typ) {
        int Ky_0 = 0;
        int Ky_1 = Ky_0;
        int Ky_2 = Ky_1;
        int Ky_3 = Ky_2;
        int Ky_4 = Ky_3;
        return typ;
    }

    public static Typ getVoidTyp() {
        return VOID_TYP;
    }

    public static Typ getIntTyp() {
        return INT_TYP;
    }
}
