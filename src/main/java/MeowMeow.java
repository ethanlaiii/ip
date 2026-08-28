import java.time.LocalDate;
import java.util.ArrayList;
public class MeowMeow {

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();

        ui.showWelcome();

        ArrayList<Task> taskList;
        try {
            taskList = storage.load();
        } catch (MeowMeowException e) {
            ui.showLoadingError(e.getMessage());
            taskList = new ArrayList<>();
        }

        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readCommand();
            String[] words = input.split("\\s+", 2);
            String commandWord = words[0];
            CommandType command = CommandType.fromString(commandWord);
            String arguments = (words.length > 1) ? words[1].trim() : "";

            try {
                if (input.isEmpty()) {
                    throw new MeowMeowException("You didn't type anything. Give me something to work with!");
                }

                switch (command) {
                case BYE -> {
                    ui.showFarewell();
                    isRunning = false;
                }
                case LIST -> {
                    ui.showList(taskList);
                }
                case MARK -> {
                    int index = parseIndex(arguments, taskList.size(), "mark");
                    taskList.get(index).markAsDone();
                    storage.save(taskList);
                    ui.showTaskMessage("Nice! I've marked this task as done:", taskList.get(index));
                }
                case UNMARK -> {
                    int index = parseIndex(arguments, taskList.size(), "unmark");
                    taskList.get(index).markAsNotDone();
                    storage.save(taskList);
                    ui.showTaskMessage("OK, I've marked this task as not done yet:", taskList.get(index));
                }
                case DELETE -> {
                    int index = parseIndex(arguments, taskList.size(), "delete");
                    Task removed = taskList.remove(index);
                    storage.save(taskList);
                    ui.showRemoved(removed, taskList.size());
                }
                case TODO -> {
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("A todo needs a description. Try: todo borrow book");
                    }
                    taskList.add(new Todo(arguments));
                    storage.save(taskList);
                    ui.showAdded(taskList.getLast(), taskList.size());
                }
                case DEADLINE -> {
                    String[] parts = arguments.split("/by", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new MeowMeowException("A deadline needs a description. "
                                + "Try: deadline return book /by Sunday");
                    }
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new MeowMeowException("I need a due date after /by. "
                                + "Try: deadline return book /by Sunday");
                    }
                    TaskDateTime by = TaskDateTime.parse(parts[1].trim());
                    taskList.add(new Deadline(description, by));
                    storage.save(taskList);
                    ui.showAdded(taskList.getLast(), taskList.size());
                }
                case EVENT -> {
                    String[] fromParts = arguments.split("/from", 2);
                    String description = fromParts[0].trim();
                    if (description.isEmpty()) {
                        throw new MeowMeowException("An event needs a description. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    if (fromParts.length < 2) {
                        throw new MeowMeowException("I need a start time after /from. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    String[] toParts = fromParts[1].split("/to", 2);
                    String fromText = toParts[0].trim();
                    if (fromText.isEmpty()) {
                        throw new MeowMeowException("The start time after /from is empty. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    if (toParts.length < 2 || toParts[1].trim().isEmpty()) {
                        throw new MeowMeowException("I need an end time after /to. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    TaskDateTime from = TaskDateTime.parse(fromText);
                    TaskDateTime to = TaskDateTime.parse(toParts[1].trim());
                    taskList.add(new Event(description, from, to));
                    storage.save(taskList);
                    ui.showAdded(taskList.getLast(), taskList.size());
                }
                case ON -> {
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("Which date? Try: on 2019-12-02");
                    }
                    LocalDate date = TaskDateTime.parse(arguments).toLocalDate();
                    ArrayList<Task> matches = new ArrayList<>();
                    for (Task task : taskList) {
                        if (task.occursOn(date)) {
                            matches.add(task);
                        }
                    }
                    ui.showTasksOn(date, matches);
                }
                case UNKNOWN -> {
                    throw new MeowMeowException("I don't know what \"" + commandWord + "\" means. "
                            + "I understand: todo, deadline, event, list, mark, unmark, delete, on, bye");
                }
                }
            } catch (MeowMeowException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    private static int parseIndex(String arguments, int taskCount, String command) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("Which task? Give me a number, e.g. " + command + " 2");
        }
        if (taskCount == 0) {
            throw new MeowMeowException("Your list is empty, so there's nothing to " + command + ".");
        }
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new MeowMeowException("\"" + arguments + "\" isn't a number. Try: " + command + " 2");
        }

        if (index < 0 || index >= taskCount) {
            throw new MeowMeowException("There's no task " + (index + 1) + ". You have " + taskCount + " task(s).");
        }
        return index;
    }

}
