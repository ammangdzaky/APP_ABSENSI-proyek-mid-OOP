package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AppUtils {
    public static final Scanner scanner = new Scanner(System.in);

    // === Input Validation ===
    public static int getValidIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Harap masukkan angka!");
            }
        }
    }

    public static String getNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Input tidak boleh kosong!");
            }
        } while (input.isEmpty());
        return input;
    }

    public static String getTimeInput(String prompt) {
        Pattern timePattern = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
        while (true) {
            String input = getNonEmptyInput(prompt);
            if (timePattern.matcher(input).matches()) {
                return input;
            }
            System.out.println("Error: Format waktu HH:mm (contoh: 13:30)");
        }
    }

    // === Date/Time Formatting ===
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // === Screen Handling ===
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50)); // Fallback
        }
    }

    // === Other Utilities ===
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static int getValidIntInput(String prompt, int min, int max) {
        while (true) {
            int input = getValidIntInput(prompt);
            if (input >= min && input <= max) {
                return input;
            }
            System.out.println("Error: Masukkan angka antara " + min + "-" + max + "!");
        }
    }
}