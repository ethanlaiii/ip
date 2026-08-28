package meowmeow;

public class Parser {

    public static CommandType parseCommand(String input) {
        String[] words = input.trim().split("\\s+", 2);
        return CommandType.fromString(words[0]);
    }

    public static String parseCommandWord(String input) {
        String[] words = input.trim().split("\\s+", 2);
        return words[0];
    }

    public static String parseArguments(String input) {
        String[] words = input.trim().split("\\s+", 2);
        return (words.length > 1) ? words[1].trim() : "";
    }

    public static Todo parseTodo(String arguments) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("A todo needs a description. Try: todo borrow book");
        }
        return new Todo(arguments);
    }

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

    public static TaskDateTime parseDate(String arguments) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("Which date? Try: on 2019-12-02");
        }
        return TaskDateTime.parse(arguments);
    }
}