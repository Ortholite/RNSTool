package RNS;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Базовый класс для представления системы чисел.
 */
public abstract class System {
	protected int[] system;

	//region Конструкторы.
	protected System() {
		this(null);
	}

	protected System(int[] system) {
		this.system = system;
	}
	//endregion

	public int[] getSystem() {
		return system;
	}

	public void setSystem(int[] system) {
		this.system = system;
	}

	public int getLength() {
		return system.length;
	}

	/**
	 * Метод, преобразующий строковое представление системы чисел в массив.
	 * @param strSystem Строковое представление системы.
	 * @return Возввращает массив чисел системы.
	 */
	public static int[] toArray(String strSystem) {
		Matcher matcher = Pattern.compile("((?<value>\\d+)(,\\s)?)").matcher(strSystem);
		ArrayList<Integer> list = new ArrayList<>();
		while (matcher.find())
			list.add(Integer.parseInt(matcher.group("value")));
		return list.stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Метод, представляющий систему числел в строковом формате вида {1, 2, ..., n}.
	 * @return Возвращает систему в строковом формате.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('{');
		for (int i : system)
			stringBuilder.append(Integer.toString(i)).append(",");
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append('}');
		return stringBuilder.toString();
	}
}