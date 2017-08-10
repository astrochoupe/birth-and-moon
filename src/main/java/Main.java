import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Main {

    /** Moon age by date.
     * Moon age is the number of days since the last New Moon.
     * For example Full Moon is 14-15 days after New Moon.
     */
    private static Map<LocalDate,Integer> moonAge;

    /**
     * Give the number of birth by moon age.
     * @param args
     */
    public static void main(String[] args) {
        // initialize result map
        Map<Integer,Integer> birthsByMoonAge = new HashMap<>();
        for(int i=0; i<=30; i++) {
            birthsByMoonAge.put(i, 0);
        }

        InputStream is = Main.class.getClassLoader().getResourceAsStream("births.csv");

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
                    Integer moonAge = getMoonAge(date);

                    if(moonAge == null) {
                        System.out.println("Moon age unknown for " + dateIso);
                        continue;
                    }

                    int nbBirthAtMoonAge = birthsByMoonAge.get(moonAge);
                    birthsByMoonAge.put(moonAge, nbBirthAtMoonAge + nbBirthAtThisDate);
                } catch (NumberFormatException e) {
                    System.out.println("nbBirthString = '" + nbBirthString + "'");
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showResult(birthsByMoonAge);
    }

    private static LocalDate getDateFromIsoString(String dateIso) {
        String[] parts = dateIso.split("-");
        Integer year = Integer.valueOf(parts[0]);
        Integer month = Integer.valueOf(parts[1]);
        Integer day = Integer.valueOf(parts[2]);

        return LocalDate.of(year, month, day);
    }

    private static Integer getMoonAge(LocalDate date) {
        if(moonAge == null) {
            initMoonAgeMap();
        }

        return moonAge.get(date);
    }

    private static void initMoonAgeMap() {
        moonAge = new HashMap<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream("moon_phases.csv");

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
                    moonAge.put(date, 0);

                    for(int age=1; age<=30; age++) {
                        LocalDate date2 = date.plus(age, ChronoUnit.DAYS);
                        moonAge.put(date2, age);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showResult(Map<Integer,Integer> birthByMoonAge) {
        System.out.println("Moon age,Births");

        for (Map.Entry<Integer,Integer> pair : birthByMoonAge.entrySet()){
            System.out.println(pair.getKey() + "," + pair.getValue());
        }
    }
}
