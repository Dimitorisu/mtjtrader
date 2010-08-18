package forex.auto.trade.core;

public class BarValue {
	double[] values = null;
	int bars = 0;

	public BarValue(int size) {
		values = new double[size];
	}

	public double getValue(int index) {
		int size = values.length;
		if (index < size) {
			int reIndex = ((bars - index) % size);
			return values[reIndex];
		} else {
			return -1;
		}
	}

	public void setValue(int index, double value) {
		int size = values.length;
		if (index < size) {
			int reIndex = ((bars - index ) % size);
			values[reIndex] = value;
		}
	}

	public void newValue(double newValue) {
		bars++;
		
		int reIndex = bars % values.length;
		values[reIndex] = newValue;
		
	}

}
