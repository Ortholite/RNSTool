package RNS;

/**
 * Класс, представляющий систему модулей.
 */
public class ModSystem extends System {
	//region Конструкторы.
	public ModSystem() {
		super();
	}

	public ModSystem(int n) {
		super(generateModSystem(n));
	}

	public ModSystem(String modSystem) throws Exception {
		super(toArray(modSystem));
		if (!isPairwiseSimple()) throw new Exception("Система модулей не попарно простая.");
	}

	public ModSystem(ModSystem modSystem) {
		super(modSystem.getSystem());
	}
	//endregion

	/**
	 * Статический метод, проверяющий, являяется ли число взаимнопростым с модулями системы.
	 * Использует алгоритм Евклида.
	 * @param number Проверяемое целое число.
	 * @param modSystem Целевая система модулей.
	 * @return Возвращает результат проверки.
	 */
	public static boolean isPairwiseSimple(int number, ModSystem modSystem) {
		int[] system = modSystem.getSystem();
		int length = modSystem.getLength();
		for (int i = 0; i < length; i++) {
			if (!(Math.GCD(number, system[i]) == 1)) return false;
		}
		return true;
	}

	/**
	 * Метод, проверяющий, являются ли модули взаимнопростыми.
	 * Использует алгоритм Евклида.
	 * @return Возвращает результат проверки.
	 */
	private boolean isPairwiseSimple() {
		for (int i = 0; i < getLength(); i++) {
			for (int j = 0; j < getLength(); j++) {
				if (i != j) {
					if (!(Math.GCD(system[i], system[j]) == 1)) return false;
				}
			}
		}
		return true;
	}

	/**
	 * Метод, вычисляющий динамический диапазон системы модулей.
	 * @return Вовзвращает динамический диапазон системы модулей.
	 */
	public int getMaxValue() {
		int maxValue = 1;
		for (int i : system) {
			maxValue *= i;
		}
		return maxValue;
	}

	public String getRange() {
		return String.format("[0, %s]", getMaxValue() - 1);
	}

	/**
	 * Метод генерации системы модулей по шаблону: {2^(2n)+1, 2^n+1, 2^n-1}.
	 * @param n Натуральное число.
	 * @return Возвращает массив модулей системы.
	 */
	public static int[] generateModSystem(int n) {
		return new int[] {
				(int) java.lang.Math.pow(2, 2 * n) + 1,
				(int) java.lang.Math.pow(2, n) + 1,
				(int) java.lang.Math.pow(2, n) - 1
		};
	}
}