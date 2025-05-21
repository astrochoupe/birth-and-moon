import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		String filenameBirthsByDate = "birthsByDate.csv";
		String filenamePublicHolidays = "publicHolidays.csv";
		List<Births4Day> birthsByDay = readCsvBirthByDate(filenameBirthsByDate);
		Map<LocalDate, Integer> birthsByDayMap = csvBirthByDateToMap(filenameBirthsByDate);
		
		birthsByDayOfWeek(birthsByDay);
		birthsByMonth(birthsByDay);
		birthsByYear(birthsByDay);
		birthsWhenPublicHolidays(birthsByDayMap, filenamePublicHolidays);
		averageBirthsByDayOfWeek(birthsByDay);
	}

	/**
	 * Read CSV file "births by date".
	 * 
	 * @param filename Path and filename in the classpath.
	 * @return List of births by day. Could be empty (but not null).
	 * @throws URISyntaxException
	 */
	private static List<Births4Day> readCsvBirthByDate(String filename) throws URISyntaxException {
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
	 * Read CSV file "births by date" and store to a Map.
	 * 
	 * @param filename Path and filename in the classpath.
	 * @return a Map<LocalDate, Integer>
	 */
	private static Map<LocalDate, Integer> csvBirthByDateToMap(String filename) {
		Map<LocalDate, Integer> map = new HashMap<>();
		
        InputStream is = Main.class.getClassLoader().getResourceAsStream(filename);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            // skip header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String nbBirthString = null;
                try {
                    String[] parts = line.split(",");
                    String dateIso = parts[0];
                    nbBirthString = parts[1];

                    // conversion
                    Integer nbBirthAtThisDate = Integer.valueOf(nbBirthString);
                    LocalDate date = parseDateIso8601(dateIso);
                    
                    map.put(date, nbBirthAtThisDate);
                    
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return map;
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
	 * Calculate the average number of births by day of week.
	 * Print the result in the console.
	 * 
	 * @param birthsByDay List of births by day
	 */
	private static void averageBirthsByDayOfWeek(List<Births4Day> birthsByDay) {
		Map<DayOfWeek, Integer> birthsByDayOfWeek = new HashMap<>();
		Map<DayOfWeek, Integer> daysByDayOfWeek = new HashMap<>();
		
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) { 
			birthsByDayOfWeek.put(dayOfWeek, 0);
			daysByDayOfWeek.put(dayOfWeek, 0);
		}
		
		for(Births4Day births4Day : birthsByDay) {
			LocalDate date = births4Day.getDate();
			Integer births = births4Day.getNumberOfBirths();
			
			DayOfWeek dayOfWeek = date.getDayOfWeek();
			
			Integer birthsForThisDayOfWeek = birthsByDayOfWeek.get(dayOfWeek);
			birthsForThisDayOfWeek += births;
			birthsByDayOfWeek.put(dayOfWeek, birthsForThisDayOfWeek);
			
			Integer daysForThisDayOfWeek = daysByDayOfWeek.get(dayOfWeek);
			daysForThisDayOfWeek++;
			daysByDayOfWeek.put(dayOfWeek, daysForThisDayOfWeek);	
		}
		
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) { 
			Integer birthsForThisDayOfWeek = birthsByDayOfWeek.get(dayOfWeek);
			Integer daysForThisDayOfWeek = daysByDayOfWeek.get(dayOfWeek);
			
			int average = birthsForThisDayOfWeek / daysForThisDayOfWeek;
			
			System.out.println(dayOfWeek.toString() + "=" + average);
		}
		
		System.out.println();
	}
	
	/**
	 * Calculate the number of births when a public holiday occurs (and it is not a saturday or sunday).
	 * Print the result in the console.
	 * 
	 * @param birthsByDay List of births by day
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	private static void birthsWhenPublicHolidays(Map<LocalDate, Integer> birthsByDayMap, String filenamePublicHolidays) throws URISyntaxException, IOException {
		// read csv with public holidays
		Path path = Paths.get(Main.class.getClassLoader().getResource(filenamePublicHolidays).toURI());
		
		// init variables
		int numberOfPublicHolidays = 0;
		int totalBirths = 0;
		
		// for each public holiday
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			// skip header line
			if (line.startsWith("date")) {
				continue;
			}
			
			String[] columns = line.split(",");
			LocalDate publicHolidayDate = parseDateIso8601(columns[0]);
			DayOfWeek dayOfWeek = publicHolidayDate.getDayOfWeek();
			
			if(!DayOfWeek.SATURDAY.equals(dayOfWeek) && !DayOfWeek.SUNDAY.equals(dayOfWeek)) {
				int births = birthsByDayMap.get(publicHolidayDate);
				
				numberOfPublicHolidays++;
				totalBirths += births;
			}
		}
		
		int averageBirth = totalBirths/numberOfPublicHolidays;
		
		System.out.println("Sum of births during public holidays=" + totalBirths);
		System.out.println("Average of births during a public holidays=" + averageBirth);
		
		
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
		
		LocalDate date = parseDateIso8601(columns[0]);
		birth4day.setDate(date);
		
		birth4day.setNumberOfBirths(Integer.valueOf(columns[1]));
		
		return birth4day;
	}
	
	/**
	 * Converts a date as a String in ISO 8601 format to a LocatDate object.
	 * 
	 * @param dateIso8601 a String in ISO 8601 format
	 * @return a LocatDate object
	 */
	private static LocalDate parseDateIso8601(String dateIso8601) {
		String[] datePart = dateIso8601.split("-");
		String year = datePart[0];
		String month = datePart[1];
		String day = datePart[2];
		return LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
	}

}
