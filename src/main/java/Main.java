import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Main {

    /** Moon's age by date.
     * Moon's age is the number of days since the last New Moon.
     * For example Full Moon is 14-15 days after New Moon.
     */
    private static Map<LocalDate,Integer> moonsAge;

    /**
     * Give the number of birth by moon's age.
     * @param args
     */
    public static void main(String[] args) {
        // initialize result map
        Map<Integer,Integer> birthsByMoonsAge = new HashMap<>();
        for(int i=0; i<=30; i++) {
            birthsByMoonsAge.put(i, 0);
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
                    Integer moonsAge = getMoonsAge(date);

                    if(moonsAge == null) {
                        System.out.println("Moon's age unknown for " + dateIso);
                        continue;
                    }

                    int nbBirthAtMoonsAge = birthsByMoonsAge.get(moonsAge);
                    birthsByMoonsAge.put(moonsAge, nbBirthAtMoonsAge + nbBirthAtThisDate);
                } catch (NumberFormatException e) {
                    System.out.println("nbBirthString = '" + nbBirthString + "'");
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showResult(birthsByMoonsAge);
    }

    private static LocalDate getDateFromIsoString(String dateIso) {
        String[] parts = dateIso.split("-");
        Integer year = Integer.valueOf(parts[0]);
        Integer month = Integer.valueOf(parts[1]);
        Integer day = Integer.valueOf(parts[2]);

        return LocalDate.of(year, month, day);
    }

    private static Integer getMoonsAge(LocalDate date) {
        if(moonsAge == null) {
            initMoonsAgeMap();
        }

        return moonsAge.get(date);
    }

    private static void initMoonsAgeMap() {
        moonsAge = new HashMap<>();

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

                if(phase.equals("NL")) {
                    moonsAge.put(date, 0);

                    for(int age=1; age<=30; age++) {
                        LocalDate date2 = date.plus(age, ChronoUnit.DAYS);
                        moonsAge.put(date2, age);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showResult(Map<Integer,Integer> birthByMoonsAge) {
        System.out.println("Moon's age,Births");

        for (Map.Entry<Integer,Integer> pair : birthByMoonsAge.entrySet()){
            System.out.println(pair.getKey() + "," + pair.getValue());
        }
    }
}
