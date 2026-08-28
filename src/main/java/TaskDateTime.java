import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TaskDateTime {
    private static final DateTimeFormatter[] DATE_TIME_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")
    };

    private static final DateTimeFormatter[] DATE_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    };

    private static final DateTimeFormatter OUTPUT_DATE =
            DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private final LocalDateTime dateTime;
    private final boolean hasTime;

    private TaskDateTime(LocalDateTime dateTime, boolean hasTime) {
        this.dateTime = dateTime;
        this.hasTime = hasTime;
    }

    public static TaskDateTime parse(String input) throws MeowMeowException {
        String trimmed = input.trim();

        for (DateTimeFormatter format : DATE_TIME_FORMATS) {
            try {
                return new TaskDateTime(LocalDateTime.parse(trimmed, format), true);
            } catch (DateTimeParseException e) {
                // try the next format
            }
        }

        for (DateTimeFormatter format : DATE_FORMATS) {
            try {
                return new TaskDateTime(LocalDate.parse(trimmed, format).atStartOfDay(), false);
            } catch (DateTimeParseException e) {
                // try the next format
            }
        }

        throw new MeowMeowException("I can't read \"" + trimmed + "\" as a date. "
                + "Try yyyy-MM-dd or d/M/yyyy, optionally with a time, "
                + "e.g. 2019-12-02 1800");
    }

    public static TaskDateTime fromStorage(String stored) throws MeowMeowException {
        try {
            if (stored.contains("T")) {
                return new TaskDateTime(LocalDateTime.parse(stored), true);
            }
            return new TaskDateTime(LocalDate.parse(stored).atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            throw new MeowMeowException("Unreadable date in save file: " + stored);
        }
    }

    public String toStorageFormat() {
        return hasTime ? dateTime.toString() : dateTime.toLocalDate().toString();
    }

    public LocalDate toLocalDate() {
        return dateTime.toLocalDate();
    }

    @Override
    public String toString() {
        return hasTime ? dateTime.format(OUTPUT_DATE_TIME) : dateTime.format(OUTPUT_DATE);
    }
}