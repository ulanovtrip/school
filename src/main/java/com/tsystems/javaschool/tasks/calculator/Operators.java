package com.tsystems.javaschool.tasks.calculator;

import java.math.BigDecimal;

interface Operators {

    Operator PLUS = new Operator("+", 20, true) {
        @Override
        public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
            return v1.add(v2, Calculator.MATH_CONTEXT);
        }
    };

    Operator MINUS = new Operator("-", 20, true) {
        @Override
        public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
            return v1.subtract(v2, Calculator.MATH_CONTEXT);
        }
    };

    Operator MULTIPLY = new Operator("*", 30, true) {
        @Override
        public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
            return v1.multiply(v2, Calculator.MATH_CONTEXT);
        }
    };

    Operator DIV = new Operator("/", 30, true) {
        @Override
        public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
            return v1.divide(v2, Calculator.MATH_CONTEXT);
        }
    };

    Operator REMAINDER = new Operator("%", 30, true) {
        @Override
        public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
            return v1.remainder(v2, Calculator.MATH_CONTEXT);
        }
    };
}
