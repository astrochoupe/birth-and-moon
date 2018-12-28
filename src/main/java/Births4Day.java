import java.time.LocalDate;



public class Births4Day {
	private LocalDate date;
	private Integer numberOfBirths;
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
		date.getDayOfWeek();
	}
	
	public void setDate(String year, String month, String day) {
		date = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
	}
	
	public Integer getNumberOfBirths() {
		return numberOfBirths;
	}
	
	public void setNumberOfBirths(Integer numberOfBirths) {
		this.numberOfBirths = numberOfBirths;
	}
	
	public String toString() {
		return date + " " + numberOfBirths;
	}
}
