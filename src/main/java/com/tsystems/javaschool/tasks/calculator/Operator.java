package com.tsystems.javaschool.tasks.calculator;

import java.math.BigDecimal;

abstract class Operator {

    private String operatorName;

    private int precedence;

    private boolean leftAssoc;

    Operator(String operatorName, int precedence, boolean leftAssoc) {
        this.operatorName = operatorName;
        this.precedence = precedence;
        this.leftAssoc = leftAssoc;
    }

    String name() {
        return operatorName;
    }

    int getPrecedence() {
        return precedence;
    }

    boolean isLeftAssoc() {
        return leftAssoc;
    }

    abstract BigDecimal eval(BigDecimal v1, BigDecimal v2);
}