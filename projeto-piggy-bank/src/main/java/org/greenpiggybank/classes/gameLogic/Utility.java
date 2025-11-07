package org.greenpiggybank.classes.gameLogic;

import java.util.Scanner;

public class Utility {

    private static Scanner scanner = new Scanner(System.in);

    // Metodo para ler um texto do usuário
    public static String lerEntrada(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    // Metodo para ler um número (com validação simples)
    public static int lerInt(String prompt) {
        while (true) {
            try {
                String entrada = lerEntrada(prompt);
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    // Metodo "gambiarra" para limpar o console (funciona na maioria dos terminais)
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
