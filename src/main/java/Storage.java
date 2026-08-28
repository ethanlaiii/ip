import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public ArrayList<Task> load() throws MeowMeowException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            int corruptedCount = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task task = parseLine(line);
                if (task == null) {
                    corruptedCount++;
                } else {
                    tasks.add(task);
                }
            }

            if (corruptedCount > 0) {
                System.out.println("     Skipped " + corruptedCount
                        + " unreadable line(s) in the save file.");
            }
        } catch (IOException e) {
            throw new MeowMeowException("I couldn't read your save file: " + e.getMessage());
        }

        return tasks;
    }

    private Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        String doneFlag = parts[1];
        String description = parts[2];

        if (description.isEmpty()) {
            return null;
        }
        if (!doneFlag.equals("0") && !doneFlag.equals("1")) {
            return null;
        }

        Task task;
        try {
            switch (type) {
                case "T" -> task = new Todo(description);
                case "D" -> {
                    if (parts.length < 4 || parts[3].isEmpty()) {
                        return null;
                    }
                    task = new Deadline(description, TaskDateTime.fromStorage(parts[3]));
                }
                case "E" -> {
                    if (parts.length < 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                        return null;
                    }
                    task = new Event(description, TaskDateTime.fromStorage(parts[3]), TaskDateTime.fromStorage(parts[4]));
                }
                default -> {
                    return null;
                }
            }
        } catch (MeowMeowException e) {
            return null;
        }

        if (doneFlag.equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    public void save(ArrayList<Task> tasks) throws MeowMeowException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            StringBuilder builder = new StringBuilder();
            for (Task task : tasks) {
                builder.append(task.toFileFormat()).append(System.lineSeparator());
            }

            Files.writeString(filePath, builder.toString());
        } catch (IOException e) {
            throw new MeowMeowException("I couldn't save your tasks: " + e.getMessage());
        }
    }
}