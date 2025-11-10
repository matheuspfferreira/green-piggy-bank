package org.greenpiggybank.classes.gameLogic;

import java.util.Scanner;

public class Utility {

    private static Scanner scanner = new Scanner(System.in);
    private static final int DEFAULT_WRAP_WIDTH = 80;

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String COLOR_GREEN = "\u001B[32m";
    public static final String COLOR_CYAN = "\u001B[36m";
    public static final String COLOR_YELLOW = "\u001B[33m";
    public static final String COLOR_MAGENTA = "\u001B[35m";
    public static final String COLOR_WHITE = "\u001B[37m";
    public static final String COLOR_RED = "\u001B[31m";

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

    public static String colorize(String texto, String... estilos) {
        if (texto == null) {
            return "";
        }
        StringBuilder prefixo = new StringBuilder();
        for (String estilo : estilos) {
            if (estilo != null) {
                prefixo.append(estilo);
            }
        }
        return prefixo + texto + RESET;
    }

    public static void printBanner(String titulo, String subtitulo) {
        String barra = colorize("======================================", BOLD, COLOR_CYAN);
        System.out.println(barra);
        System.out.println(colorize(titulo, BOLD, COLOR_GREEN));
        if (subtitulo != null && !subtitulo.isBlank()) {
            System.out.println(colorize(subtitulo, ITALIC, COLOR_WHITE));
        }
        System.out.println(barra);
    }

    public static void printSectionTitle(String titulo) {
        System.out.println(colorize("\n" + titulo, BOLD, COLOR_MAGENTA));
    }

    public static void showLoading(String mensagem) {
        showLoading(mensagem, 3, 200);
    }

    public static void showLoading(String mensagem, int passos, long atrasoMs) {
        System.out.print(colorize(mensagem, COLOR_YELLOW, ITALIC));
        for (int i = 0; i < passos; i++) {
            try {
                Thread.sleep(atrasoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            System.out.print(colorize(".", COLOR_YELLOW));
        }
        System.out.println();
    }

    public static String formatMenuOption(int numero, String texto) {
        return String.format("%s %s",
                colorize(String.format("%2d)", numero), BOLD, COLOR_CYAN),
                texto);
    }

    public static String formatBadge(String texto) {
        return colorize(texto, BOLD, COLOR_GREEN);
    }

    public static void printWrapped(String texto) {
        printWrapped(texto, DEFAULT_WRAP_WIDTH);
    }

    public static void printWrapped(String texto, int largura) {
        if (texto == null || texto.isBlank()) {
            return;
        }

        String[] paragrafos = texto.split("\\r?\\n");
        for (int p = 0; p < paragrafos.length; p++) {
            String paragrafo = paragrafos[p].trim();

            if (paragrafo.isEmpty()) {
                System.out.println();
                continue;
            }

            StringBuilder linhaAtual = new StringBuilder();
            for (String palavra : paragrafo.split("\\s+")) {
                if (linhaAtual.length() == 0) {
                    linhaAtual.append(palavra);
                    continue;
                }

                if (linhaAtual.length() + 1 + palavra.length() > largura) {
                    System.out.println(linhaAtual.toString());
                    linhaAtual.setLength(0);
                    linhaAtual.append(palavra);
                } else {
                    linhaAtual.append(' ').append(palavra);
                }
            }

            if (linhaAtual.length() > 0) {
                System.out.println(linhaAtual.toString());
            }

            if (p < paragrafos.length - 1) {
                System.out.println();
            }
        }
    }
}
