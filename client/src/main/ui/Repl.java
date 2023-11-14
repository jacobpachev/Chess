package ui;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl() {
        client = new ChessClient("http://localhost:8080");
    }
    public void run() {

        System.out.println("Welcome! Here are the valid commands");
        System.out.println(client.help());
        var scanner = new Scanner(System.in);
        String command = "";
        while (!command.equalsIgnoreCase("quit")) {
            System.out.print("\n>>> ");
            var line = scanner.nextLine();
            try {
                command = client.eval(line);
                if(!command.equalsIgnoreCase("quit")) {
                    System.out.println(command);
                }
            }
            catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
