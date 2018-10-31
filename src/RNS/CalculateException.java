package RNS;

/**
 * Класс-обёртка, используемый для конкретизации исключений, связанных с
 * возможными ошибками вычислений.
 */
public class CalculateException extends Exception {
	public CalculateException(String message) {
		super(message);
	}
}
