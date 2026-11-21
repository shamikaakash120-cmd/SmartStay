package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import model.E195569BedType;
import model.E195569RoomType;

/**
 * Reusable utility class for safe, validated console input parsing.
 * Uses a single shared Scanner to prevent resource leaks and input stream conflicts.
 * Re-prompts gracefully on invalid input and never crashes or throws runtime exceptions.
 */
public final class E195569ConsoleReader {

    // Shared single Scanner instance across the entire console application
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{9,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private E195569ConsoleReader() {}

    /**
     * Reads a non-blank line of text from the console.
     * Re-prompts continuously until a valid non-empty string is entered.
     *
     * @param prompt User instruction prompt.
     * @return Trimmed non-empty string entered by the user.
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                if (!SCANNER.hasNextLine()) {
                    return "";
                }
                String input = SCANNER.nextLine();
                if (input != null && !input.trim().isBlank()) {
                    return input.trim();
                }
                System.out.println("  [!] Input cannot be empty. Please try again.");
            } catch (Exception e) {
                System.out.println("  [!] Error reading input. Please try again.");
            }
        }
    }

    /**
     * Reads an integer within a specified inclusive range.
     * Validates input format and range, re-prompting on invalid values.
     *
     * @param prompt User prompt.
     * @param min    Minimum allowed value (inclusive).
     * @param max    Maximum allowed value (inclusive).
     * @return Validated integer within the specified range.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                if (!SCANNER.hasNextLine()) {
                    return min;
                }
                String input = SCANNER.nextLine();
                if (input == null || input.trim().isBlank()) {
                    System.out.println("  [!] Input cannot be empty. Please enter an integer.");
                    continue;
                }
                int value = Integer.parseInt(input.trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  [!] Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number format. Please enter a valid integer.");
            } catch (Exception e) {
                System.out.println("  [!] Error reading input. Please try again.");
            }
        }
    }

    /**
     * Reads a date in yyyy-MM-dd format with strict validation.
     * Re-prompts on empty or malformed date inputs.
     *
     * @param prompt User prompt.
     * @return Validated LocalDate instance.
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            String displayPrompt;
            if (prompt.toLowerCase().contains("yyyy-mm-dd")) {
                displayPrompt = prompt.endsWith(" ") ? prompt : prompt + " ";
            } else {
                displayPrompt = prompt.endsWith(": ") ? prompt.substring(0, prompt.length() - 2) + " (yyyy-MM-dd): "
                        : (prompt.endsWith(":") ? prompt.substring(0, prompt.length() - 1) + " (yyyy-MM-dd): "
                        : prompt + " (yyyy-MM-dd): ");
            }
            System.out.print(displayPrompt);
            try {
                if (!SCANNER.hasNextLine()) {
                    return LocalDate.now();
                }
                String input = SCANNER.nextLine();
                if (input == null || input.trim().isBlank()) {
                    System.out.println("  [!] Date cannot be empty. Please use yyyy-MM-dd (e.g., 2026-12-01).");
                    continue;
                }
                return LocalDate.parse(input.trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("  [!] Invalid date format. Please use yyyy-MM-dd (e.g., 2026-12-01).");
            } catch (Exception e) {
                System.out.println("  [!] Error reading input. Please try again.");
            }
        }
    }

    /**
     * Displays available room types as numbered choices and returns the chosen enum value.
     *
     * @param prompt Descriptive prompt header.
     * @return Selected E195569RoomType enum value.
     */
    public static E195569RoomType readRoomType(String prompt) {
        System.out.println(prompt);
        E195569RoomType[] types = E195569RoomType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  [%d] %s%n", (i + 1), types[i].getDisplayName());
        }
        int choice = readInt("Select room type (1-" + types.length + "): ", 1, types.length);
        return types[choice - 1];
    }

    /**
     * Displays available bed types as numbered choices and returns the chosen enum value.
     *
     * @param prompt Descriptive prompt header.
     * @return Selected E195569BedType enum value.
     */
    public static E195569BedType readBedType(String prompt) {
        System.out.println(prompt);
        E195569BedType[] beds = E195569BedType.values();
        for (int i = 0; i < beds.length; i++) {
            System.out.printf("  [%d] %s%n", (i + 1), beds[i].getDisplayName());
        }
        int choice = readInt("Select bed type (1-" + beds.length + "): ", 1, beds.length);
        return beds[choice - 1];
    }

    /**
     * Reads and validates a contact phone number.
     *
     * @param prompt User prompt.
     * @return Validated phone number string.
     */
    public static String readPhoneNumber(String prompt) {
        while (true) {
            String phone = readString(prompt);
            if (PHONE_PATTERN.matcher(phone).matches()) {
                return phone;
            }
            System.out.println("  [!] Invalid phone number (9-15 digits, optional leading +). Please try again.");
        }
    }

    /**
     * Reads and validates an email address according to standard format.
     *
     * @param prompt User prompt.
     * @return Validated email string.
     */
    public static String readEmail(String prompt) {
        while (true) {
            String email = readString(prompt);
            if (EMAIL_PATTERN.matcher(email).matches()) {
                return email;
            }
            System.out.println("  [!] Invalid email format (e.g., user@example.com). Please try again.");
        }
    }

    /**
     * Pauses console execution until the user presses Enter.
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        try {
            if (SCANNER.hasNextLine()) {
                SCANNER.nextLine();
            }
        } catch (Exception ignored) {}
    }
}
