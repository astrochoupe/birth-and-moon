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
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Main {
    
    /** Number of days from Full moon by date. */
    private static Map<LocalDate,Integer> daysFromFullMoonForADate;
    
	/** List of public holidays. */
	private static Map<LocalDate, Boolean> publicHolidays;
    
    private static int MIN_DAY_FROM_FULL_MOON = -15;
    private static int MAX_DAY_FROM_FULL_MOON = 15;
    
    static String FILENAME_BIRTHS_BY_DATE = "birthsByDate.csv";
    static String FILENAME_MOON_PHASES = "moonPhases.csv";
    static String FILENAME_PUBLIC_HOLIDAYS = "publicHolidays.csv";

    /**
     * Give the number of births by days from Full moon.
     * @param args
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
    	// initialize public holidays
    	initializePublicHolidays();
    	
        // initialize result map
        Map<Integer,ResultObject> birthsByDayFromFullMoon = new HashMap<>();
        for(int day=MIN_DAY_FROM_FULL_MOON; day<=MAX_DAY_FROM_FULL_MOON; day++) {
            birthsByDayFromFullMoon.put(day, new ResultObject());
        }

        InputStream is = Main.class.getClassLoader().getResourceAsStream(FILENAME_BIRTHS_BY_DATE);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
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
                    int nbBirthAtThisDate = Integer.valueOf(nbBirthString);
                    LocalDate date = getDateFromIsoString(dateIso);
                    Integer daysFromFullMoon = getDaysFromFullMoon(date);

                    if(daysFromFullMoon == null) {
                        System.out.println("Days from Full moon unknown for " + dateIso);
                        continue;
                    }

                    ResultObject resultObject = birthsByDayFromFullMoon.get(daysFromFullMoon);
                    resultObject.addBirths(nbBirthAtThisDate);
                    resultObject.incDays();
                    
                    if(DayOfWeek.SATURDAY.equals(date.getDayOfWeek())) {
                    	resultObject.incSaturdays();
                    } else  if(DayOfWeek.SUNDAY.equals(date.getDayOfWeek())) {
                    	resultObject.incSundays();
                    } else if(publicHolidays.containsKey(date)) {
                    	resultObject.incPublicHolidays();
                    }
                    	
                } catch (NumberFormatException e) {
                    System.out.println("nbBirthString = '" + nbBirthString + "'");
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showResult(birthsByDayFromFullMoon);
    }

    private static LocalDate getDateFromIsoString(String dateIso) {
        String[] parts = dateIso.split("-");
        Integer year = Integer.valueOf(parts[0]);
        Integer month = Integer.valueOf(parts[1]);
        Integer day = Integer.valueOf(parts[2]);

        return LocalDate.of(year, month, day);
    }
    
    private static Integer getDaysFromFullMoon(LocalDate date) {
        if(daysFromFullMoonForADate == null) {
        	initDaysFromFullMoon();
        }

        return daysFromFullMoonForADate.get(date);
    }

    private static void initDaysFromFullMoon() {
    	daysFromFullMoonForADate = new HashMap<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream(FILENAME_MOON_PHASES);

        // note: phases are in french abbreviation
        // NL = Nouvelle Lune (New Moon)
        // PQ = Premier Quartier (First Quarter)
        // PL = Pleine Lune (Full Moon)
        // DQ = Dernier Quartier (Last Quarter)

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            // skip header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String dateIso = parts[0];
                String phase = parts[1];

                // date conversion
                LocalDate date = getDateFromIsoString(dateIso);

                if(phase.equals("PL")) {
                    for(int day=MIN_DAY_FROM_FULL_MOON; day<=MAX_DAY_FROM_FULL_MOON; day++) {
                        LocalDate date2 = date.plus(day, ChronoUnit.DAYS);
                        daysFromFullMoonForADate.put(date2, day);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showResult(Map<Integer,ResultObject> birthFromFullMoon) {
        System.out.println("Days from Full moon,Births,Days,Saturdays,Sundays,Public holidays,Avg births by day,Std dév");
        
        for(int day=MIN_DAY_FROM_FULL_MOON; day<=MAX_DAY_FROM_FULL_MOON; day++) {
        	ResultObject resultObject = birthFromFullMoon.get(day);
        	
        	int births = resultObject.getBirths();
        	int days = resultObject.getDays();

        	System.out.print(day + ",");
        	System.out.print(births + ",");
        	System.out.print(days + ",");
        	System.out.print(resultObject.getSaturdays() + ",");
        	System.out.print(resultObject.getSundays() + ",");
        	System.out.print(resultObject.getPublicHolidays() + ",");
        	
        	if(days != 0) {
        		double avg = (double) births/days;
        		double stdDev = Statistics.stdDev(resultObject.getArrayBirths(), avg, days);
        		
        		System.out.print(Math.round(avg) + ",");
        		System.out.println(Math.round(stdDev));
        		
        	} else {
        		System.out.println("N/A,N/A");
        	}
        	
        }
    }
    
    private static void initializePublicHolidays() throws URISyntaxException, IOException {
    	publicHolidays = new HashMap<>();
    	
		// read csv with public holidays
		Path path = Paths.get(Main.class.getClassLoader().getResource(FILENAME_PUBLIC_HOLIDAYS).toURI());
		
		// for each public holiday
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			// skip header line
			if (line.startsWith("date")) {
				continue;
			}
			
			String[] columns = line.split(",");
			LocalDate publicHolidayDate = getDateFromIsoString(columns[0]);
			
			publicHolidays.put(publicHolidayDate, Boolean.TRUE);
		}

    }
}
