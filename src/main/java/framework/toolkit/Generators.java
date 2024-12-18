package framework.toolkit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class Generators {

    public String generateAdultPesel() {
        LocalDate currentDate = LocalDate.now();
        int maxYear = currentDate.getYear() - 18;
        int year = ThreadLocalRandom.current().nextInt(maxYear - 1900) + 1900;
        int month = ThreadLocalRandom.current().nextInt(1, 13);
        int day = ThreadLocalRandom.current().nextInt(1, 29); // Uproszczony przykład, zakładamy, że zawsze generujemy dzień od 1 do 28

        // Generowanie cyfr kontrolnych i pozostałych cyfr PESEL
        StringBuilder peselBuilder = new StringBuilder();
        peselBuilder.append(String.format("%02d", year % 100))
                .append(String.format("%02d", month))
                .append(String.format("%02d", day))
                .append(generateRandomDigits(4))
                .append(generateControlDigit(peselBuilder.toString()));

        return peselBuilder.toString();
    }

    public static String generateRandomDigits(int count) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int randomDigit = ThreadLocalRandom.current().nextInt(10);
            digits.append(randomDigit);
        }
        return digits.toString();
    }

    public static String generateControlDigit(String partialPesel) {
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int controlSum = 0;

        for (int i = 0; i < 10; i++) {
            int digit = Integer.parseInt(partialPesel.substring(i, i + 1));
            controlSum += digit * weights[i];
        }

        int controlDigit = (10 - (controlSum % 10)) % 10;
        return String.valueOf(controlDigit);
    }


}
