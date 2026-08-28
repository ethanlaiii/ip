import java.time.LocalDate;
import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public ArrayList<Task> asList() {
        return new ArrayList<>(tasks);
    }

    public Task add(Task task) {
        tasks.add(task);
        return task;
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    public ArrayList<Task> findOccurringOn(LocalDate date) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }
        return matches;
    }

    public int parseIndex(String arguments, String command) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("Which task? Give me a number, e.g. " + command + " 2");
        }
        if (tasks.isEmpty()) {
            throw new MeowMeowException("Your list is empty, so there's nothing to " + command + ".");
        }
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new MeowMeowException("\"" + arguments + "\" isn't a number. Try: " + command + " 2");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new MeowMeowException("There's no task " + (index + 1) + ". You have "
                    + tasks.size() + " task(s).");
        }
        return index;
    }
}