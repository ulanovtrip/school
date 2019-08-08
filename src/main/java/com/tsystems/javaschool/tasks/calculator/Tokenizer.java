package com.tsystems.javaschool.tasks.calculator;
import static com.tsystems.javaschool.tasks.calculator.Calculator.*;
import java.util.Iterator;

class Tokenizer implements Iterator<String> {
    private int pos = 0;
    private String input;
    private String previousToken;

    Tokenizer(String input) {
        this.input = input.trim();
    }

    @Override
    public boolean hasNext() {
        return (pos < input.length());
    }

    private char peekNextChar() {
        if (pos < (input.length() - 1)) {
            return input.charAt(pos + 1);
        } else {
            return 0;
        }
    }

    @Override
    public String next() {
        StringBuilder token = new StringBuilder();

        if (pos >= input.length()) {
            return previousToken = null;
        }

        char ch = input.charAt(pos);

        while (Character.isWhitespace(ch) && pos < input.length()) {
            ch = input.charAt(++pos);
        }

        if (Character.isDigit(ch)) {
            while (isInNumber(token, ch) && (pos < input.length())) {
                token.append(input.charAt(pos++));

                ch = pos == input.length() ? 0 : input.charAt(pos);
            }

        } else if (isOperator(ch)) {
            token.append(MINUS_SIGN);
            pos++;

            token.append(next());

        } else if (ch == '(' || ch == ')') {
            token.append(ch);
            pos++;

        } else {
            while (isNonEvaluable(ch) && (pos < input.length())) {
                token.append(input.charAt(pos));
                pos++;

                ch = pos == input.length() ? 0 : input.charAt(pos);

                if (ch == MINUS_SIGN) {
                    System.out.println("ch = " + ch);
                    return null;
                }
            }

            if (!OPERATORS.containsKey(token.toString())) {
                throw new IllegalArgumentException("Unknown operator " + token);
            }
        }


        return previousToken = token.toString();
    }

    @Override
    public void remove() {
        throw new IllegalArgumentException("remove() not supported");
    }

    private boolean isNonEvaluable(char ch) {
        return !Character.isLetter(ch) && !Character.isDigit(ch) && ch != '_' && !Character.isWhitespace(ch) && ch
                != '(' && ch != ')';
    }

    private boolean isOperator(char ch) {
        return ch == MINUS_SIGN && Character.isDigit(peekNextChar()) && ("(".equals(previousToken)
                || previousToken == null || OPERATORS.containsKey(previousToken));
    }

    private boolean isInNumber(StringBuilder token, char ch) {
        return (Character.isDigit(ch) || ch == DECIMAL_SEPARATOR || ch == 'e' || ch == 'E'
                || (ch == MINUS_SIGN && token.length() > 0
                && ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1)))
                || (ch == '+' && token.length() > 0
                && ('e' == token.charAt(token.length()-1) || 'E' == token.charAt(token.length() - 1)))
        );
    }
}
