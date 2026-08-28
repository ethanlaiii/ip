package meowmeow;

import java.time.LocalDate;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        // mark done task with X
        return (isDone ? "X" : " ");
    }

    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns whether this task's description contains the given text.
     *
     * @param lowerKeyword Search text, already converted to lower case.
     * @return True if the description contains the text, ignoring case.
     */
    public boolean matches(String lowerKeyword) {
        return description.toLowerCase().contains(lowerKeyword);
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
