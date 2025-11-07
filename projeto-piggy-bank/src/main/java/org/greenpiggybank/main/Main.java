package org.greenpiggybank.main;

import org.greenpiggybank.classes.gameLogic.Alternative;
import org.greenpiggybank.database.PhaseDAO;
import org.greenpiggybank.database.PlayerDAO;
import org.greenpiggybank.classes.player.Player;
import org.greenpiggybank.database.QuestionDAO;
import org.greenpiggybank.classes.gameLogic.Question;
import org.greenpiggybank.classes.gameLogic.Utility;
import org.greenpiggybank.classes.gameLogic.Phase;


import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Main {

    private static PlayerDAO playerDAO = new PlayerDAO();
    private static QuestionDAO questionDAO = new QuestionDAO();
    private static PhaseDAO phaseDAO = new PhaseDAO();

    public static void main(String[] args) {
        Utility.limparTela();
        System.out.println("======================================");
        System.out.println("🪙  BEM-VINDO AO COFRINHO VERDE  🪙");
        System.out.println("    Educação Financeira Gamificada");
        System.out.println("======================================");

        // 2. Login ou Criação do Jogador
        String nomeJogador = Utility.lerEntrada("\nDigite seu nome de usuário para começar:");
        Player jogador = playerDAO.getOrCreatePlayer(nomeJogador);

        System.out.println("\nOlá, " + jogador.getNome() + "! É bom ter você aqui.");
        Utility.lerEntrada("Pressione ENTER para ver as fases...");

        // 3. Loop Principal do Jogo (Menu de Fases)
        while (true) {
            mostrarMenuFases(jogador);
        }
    }

    /**
     * Mostra o menu principal com a lista de fases.
     * @param jogador O jogador logado.
     */
    private static void mostrarMenuFases(Player jogador) {
        Utility.limparTela();
        System.out.println("--- MENU PRINCIPAL ---");
        // Exibe o status atual do jogador (Nome e Saldo)
        System.out.println(jogador);
        System.out.println("-------------------------");
        System.out.println("Escolha uma fase para aprender e ganhar:");

        // 4. Busca todas as fases do banco de dados
        List<Phase> fases = phaseDAO.findAll();

        if (fases.isEmpty()) {
            System.out.println("\nNenhuma fase cadastrada no momento.");
            System.out.println("Por favor, avise um administrador.");
            Utility.lerEntrada("\nPressione ENTER para sair.");
            System.exit(0); // Sai se não houver fases
            return;
        }

        // Exibe as fases disponíveis
        for (Phase f : fases) {
            System.out.println(f.getId() + ") " + f.getNomeDaFase());
        }
        System.out.println("\n0) Sair do Jogo");

        // 5. Processa a escolha do usuário
        int escolha = Utility.lerInt("\nSua escolha:");

        if (escolha == 0) {
            System.out.println("\nObrigado por jogar! Conhecimento é o melhor investimento.");
            System.exit(0);
        }

        Phase faseEscolhida = phaseDAO.findById(escolha);

        if (faseEscolhida != null) {
            // Se a fase existe, inicia ela
            iniciarFase(jogador, faseEscolhida);
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            Utility.lerEntrada("Pressione ENTER para continuar...");
        }
    }

    /**
     * Inicia a lógica de uma fase específica (perguntas e respostas).
     * @param jogador O jogador.
     * @param fase A fase escolhida.
     */
    private static void iniciarFase(Player jogador, Phase fase) {
        Utility.limparTela();
        System.out.println("🚀 INICIANDO: " + fase.getNomeDaFase().toUpperCase() + " 🚀");
        System.out.println(fase.getDescricao());
        Utility.lerEntrada("\nPressione ENTER para a primeira pergunta...");

        // 6. Busca todas as perguntas daquela fase
        List<Question> perguntas = questionDAO.findByFaseId(fase.getId());

        if (perguntas.isEmpty()) {
            System.out.println("Esta fase ainda não possui perguntas cadastradas.");
            Utility.lerEntrada("Pressione ENTER para voltar ao menu...");
            return;
        }

        // 7. Loop de Perguntas
        for (Question p : perguntas) {
            jogarPergunta(jogador, p);
        }

        // 8. Fim da Fase
        Utility.limparTela();
        System.out.println("🎉 FASE CONCLUÍDA: " + fase.getNomeDaFase() + " 🎉");
        System.out.println("Parabéns, " + jogador.getNome() + "!");
        System.out.println("Seu novo saldo é: R$" + String.format("%.2f", jogador.getSaldo()));
        Utility.lerEntrada("\nPressione ENTER para voltar ao menu...");
    }

    /**
     * Exibe uma única pergunta, processa a resposta e atualiza o saldo.
     * @param jogador O jogador.
     * @param pergunta A pergunta a ser jogada.
     */
    private static void jogarPergunta(Player jogador, Question pergunta) {
        Utility.limparTela();
        System.out.println(jogador); // Status (Nome e Saldo)
        System.out.println("-------------------------");
        System.out.println("PERGUNTA:");
        System.out.println(pergunta.getTextoPergunta());
        System.out.println("\nRECOMPENSA: R$" + String.format("%.2f", pergunta.getRecompensa()));
        System.out.println("-------------------------");

        List<Alternative> alternativas = pergunta.getAlternativas();

        // Embaralha as alternativas para o jogo não ficar repetitivo
        Collections.shuffle(alternativas);

        for (int i = 0; i < alternativas.size(); i++) {
            // Exibe (1), (2), (3)...
            System.out.println((i + 1) + ") " + alternativas.get(i).getTextoAlternativa());
        }

        // 9. Validação da Resposta
        int respostaNum = 0;
        while (respostaNum < 1 || respostaNum > alternativas.size()) {
            respostaNum = Utility.lerInt("\nEscolha a alternativa (1-" + alternativas.size() + "):");
            if (respostaNum < 1 || respostaNum > alternativas.size()) {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }

        // 10. Checagem e Recompensa
        Alternative respostaEscolhida = alternativas.get(respostaNum - 1); // -1 pois a lista começa em 0

        if (respostaEscolhida.isCorreta()) {
            System.out.println("\n✅ CORRETO!");
            System.out.println("Você ganhou + R$" + String.format("%.2f", pergunta.getRecompensa()));

            // Atualiza o objeto do jogador
            jogador.setSaldo(jogador.getSaldo() + pergunta.getRecompensa());

            // SALVA A MUDANÇA NO BANCO DE DADOS
            playerDAO.updateSaldo(jogador);

        } else {
            System.out.println("\n❌ ERRADO!");
            // Mostra qual era a resposta correta
            for (Alternative alt : alternativas) {
                if (alt.isCorreta()) {
                    System.out.println("A resposta certa era: " + alt.getTextoAlternativa());
                    break;
                }
            }
        }

        Utility.lerEntrada("\nPressione ENTER para a próxima pergunta...");
    }
}
