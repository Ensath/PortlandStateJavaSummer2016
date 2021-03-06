package edu.pdx.cs410J.whitlock;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BinaryOperator;

/**
 * This class is represents a <code>Student</code>.
 */
public class RPNCalculator {

  @VisibleForTesting
  List<Object> parseExpression(String expression) {
    List<Object> list = new ArrayList<>();

    if (expression.equals("")) {
      return list;
    }

    for (String token : expression.split(" ")) {
      list.add(parseToken(token));
    }

    return list;
  }

  private Object parseToken(String token) {
    Object value;

    switch (token) {
      case "+":
        return Operation.ADDITION;

      case "-":
        return Operation.SUBTRACT;

      case "*":
        return Operation.MULTIPLY;

      case "/":
        return Operation.DIVIDE;
    }

    try {
      value = Integer.parseInt(token);
    } catch (NumberFormatException ex) {
      throw new InvalidRPNExpressionException("Invalid expression: " + token);
    }
    return value;
  }

  int evaluate(String expression) {
    List lexemes = parseExpression(expression);
    if (lexemes.isEmpty()) {
      return 0;
    }

    Stack<Object> stack = new Stack<>();

    for (Object lexeme : lexemes) {
      if (lexeme instanceof Integer) {
        stack.push(lexeme);

      } else if (lexeme instanceof Operation) {
        Operation operation = (Operation) lexeme;
        int right = popInt(stack);
        int left = popInt(stack);
        int result = operation.evaluate(left, right);
        stack.push(result);
      }
    }

    return (Integer) stack.pop();
  }

  private int popInt(Stack stack) {
    return (Integer) stack.pop();
  }

  public static void main(String[] args) {
    System.err.println("Missing command line arguments");
    System.exit(1);
  }

  enum Operation {
    SUBTRACT((l, r) -> l - r),
    MULTIPLY((l, r) -> l * r),
    DIVIDE((l, r) -> l / r),
    ADDITION((l, r) -> l + r);

    private final BinaryOperator<Integer> operation;

    Operation(BinaryOperator<Integer> operation) {
      this.operation = operation;
    }

    public int evaluate(int left, int right) {
      return this.operation.apply(left, right);
    }
  }
}