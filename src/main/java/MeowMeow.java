import java.time.LocalDate;

public class MeowMeow {

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();

        ui.showWelcome();

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (MeowMeowException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
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
                    ui.showList(tasks.asList());
                }
                case MARK -> {
                    int index = tasks.parseIndex(arguments, "mark");
                    tasks.markAsDone(index);
                    storage.save(tasks.asList());
                    ui.showTaskMessage("Nice! I've marked this task as done:", tasks.get(index));
                }
                case UNMARK -> {
                    int index = tasks.parseIndex(arguments, "unmark");
                    tasks.markAsNotDone(index);
                    storage.save(tasks.asList());
                    ui.showTaskMessage("OK, I've marked this task as not done yet:", tasks.get(index));
                }
                case DELETE -> {
                    int index = tasks.parseIndex(arguments, "delete");
                    Task removed = tasks.delete(index);
                    storage.save(tasks.asList());
                    ui.showRemoved(removed, tasks.size());
                }
                case TODO -> {
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("A todo needs a description. Try: todo borrow book");
                    }
                    Task added = tasks.add(new Todo(arguments));
                    storage.save(tasks.asList());
                    ui.showAdded(added, tasks.size());
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
                    Task added = tasks.add(new Deadline(description, by));
                    storage.save(tasks.asList());
                    ui.showAdded(added, tasks.size());
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
                    Task added = tasks.add(new Event(description, from, to));
                    storage.save(tasks.asList());
                    ui.showAdded(added, tasks.size());
                }
                case ON -> {
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("Which date? Try: on 2019-12-02");
                    }
                    LocalDate date = TaskDateTime.parse(arguments).toLocalDate();
                    ui.showTasksOn(date, tasks.findOccurringOn(date));
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


}
