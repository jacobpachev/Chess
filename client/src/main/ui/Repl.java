package ui;

import java.util.Scanner;

public class Repl {
    public void run() {

        System.out.println("Welcome! Here are the valid commands");
        System.out.println("Help");
        var scanner = new Scanner(System.in);
        String command = "";
        while (!command.equalsIgnoreCase("quit")) {
            System.out.print("\n>>>");
            var line = scanner.nextLine();
            try {
                command = line; //Later client should evaluate the command
                System.out.println(command);
            }
            catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
