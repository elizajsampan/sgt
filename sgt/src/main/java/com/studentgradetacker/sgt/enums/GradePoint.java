package com.studentgradetacker.sgt.enums;

public enum GradePoint {

    A(1.0),
    B_PLUS(1.25),
    B(1.5),
    C_PLUS(1.75),
    C(2.0),
    D_PLUS(2.25),
    D(2.5),
    E_PLUS(2.75),
    E(3.0),
    F(5.0);

    private final double value;

    GradePoint(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static com.studentgradetacker.sgt.enums.GradePoint fromFinalGrade(double finalGrade) {
        if (finalGrade >= 99) {
            return A;
        } else if (finalGrade >= 96 && finalGrade <= 98) {
            return B_PLUS;
        } else if (finalGrade >= 93 && finalGrade <= 95) {
            return B;
        } else if (finalGrade >= 90 && finalGrade <= 92) {
            return C_PLUS;
        } else if (finalGrade >= 87 && finalGrade <= 89) {
            return C;
        } else if (finalGrade >= 84 && finalGrade <= 86) {
            return D_PLUS;
        } else if (finalGrade >= 81 && finalGrade <= 83) {
            return D;
        } else if (finalGrade >= 78 && finalGrade <= 80) {
            return E_PLUS;
        } else if (finalGrade >= 75 && finalGrade <= 77) {
            return E;
        } else {
            return F;
        }
    }

}
