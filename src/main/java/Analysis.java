import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analysis {

	/**
	 * Analysis of the number of births.
	 * 
	 * @param args No argument needed
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {
		String filename = "birthsByDate.csv";
		List<Births4Day> birthsByDay = readCsv(filename);
		
		birthsByDayOfWeek(birthsByDay);
		birthsByMonth(birthsByDay);
		birthsByYear(birthsByDay);
	}

	/**
	 * Read CSV file (births by date).
	 * 
	 * @param filename Path and filename in the classpath.
	 * @return List of births by day. Could be empty (but not null).
	 * @throws URISyntaxException
	 */
	private static List<Births4Day> readCsv(String filename) throws URISyntaxException {
		List<Births4Day> birthsByDay = Collections.emptyList();
		Path path = Paths.get(Main.class.getClassLoader().getResource(filename).toURI());
		
		try(Stream<String> streamLines = Files.lines(path)) {
			birthsByDay = streamLines
				.filter(line -> !line.startsWith("Date")) // remove header line
				.map(line -> {return lineToBirth4Day(line);})
				.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return birthsByDay;
	}

	/**
	 * Calculate the number of births by day of week.
	 * Print the result in the console.
	 * 
	 * @param birthsByDay List of births by day
	 */
	private static void birthsByDayOfWeek(List<Births4Day> birthsByDay) {
		Map<DayOfWeek, Integer> result = birthsByDay.stream()
			.collect(
				Collectors.groupingBy(
					dateOfBirth -> dateOfBirth.getDate().getDayOfWeek(),
					Collectors.summingInt(Births4Day::getNumberOfBirths)
				)
			);
		
		result.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(System.out::println);
		
		System.out.println();
	}

	/**
	 * Calculate the number of births by Month.
	 * Print the result in the console.
	 * 
	 * @param birthsByDay List of births by day
	 */
	private static void birthsByMonth(List<Births4Day> birthsByDay) {
		Map<Month, Integer> result = birthsByDay.stream()
			.collect(
				Collectors.groupingBy(
					dateOfBirth -> dateOfBirth.getDate().getMonth(),
					Collectors.summingInt(Births4Day::getNumberOfBirths)
				)
			);
		
		result.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(System.out::println);
		 
		System.out.println();
	}

	
	/**
	 * Calculate the number of births by year.
	 * Print the result in the console.
	 * 
	 * @param birthsByDay List of births by day
	 */
	private static void birthsByYear(List<Births4Day> birthsByDay) {
		Map<Integer, Integer> result = birthsByDay.stream()
			.collect(
				Collectors.groupingBy(
					dateOfBirth -> dateOfBirth.getDate().getYear(),
					Collectors.summingInt(Births4Day::getNumberOfBirths)
				)
			);
		
		result.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(System.out::println);
		
		System.out.println();
	}
	
	/**
	 * Convert a CSV line (with the number of births for a day) to a model object.
	 * 
	 * @param csvLine A CSV line
	 * @return A model object Births4Day
	 */
	private static Births4Day lineToBirth4Day(String csvLine) {
		Births4Day birth4day = new Births4Day();
		
		String[] columns = csvLine.split(",");
		
		String[] datePart = columns[0].split("-");
		birth4day.setDate(datePart[0], datePart[1], datePart[2]);
		
		birth4day.setNumberOfBirths(Integer.valueOf(columns[1]));
		
		return birth4day;
	}

}
