package meowmeow;

import javafx.application.Application;
import meowmeow.ui.Main;

/**
 * A launcher class to workaround classpath issues.
 * <p>
 * Reused from the SE-EDU JavaFX tutorial, part 1
 * (https://se-education.org/guides/tutorials/javaFxPart1.html),
 * with the package declaration added.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}