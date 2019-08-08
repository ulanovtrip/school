package com.tsystems.javaschool.tasks.calculator;
import static com.tsystems.javaschool.tasks.calculator.Operators.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    //setting the accuracy of the resulting value and rounding method
    static final MathContext MATH_CONTEXT = new MathContext(6, RoundingMode.HALF_UP);
    static final char DECIMAL_SEPARATOR = '.';
    static final char MINUS_SIGN = '-';

    static final Map<String, Operator> OPERATORS = new HashMap<>();

    static {
        OPERATORS.put(PLUS.name(), PLUS);
        OPERATORS.put(MINUS.name(), MINUS);
        OPERATORS.put(MULTIPLY.name(), MULTIPLY);
        OPERATORS.put(DIV.name(), DIV);
        OPERATORS.put(REMAINDER.name(), REMAINDER);
    }

    private String expression;
    private List<String> rpn = null;

    public String evaluate(String input) {
        // TODO: Implement the logic here

        //final Calculator calculator = new Calculator();
        this.expression = input;
        String result;
        try {
            result = evaluate().toPlainString();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    private BigDecimal evaluate() {
        Stack<Operand> stack = new Stack<>();

        for (final String token : getRPNExpression()) {
            if (OPERATORS.containsKey(token)) {
                final Operand argLeft = stack.pop();
                final Operand argRight = stack.pop();
                final Operator operator = OPERATORS.get(token);

                stack.push(new Operand() {
                    @Override
                    public BigDecimal eval() {
                        return operator.eval(argRight.eval(), argLeft.eval());
                    }
                });
            } else {
                stack.push(new Operand() {
                    @Override
                    public BigDecimal eval() {
                        return new BigDecimal(token, MATH_CONTEXT);
                    }
                });
            }
        }

        return stack.pop().eval().stripTrailingZeros();
    }

    private List<String> getRPNExpression() {
        if (rpn == null) {
            rpn = shuntingYard(this.expression);
            validateRPN(rpn);
        }
        return rpn;
    }

    private boolean isNumber(String st) {
        if (st.charAt(0) == MINUS_SIGN && st.length() == 1) return false;
        if (st.charAt(0) == '+' && st.length() == 1) return false;
        if (st.charAt(0) == 'e' ||  st.charAt(0) == 'E') return false;

        for (char ch : st.toCharArray()) {
            if (!Character.isDigit(ch) && ch != MINUS_SIGN
                    && ch != DECIMAL_SEPARATOR
                    && ch != 'e' && ch != 'E' && ch != '+')
                return false;
        }

        return true;
    }

    private void validateRPN(List<String> rpn) {
        final Stack<Integer> params = new Stack<>();
        int counter = 0;

        for (String token : rpn) {

            if (!params.isEmpty()) {
                params.set(params.size() - 1, params.peek() + 1);
            } else if (OPERATORS.containsKey(token)) {
                counter -= 2;
            }

            if (counter < 0) {
                throw new IllegalArgumentException("Too many operators or functions at: " + token);
            }

            counter++;
        }

        if (counter > 1) throw new IllegalArgumentException("Too many numbers or variables");
        else if (counter < 1) throw new IllegalArgumentException("Empty expression");
    }

    private List<String> shuntingYard(String expression) {
        final List<String> outputQueue = new ArrayList<>();
        final Stack<String> stack = new Stack<>();

        final Tokenizer tokenizer = new Tokenizer(expression);

        while (tokenizer.hasNext()) {
            String token = tokenizer.next();

            if (isNumber(token)) {

                outputQueue.add(token);

            } else if (Character.isLetter(token.charAt(0))) {

                stack.push(token);

            } else if (",".equals(token)) {

                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    outputQueue.add(stack.pop());
                }

            } else if (OPERATORS.containsKey(token)) {
                Operator operator = OPERATORS.get(token);
                String nextToken = stack.isEmpty() ? null : stack.peek();

                while (nextToken != null && OPERATORS.containsKey(nextToken) && ((
                        operator.isLeftAssoc() && operator.getPrecedence() <= OPERATORS.get(nextToken).getPrecedence())
                        || operator.getPrecedence() < OPERATORS.get(nextToken).getPrecedence())) {

                    outputQueue.add(stack.pop());
                    nextToken = stack.isEmpty() ? null : stack.peek();
                }

                stack.push(token);

            } else if ("(".equals(token)) {

                stack.push(token);

            } else if (")".equals(token)) {

                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    outputQueue.add(stack.pop());
                }

                if (stack.isEmpty()) {
                    throw new RuntimeException("Mismatched parentheses");
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            String element = stack.pop();

            if ("(".equals(element) || ")".equals(element)) {
                throw new RuntimeException("Mismatched parentheses");
            }

            if (!OPERATORS.containsKey(element)) {
                throw new RuntimeException("Unknown operator or function: " + element);
            }


            outputQueue.add(element);
        }

        return outputQueue;
    }

}
