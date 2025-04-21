import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Main {
    
    /** Number of days from Full moon by date.
     */
    private static Map<LocalDate,Integer> daysFromFullMoonForADate;
    
    private static int MIN_DAY_FROM_FULL_MOON = -15;
    private static int MAX_DAY_FROM_FULL_MOON = 15;

    /**
     * Give the number of births by days from Full moon.
     * @param args
     */
    public static void main(String[] args) {
        // initialize result map
        Map<Integer,Integer> birthsByDayFromFullMoon = new HashMap<>();
        for(int day=MIN_DAY_FROM_FULL_MOON; day<=MAX_DAY_FROM_FULL_MOON; day++) {
            birthsByDayFromFullMoon.put(day, 0);
        }

        InputStream is = Main.class.getClassLoader().getResourceAsStream("birthsByDate.csv");

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
                    int nbBirthAtThisDate = Integer.valueOf(nbBirthString);
                    LocalDate date = getDateFromIsoString(dateIso);
                    Integer daysFromFullMoon = getDaysFromFullMoon(date);

                    if(daysFromFullMoon == null) {
                        System.out.println("Days from Full moon unknown for " + dateIso);
                        continue;
                    }

                    int nbBirthForDaysFromFullMoon = birthsByDayFromFullMoon.get(daysFromFullMoon);
                    birthsByDayFromFullMoon.put(daysFromFullMoon, nbBirthForDaysFromFullMoon + nbBirthAtThisDate);
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

        InputStream is = Main.class.getClassLoader().getResourceAsStream("moonPhases.csv");

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

    private static void showResult(Map<Integer,Integer> birthFromFullMoon) {
        System.out.println("Days from Full moon,Births");
        
        for(int day=MIN_DAY_FROM_FULL_MOON; day<=MAX_DAY_FROM_FULL_MOON; day++) {
        	Integer value = birthFromFullMoon.get(day);
        	System.out.println(day + "," + value);
        }
    }
}
