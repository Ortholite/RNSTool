package RNS;

/**
 * Класс, представляющий систему чисел в системе остаточных классов.
 */
public class Unit extends System {
	private ModSystem modSystem;

	//region Конструкторы.
	public Unit() {
		this(new int[]{}, new ModSystem());
	}

	public Unit(ModSystem modSystem) {
		this(new int[]{}, modSystem);
	}

	public Unit(String unit, ModSystem modSystem) {
		this(toArray(unit), modSystem);
	}

	public Unit(int[] unit, ModSystem modSystem) {
		super(unit);
		this.modSystem = modSystem;
	}
	//endregion

	public ModSystem getModSystem() {
		return modSystem;
	}

	public void setModSystem(ModSystem modSystem) {
		this.modSystem = modSystem;
	}
}