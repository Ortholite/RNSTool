package RNS;

/**
 * Статический класс, предназначенный для преобразования чисел из позиционной системы счисления в
 * систему остаточных классов и наоборот.
 */
public final class Convert {
	private Convert() {}

	/**
	 * Метод для преобразования числа из позиционной системы счисления в систему остаточных классов.
	 * @param number Число для преобразования.
	 * @param modSystem Система модулей.
	 * @return Возвращает число в системе остаточных классов.
	 */
	public static Unit toRNS(int number, ModSystem modSystem) {
		int[] unit = new int[modSystem.getLength()];
		for (int i = 0; i < modSystem.getLength(); i++) {
			unit[i] = Math.mod(number, modSystem.getSystem()[i]);
		}
		return new Unit(unit, modSystem);
	}

	/**
	 * Метод для преобразования числа из системы остаточных классов в позиционную систему счисления.
	 * @param unit Число в системе остаточных классов.
	 * @return Возвращает число в позиционной системе счисления.
	 * @throws CalculateException Выбрасывает исключение, если мультипликативно обратное не имеет решения.
	 */
	public static int toPositionalSystem(Unit unit) throws CalculateException {
		ModSystem modSystem = unit.getModSystem();
		int m = modSystem.getMaxValue();
		int length = modSystem.getLength();
		int[] mi = new int[length];
		int[] bi = new int[length];
		for (int i = 0; i < length; i++) {
			mi[i] = m / modSystem.getSystem()[i];
			bi[i] = Math.invNum(mi[i], modSystem.getSystem()[i]);
		}
		int number = 0;
		for (int i = 0; i < length; i++) {
			number += unit.getSystem()[i] * mi[i] * bi[i];
		}
		return Math.mod(number, m);
	}
}