package meowmeow;

import java.time.LocalDate;

public class Deadline extends Task {
    protected TaskDateTime by;

    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by.toStorageFormat();
    }
}
