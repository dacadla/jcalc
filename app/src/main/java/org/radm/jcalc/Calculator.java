package org.radm.jcalc;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator {
    public double calculate(String expression) {
        try {
            ArrayList<String> tokens = tokenize(expression);
            ArrayList<String> postfix = infixToPostfix(tokens);
            return evaluatePostfix(postfix);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0.0;
        }
    }

    public ArrayList<String> tokenize(String expression) {
        ArrayList<String> tokens = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }

        expression = sb.toString();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(Character.toString(c));
            } else if (Character.isDigit(c)) {
                int start = i;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    i++;
                }
                tokens.add(expression.substring(start, i));
                i--;
            }
        }

        return tokens;
    }

    public ArrayList<String> infixToPostfix(ArrayList<String> tokens) {
        ArrayList<String> postfix = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        for (String token : tokens) {
            if (token.equals("+") || token.equals("-")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("*") || token.equals("/")) {
                while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.add(stack.pop());
                }
                stack.pop(); // Discard the opening parenthesis
            } else {
                postfix.add(token);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    public double evaluatePostfix(ArrayList<String> postfix) {
        Stack<Double> stack = new Stack<Double>();

        for (String token : postfix) {
            if (token.equals("+")) {
                stack.push(stack.pop() + stack.pop());
            } else if (token.equals("-")) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                stack.push(operand1 - operand2);
            } else if (token.equals("*")) {
                stack.push(stack.pop() * stack.pop());
            } else if (token.equals("/")) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                stack.push(operand1 / operand2);
            } else {
                stack.push(Double.parseDouble(token));
            }
        }

        return stack.pop();
    }
}
