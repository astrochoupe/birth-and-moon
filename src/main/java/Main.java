import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<LocalDate,Integer> moonAge;

    public static void main(String[] args) {
        Map<Integer,Integer> birthByMoonAge = new HashMap<>();
        // init result map
        for(int i=0; i<=30; i++) {
            birthByMoonAge.put(i, 0);
        }

        InputStream is = Main.class.getClassLoader().getResourceAsStream("naissances.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // avoid header line
                if(lineNumber>1) {
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

                        int nbBirthAtMoonAge = birthByMoonAge.get(moonAge);
                        birthByMoonAge.put(moonAge, nbBirthAtMoonAge + nbBirthAtThisDate);
                    } catch (NumberFormatException e) {
                        System.out.println("nbBirthString = '" + nbBirthString + "'");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showResult(birthByMoonAge);
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

        InputStream is = Main.class.getClassLoader().getResourceAsStream("phases_lune.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            // skip header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String date = parts[0];
                String phase = parts[1];

                // date conversion
                LocalDate localDate = getDateFromIsoString(date);

                if(phase.equals("NL")) {
                    moonAge.put(localDate, 0);

                    for(int age=1; age<=30; age++) {
                        LocalDate localDate2 = localDate.plus(age, ChronoUnit.DAYS);
                        moonAge.put(localDate2, age);
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
