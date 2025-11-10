package org.greenpiggybank.main;

import org.greenpiggybank.classes.gameLogic.Alternative;
import org.greenpiggybank.classes.gameLogic.Phase;
import org.greenpiggybank.classes.gameLogic.Question;
import org.greenpiggybank.classes.gameLogic.Utility;
import org.greenpiggybank.classes.player.Player;
import org.greenpiggybank.database.AlternativeDAO;
import org.greenpiggybank.database.CreateTable;
import org.greenpiggybank.database.PhaseDAO;
import org.greenpiggybank.database.PlayerDAO;
import org.greenpiggybank.database.QuestionDAO;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class Main {

    private static PlayerDAO playerDAO = new PlayerDAO();
    private static QuestionDAO questionDAO = new QuestionDAO();
    private static PhaseDAO phaseDAO = new PhaseDAO();
    private static AlternativeDAO alternativeDAO = new AlternativeDAO();

    public static void main(String[] args) {
        CreateTable.createPlayersTable(playerDAO.getConexaoBanco().getConexao());
        CreateTable.createPhasesTable(phaseDAO.getConexaoBanco().getConexao());
        CreateTable.createQuestionsTable(questionDAO.getConexaoBanco().getConexao());
        CreateTable.createAlternativesTable(alternativeDAO.getConexaoBanco().getConexao());

        try (Connection conn = phaseDAO.getConexaoBanco().getConexao()) {
            CreateTable.populateInitialData(conn);
        } catch (Exception e) {
            System.out.println("Ops! Não foi possível inserir os dados iniciais: " + e.getMessage());
        }

        Utility.limparTela();
        Utility.printBanner("🪙  BEM-VINDO AO COFRINHO VERDE  🪙", "Educação Financeira Gamificada");

        // 2. Login ou Criação do Jogador
        String nomeJogador = Utility.lerEntrada("\nDigite seu nome de usuário para começar:");
        Player jogador = playerDAO.getOrCreatePlayer(nomeJogador);

        System.out.println(Utility.colorize("\nOlá, " + jogador.getNome() + "! É bom ter você aqui.", Utility.BOLD, Utility.COLOR_GREEN));
        Utility.lerEntrada(Utility.colorize("Pressione ENTER para ver as fases...", Utility.COLOR_WHITE));

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
        Utility.showLoading("Carregando fases");

        Utility.limparTela();
        System.out.println(Utility.colorize("--- MENU PRINCIPAL ---", Utility.BOLD, Utility.COLOR_CYAN));
        System.out.println(Utility.colorize(jogador.toString(), Utility.COLOR_GREEN));
        System.out.println(Utility.colorize("-------------------------", Utility.COLOR_CYAN));
        System.out.println(Utility.colorize("Escolha uma fase para aprender e ganhar:", Utility.BOLD, Utility.COLOR_WHITE));

        // 4. Busca todas as fases do banco de dados
        List<Phase> fases = phaseDAO.findAll();

        if (fases.isEmpty()) {
            System.out.println(Utility.colorize("\nNenhuma fase cadastrada no momento.", Utility.COLOR_RED, Utility.BOLD));
            System.out.println(Utility.colorize("Por favor, avise um administrador.", Utility.COLOR_WHITE));
            Utility.lerEntrada(Utility.colorize("\nPressione ENTER para sair.", Utility.COLOR_WHITE));
            System.exit(0); // Sai se não houver fases
            return;
        }

        System.out.println();
        for (Phase f : fases) {
            System.out.println(Utility.formatMenuOption(f.getId(), f.getNomeDaFase()));
        }
        System.out.println("\n" + Utility.colorize(" 0) Sair do Jogo", Utility.COLOR_WHITE));

        // 5. Processa a escolha do usuário
        int escolha = Utility.lerInt(Utility.colorize("\nSua escolha:", Utility.BOLD, Utility.COLOR_WHITE));

        if (escolha == 0) {
            System.out.println(Utility.colorize("\nObrigado por jogar! Conhecimento é o melhor investimento.", Utility.COLOR_GREEN, Utility.BOLD));
            System.exit(0);
        }

        Phase faseEscolhida = phaseDAO.findById(escolha);

        if (faseEscolhida != null) {
            // Se a fase existe, inicia ela
            iniciarFase(jogador, faseEscolhida);
        } else {
            System.out.println(Utility.colorize("Opção inválida! Tente novamente.", Utility.COLOR_RED, Utility.BOLD));
            Utility.lerEntrada(Utility.colorize("Pressione ENTER para continuar...", Utility.COLOR_WHITE));
        }
    }

    /**
     * Inicia a lógica de uma fase específica (perguntas e respostas).
     * @param jogador O jogador.
     * @param fase A fase escolhida.
     */
    private static void iniciarFase(Player jogador, Phase fase) {
        Utility.limparTela();
        System.out.println(Utility.colorize("🚀 INICIANDO: " + fase.getNomeDaFase().toUpperCase() + " 🚀", Utility.BOLD, Utility.COLOR_MAGENTA));
        Utility.printWrapped(fase.getDescricao());
        if (fase.getNarrativa() != null && !fase.getNarrativa().isBlank()) {
            System.out.println();
            Utility.printWrapped(fase.getNarrativa());
        }
        Utility.lerEntrada(Utility.colorize("\nPressione ENTER para a primeira pergunta...", Utility.COLOR_WHITE));

        // 6. Busca todas as perguntas daquela fase
        List<Question> perguntas = questionDAO.findByFaseId(fase.getId());

        if (perguntas.isEmpty()) {
            System.out.println(Utility.colorize("Esta fase ainda não possui perguntas cadastradas.", Utility.COLOR_RED, Utility.BOLD));
            Utility.lerEntrada(Utility.colorize("Pressione ENTER para voltar ao menu...", Utility.COLOR_WHITE));
            return;
        }

        // 7. Loop de Perguntas
        for (Question p : perguntas) {
            jogarPergunta(jogador, p);
        }

        // 8. Fim da Fase
        Utility.limparTela();
        System.out.println(Utility.colorize("🎉 FASE CONCLUÍDA: " + fase.getNomeDaFase() + " 🎉", Utility.BOLD, Utility.COLOR_GREEN));
        System.out.println(Utility.colorize("Parabéns, " + jogador.getNome() + "!", Utility.COLOR_GREEN));
        System.out.println(Utility.colorize("Seu novo saldo é: R$" + String.format("%.2f", jogador.getSaldo()), Utility.BOLD, Utility.COLOR_YELLOW));
        Utility.lerEntrada(Utility.colorize("\nPressione ENTER para voltar ao menu...", Utility.COLOR_WHITE));
    }

    /**
     * Exibe uma única pergunta, processa a resposta e atualiza o saldo.
     * @param jogador O jogador.
     * @param pergunta A pergunta a ser jogada.
     */
    private static void jogarPergunta(Player jogador, Question pergunta) {
        Utility.limparTela();
        System.out.println(Utility.colorize(jogador.toString(), Utility.COLOR_GREEN)); // Status (Nome e Saldo)
        System.out.println(Utility.colorize("-------------------------", Utility.COLOR_CYAN));
        System.out.println(Utility.colorize("PERGUNTA:", Utility.BOLD, Utility.COLOR_WHITE));
        Utility.printWrapped(pergunta.getTextoPergunta());
        System.out.println(Utility.colorize("\nRECOMPENSA: R$" + String.format("%.2f", pergunta.getRecompensa()), Utility.COLOR_YELLOW, Utility.BOLD));
        System.out.println(Utility.colorize("-------------------------", Utility.COLOR_CYAN));

        List<Alternative> alternativas = pergunta.getAlternativas();

        // Embaralha as alternativas para o jogo não ficar repetitivo
        Collections.shuffle(alternativas);

        for (int i = 0; i < alternativas.size(); i++) {
            // Exibe (1), (2), (3)...
            System.out.println(Utility.formatMenuOption(i + 1, alternativas.get(i).getTextoAlternativa()));
        }

        // 9. Validação da Resposta
        int respostaNum = 0;
        while (respostaNum < 1 || respostaNum > alternativas.size()) {
            respostaNum = Utility.lerInt(Utility.colorize("\nEscolha a alternativa (1-" + alternativas.size() + "):", Utility.BOLD, Utility.COLOR_WHITE));
            if (respostaNum < 1 || respostaNum > alternativas.size()) {
                System.out.println(Utility.colorize("Opção inválida. Tente novamente.", Utility.COLOR_RED, Utility.BOLD));
            }
        }

        // 10. Checagem e Recompensa
        Alternative respostaEscolhida = alternativas.get(respostaNum - 1); // -1 pois a lista começa em 0

        if (respostaEscolhida.isCorreta()) {
            System.out.println(Utility.colorize("\n✅ CORRETO!", Utility.BOLD, Utility.COLOR_GREEN));
            System.out.println(Utility.colorize("Você ganhou + R$" + String.format("%.2f", pergunta.getRecompensa()), Utility.COLOR_YELLOW));

            // Atualiza o objeto do jogador
            jogador.setSaldo(jogador.getSaldo() + pergunta.getRecompensa());

            // SALVA A MUDANÇA NO BANCO DE DADOS
            playerDAO.updateSaldo(jogador);

        } else {
            System.out.println(Utility.colorize("\n❌ ERRADO!", Utility.BOLD, Utility.COLOR_RED));
            // Mostra qual era a resposta correta
            for (Alternative alt : alternativas) {
                if (alt.isCorreta()) {
                    System.out.println(Utility.colorize("A resposta certa era: " + alt.getTextoAlternativa(), Utility.COLOR_WHITE));
                    break;
                }
            }
        }

        Utility.lerEntrada(Utility.colorize("\nPressione ENTER para a próxima pergunta...", Utility.COLOR_WHITE));
    }
}
