package ro.ase.acs.classes;

@FunctionalInterface
public interface DataSeriesOperation {
	public Double doOperation(Integer[] values);
}
