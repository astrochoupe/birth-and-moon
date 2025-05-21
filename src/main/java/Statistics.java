
public class Statistics {

	/**
	 * Calculate the standard deviation of an array of int.
	 * 
	 * @param array  An array of int
	 * @param double Average of the array
	 * @param int    Number of items in array
	 * @return The standard deviation
	 */
	public static double stdDev(int[] array, double avg, int nbItems) {
		double sum = 0;

		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0) {
				double difference = array[i] - avg;
				double square = difference * difference;
				sum += square;
			}
		}

		double variance = sum / nbItems;
		return Math.sqrt(variance);
	}

}
