package hr.fer.tinf.lab.zad3;

import java.util.Scanner;

public class ConsoleUI {
    public static void main(String... arguments) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print(">");
            System.out.flush();
            String commandString = sc.next();
            if ("exit".equals(commandString)) break;
        }
        sc.close();
    }
}
