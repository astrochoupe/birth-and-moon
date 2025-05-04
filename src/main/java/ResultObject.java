
public class ResultObject {
	private int births = 0;
	private int days = 0;
	private int saturdays = 0;
	private int sundays = 0;
	private int publicHolidays = 0;

	public int getBirths() {
		return births;
	}

	public void addBirths(int births) {
		this.births += births;
	}
	
	public int getDays() {
		return days;
	}

	public void incDays() {
		this.days++;
	}

	public int getSaturdays() {
		return saturdays;
	}

	public void incSaturdays() {
		this.saturdays++;
	}

	public int getSundays() {
		return sundays;
	}

	public void incSundays() {
		this.sundays++;
	}

	public int getPublicHolidays() {
		return publicHolidays;
	}

	public void incPublicHolidays() {
		this.publicHolidays++;
	}

}
