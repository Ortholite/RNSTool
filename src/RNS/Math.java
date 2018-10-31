package RNS;

@FunctionalInterface
interface ICalculable {
	int resultOf(int...params) throws CalculateException;
}

/**
 * Статический класс, реализующий математические операции и алгоритмы.
 */
public final class Math {
	private Math() { }

	private static Unit calculate(Unit unit1, Unit unit2, ICalculable action) throws CalculateException {
		ModSystem modSystem = new ModSystem(unit1.getModSystem());
		int[] unit = new int[modSystem.getLength()];
		for (int i = 0; i < modSystem.getLength(); i++)
			unit[i] = Math.mod(action.resultOf(unit1.getSystem()[i], unit2.getSystem()[i], modSystem.getSystem()[i]), modSystem.getSystem()[i]);
		return new Unit(unit, modSystem);
	}

	public static Unit add(Unit unit1, Unit unit2) throws CalculateException {
		return calculate(unit1, unit2, (x) -> x[0] + x[1]);
	}

	public static Unit subtract(Unit unit1, Unit unit2) throws CalculateException {
		return calculate(unit1, unit2, (x) -> x[0] - x[1]);
	}

	public static Unit multiply(Unit unit1, Unit unit2) throws CalculateException {
		return calculate(unit1, unit2, (x) -> x[0] * x[1]);
	}

	public static Unit divide(Unit unit1, Unit unit2) throws CalculateException {
		//TODO: Пересмотреть проверку на деление нацело
		int number1 = 0;
		int number2 = 0;
		try {
			number1 = Convert.toPositionalSystem(unit1);
			number2 = Convert.toPositionalSystem(unit2);
		} catch (Exception ex) {
		}
		if (mod(number1, number2) != 0)
			throw new CalculateException("Деление не является целочисленным.");
		return calculate(unit1, unit2, (x) -> {
			if (ModSystem.isPairwiseSimple(x[1], unit1.getModSystem()))
				return x[0] * invNum(x[1], x[2]);
			else throw new CalculateException("Делитель невзаимнопростой с набором модулей.");
		});
	}

	static int mod(int num, int divider) {
		num %= divider;
		if (num < 0)
			num += divider;
		return num;
	}

	/**
	 * Алгоритм Евклида для нахождения НОД.
	 * @param a Целое число.
	 * @param b Целое число.
	 * @return Возвращает наибольший общий делитель для целых числел a и b.
	 */
	static int GCD (int a, int b) {
		while (b != 0)
			b = a % (a = b);
		return a < 0 ? -a : a;
	}

	/**
	 * Расширенный алгоритм Евклида для нахождения НОД.
	 * @param a Целое число.
	 * @param b Целое число.
	 * @return Возвращает массив, где [0]-элемент является НОД, [1] и [2] - коэффициенты Безу.
	 */
	private static int[] GCDExtended(int a, int b) {
		int res[] = new int[3]; // d, x, y
		if (b == 0) {
			res[0] = a;
			res[1] = 1;
			res[2] = 0;
			return res;
		}
		res = GCDExtended(b, a % b);
		int s = res[2];
		res[2] = res[1] - (a / b) * res[2];
		res[1] = s;
		return res;
	}

	/**
	 * Метод, вычисляющий мультипликативно обраное к числу по модулю.
	 * @param number Исходное число.
	 * @param mod Модуль, по которому идёт вычисление.
	 * @return Возвращает мультипликативно обраное.
	 * @throws CalculateException Выбрасывает исключение, если мультипликативно обратное не имеет решения.
	 */
	static int invNum(int number, int mod) throws CalculateException {
		int[] res = GCDExtended(number, mod);
		if (res[0] != 1) {
			throw new CalculateException("no solution");
		}
		else {
			return mod(res[1], mod);
		}
	}
}