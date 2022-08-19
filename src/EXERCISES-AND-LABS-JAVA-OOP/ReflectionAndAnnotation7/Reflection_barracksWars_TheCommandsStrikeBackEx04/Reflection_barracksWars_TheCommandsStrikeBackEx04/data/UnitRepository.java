package Reflection_barracksWars_TheCommandsStrikeBackEx04.data;

import Reflection_barracksWars_TheCommandsStrikeBackEx04.interfaces.Repository;
import Reflection_barracksWars_TheCommandsStrikeBackEx04.interfaces.Unit;
import jdk.jshell.spi.ExecutionControl;

import java.util.Map;
import java.util.TreeMap;

public class UnitRepository implements Repository {

	private Map<String, Integer> amountOfUnits;

	public UnitRepository() {
		this.amountOfUnits = new TreeMap<>();
	}

	public void addUnit(Unit unit) {
		String unitType = unit.getClass().getSimpleName();
		if (!this.amountOfUnits.containsKey(unitType)) {
			this.amountOfUnits.put(unitType, 0);
		}

		int newAmount = this.amountOfUnits.get(unitType) + 1;
		this.amountOfUnits.put(unitType, newAmount);
	}

	public String getStatistics() {
		StringBuilder statBuilder = new StringBuilder();
		for (Map.Entry<String, Integer> entry : amountOfUnits.entrySet()) {
			String formatedEntry =
					String.format("%s -> %d%n", entry.getKey(), entry.getValue());
			statBuilder.append(formatedEntry);
		}
		statBuilder.setLength(
				statBuilder.length() - System.lineSeparator().length());

		return statBuilder.toString();
	}

	public void removeUnit(String unitType) throws ExecutionControl.NotImplementedException {
		// TODO: implement for problem 4
//		Map <String, Integer> amountOfUnits -> unitType: unitCount
		Integer unitsAmount = this.amountOfUnits.get(unitType);
		if (unitsAmount == null || unitsAmount <= 0) {
			this.amountOfUnits.remove(unitType);
			throw new IllegalArgumentException("No such units in repository.");
		} else if (unitsAmount > 0) {
			this.amountOfUnits.put(unitType, --unitsAmount);
		}
	}
}
