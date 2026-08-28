package meowmeow;

/**
 * Interprets raw user input and converts it into commands and task objects.
 */
public class Parser {

    /**
     * Returns the command type named by the first word of the input.
     *
     * @param input Full line of user input.
     * @return Matching command type, or {@code UNKNOWN} if unrecognised.
     */
    public static CommandType parseCommand(String input) {
        String[] words = input.trim().split("\\s+", 2);
        return CommandType.fromString(words[0]);
    }

    /**
     * Returns the first word of the input exactly as the user typed it.
     *
     * @param input Full line of user input.
     * @return The command word.
     */
    public static String parseCommandWord(String input) {
        String[] words = input.trim().split("\\s+", 2);
        return words[0];
    }

    /**
     * Returns everything after the command word, or an empty string if the
     * input was a single word.
     *
     * @param input Full line of user input.
     * @return Trimmed argument text.
     */
    public static String parseArguments(String input) {
        String[] words = input.trim().split("\\s+", 2);
        return (words.length > 1) ? words[1].trim() : "";
    }

    /**
     * Returns a todo built from the given argument text.
     *
     * @param arguments Text following the todo command word.
     * @return Todo with the given description.
     * @throws MeowMeowException If the description is empty.
     */
    public static Todo parseTodo(String arguments) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("A todo needs a description. Try: todo borrow book");
        }
        return new Todo(arguments);
    }

    /**
     * Returns a deadline built from the given argument text.
     * Expects a description followed by a due date after the /by delimiter.
     *
     * @param arguments Text following the deadline command word.
     * @return Deadline with the given description and due date.
     * @throws MeowMeowException If the description is missing, the /by
     *         delimiter is absent, or the date cannot be parsed.
     */

    public static Deadline parseDeadline(String arguments) throws MeowMeowException {
        String example = "Try: deadline return book /by 2019-12-02 1800";
        String[] parts = arguments.split("/by", 2);
        String description = parts[0].trim();

        if (description.isEmpty()) {
            throw new MeowMeowException("A deadline needs a description. " + example);
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new MeowMeowException("I need a due date after /by. " + example);
        }

        return new Deadline(description, TaskDateTime.parse(parts[1].trim()));
    }

    /**
     * Returns an event built from the given argument text.
     * Expects a description followed by a start time after /from and an end
     * time after /to.
     *
     * @param arguments Text following the event command word.
     * @return Event with the given description, start and end times.
     * @throws MeowMeowException If the description is missing, either
     *         delimiter is absent, or a time cannot be parsed.
     */
    public static Event parseEvent(String arguments) throws MeowMeowException {
        String example = "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600";
        String[] fromParts = arguments.split("/from", 2);
        String description = fromParts[0].trim();

        if (description.isEmpty()) {
            throw new MeowMeowException("An event needs a description. " + example);
        }
        if (fromParts.length < 2) {
            throw new MeowMeowException("I need a start time after /from. " + example);
        }

        String[] toParts = fromParts[1].split("/to", 2);
        String fromText = toParts[0].trim();

        if (fromText.isEmpty()) {
            throw new MeowMeowException("The start time after /from is empty. " + example);
        }
        if (toParts.length < 2 || toParts[1].trim().isEmpty()) {
            throw new MeowMeowException("I need an end time after /to. " + example);
        }

        return new Event(description,
                TaskDateTime.parse(fromText),
                TaskDateTime.parse(toParts[1].trim()));
    }

    /**
     * Returns the date named by the given argument text.
     *
     * @param arguments Text following the command word.
     * @return Parsed date.
     * @throws MeowMeowException If the argument is empty or cannot be parsed.
     */
    public static TaskDateTime parseDate(String arguments) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("Which date? Try: on 2019-12-02");
        }
        return TaskDateTime.parse(arguments);
    }
}