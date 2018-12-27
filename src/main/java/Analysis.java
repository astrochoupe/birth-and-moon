import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analysis {

	/**
	 * Analysis of the number of births.
	 * @param args
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {
		// read CSV file (births by date)
		String filename = "birthsByDate.csv";
		Path path = Paths.get(Main.class.getClassLoader().getResource(filename).toURI());
		List<DateOfBirth> births = null;
		
		try(Stream<String> streamLines = Files.lines(path)) {
			births = streamLines
				.filter(line -> !line.startsWith("Date"))
				.map(line -> {return lineToDateOfBirth(line);})
				.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// births by day of week
		Map<DayOfWeek, Integer> result = births.stream()
			.collect(
				Collectors.groupingBy(
					dateOfBirth -> dateOfBirth.getDate().getDayOfWeek(),
					Collectors.summingInt(DateOfBirth::getNumberOfBirths)
				)
			);
		
		result.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(System.out::println);
		
		System.out.println();
		
		// births by month
		Map<Month, Integer> result2 = births.stream()
			.collect(
				Collectors.groupingBy(
					dateOfBirth -> dateOfBirth.getDate().getMonth(),
					Collectors.summingInt(DateOfBirth::getNumberOfBirths)
				)
			);
		
		
		result2.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(System.out::println);
		 
		System.out.println();
		
		// births by year
		Map<Integer, Integer> result3 = births.stream()
			.collect(
				Collectors.groupingBy(
					dateOfBirth -> dateOfBirth.getDate().getYear(),
					Collectors.summingInt(DateOfBirth::getNumberOfBirths)
				)
			);
		
		
		result3.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(System.out::println);
	}
	
	private static DateOfBirth lineToDateOfBirth(String csvLine) {
		DateOfBirth dateOfBirth = new DateOfBirth();
		
		String[] colonnes = csvLine.split(",");
		
		String[] datePart = colonnes[0].split("-");
		dateOfBirth.setDate(datePart[0], datePart[1], datePart[2]);
		
		dateOfBirth.setNumberOfBirths(Integer.valueOf(colonnes[1]));
		
		return dateOfBirth;
	}

}
