package RNS;

import java.util.*;

/**
 * Статический класс, реализующий постфиксную нотацию и работу с выражениями.
 */
public final class PostfixNotation {
	private static ArrayList<String> operators = new ArrayList<>(Arrays.asList("(", ")", "+", "-", "*", "/", "^"));

	private PostfixNotation() { }

	/**
	 * Метод для определения приоритета операций.
	 * @param s Оператор.
	 * @return Возвращает приоритет.
	 */
	private static byte getPriority(String s) {
		switch (s) {
			case "(":
			case ")":
				return 0;
			case "+":
			case "-":
				return 1;
			case "*":
			case "/":
				return 2;
			case "^":
				return 3;
			default:
				return 4;
		}
	}

	/**
	 * Метод разделения исходной строки выражения.
	 * Выделяет из строки числа и операторы.
	 * @param input Исходная строка выражения.
	 * @return Возвращает выражение в виде массива.
	 */
	private static ArrayList<String> separate(String input) {
		ArrayList<String> res = new ArrayList<>();
		int pos = 0;
		while (pos < input.length()) {
			StringBuilder s = new StringBuilder("" + input.charAt(pos));
			if (!operators.contains(Character.toString(input.charAt(pos)))) {
				if (Character.isDigit(input.charAt(pos))) {
					for (int i = pos + 1; i < input.length() && Character.isDigit(input.charAt(i)); i++) {
						s.append(input.charAt(i));
					}
				}
			}
			pos += s.length();
			res.add(s.toString());
		}
		return res;
	}

	/**
	 * Метод преобразования выражения в постфиксную нотацию.
	 * @param input Исходная строка выражения.
	 * @return Возвращает преобразованное в постфиксную форму выражение в виде массива.
	 */
	private static ArrayList<String> convertToPostfixNotation(String input) {
		ArrayList<String> outputSeparated = new ArrayList<>();
		Stack<String> stack = new Stack<>();
		for (String c : separate(input)) {
			if (operators.contains(c)) {
				if (stack.size() > 0 && !c.equals("(")) {
					if (c.equals(")")) {
						String s = stack.pop();
						while (!s.equals("(")) {
							outputSeparated.add(s);
							s = stack.pop();
						}
					}
					else if (getPriority(c) > getPriority(stack.peek())) {
						stack.push(c);
					}
					else {
						while (stack.size() > 0 && getPriority(c) <= getPriority(stack.peek())) {
							outputSeparated.add(stack.pop());
						}
						stack.push(c);
					}
				}
				else {
					stack.push(c);
				}
			}
			else if (!isNullOrWhiteSpace(c)) {
				outputSeparated.add(c);
			}
		}
		if (stack.size() > 0) {
			Collections.reverse(stack);
			outputSeparated.addAll(stack);
		}
		return outputSeparated;
	}

	private static boolean isNullOrWhiteSpace(String value) {
		if (value == null) return true;
		for (int index = 0; index < value.length(); ++index) {
			if (!Character.isWhitespace(value.charAt(index))) return false;
		}
		return true;
	}

	/**
	 * Метод вычисления выражения, представленного в постфиксной форме.
	 * @param input Исходная строка выражения.
	 * @param modSystem Система модулей.
	 * @return
	 * @throws CalculateException
	 */
	public static Unit resultOfExpression(String input, ModSystem modSystem) throws CalculateException {
		Stack<Unit> unitsStack = new Stack<>();
		ArrayList<String> postfixNotation = convertToPostfixNotation(input);
		Collections.reverse(postfixNotation);
		Stack<String> operationsStack = new Stack<>();
		operationsStack.addAll(postfixNotation);
		String str = operationsStack.pop();
		while (operationsStack.size() >= 0) {
			if (!operators.contains(str)) {
				unitsStack.push(Convert.toRNS(Integer.valueOf(str), modSystem));
				if (operationsStack.size() != 0) str = operationsStack.pop();
				else break;
			}
			else {
				Unit b = unitsStack.pop();
				Unit a = unitsStack.pop();
				Unit res = new Unit(modSystem);
				switch (str) {
					case "+":
						res = Math.add(a, b);
						break;
					case "-":
						res = Math.subtract(a, b);
						break;
					case "*":
						res = Math.multiply(a, b);
						break;
					case "/":
						res = Math.divide(a, b);
						break;
				}
				unitsStack.push(res);
				if (operationsStack.size() > 0) str = operationsStack.pop();
				else break;
			}
		}
		return unitsStack.pop();
	}
}