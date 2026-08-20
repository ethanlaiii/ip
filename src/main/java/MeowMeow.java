import java.util.Scanner;

public class MeowMeow {
    public static void main(String[] args) {
        String logo = "  /\\_/\\\n"
                + " ( o.o )\n"
                + "  > ^ <\n";
        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println(logo);
        System.out.println("Hello! I'm MeowMeow.");
        System.out.println("What can I do for you? Meow :>");
        System.out.println(line);

        String[] taskList = new String[100];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println(line);
                System.out.println("     Bye. Hope to see you again soon! Meow :>");
                System.out.println(line);
                break;
            } else if (input.equals("list")) {
                System.out.println(line);
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + taskList[i]);
                }
                System.out.println(line);
            } else {
                taskList[taskCount] = input;
                taskCount++;
                System.out.println(line);
                System.out.println("     Meow :> added: " + input);
                System.out.println(line);
            }
        }

        scanner.close();
    }
}
