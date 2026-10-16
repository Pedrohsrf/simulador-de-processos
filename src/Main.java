import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner entrada = new Scanner(System.in);
    private static final List<Processo> processos = new ArrayList<>();
    private static int proximoId = 1;

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println();
            System.out.println("========== SIMULADOR DE ESCALONAMENTO ==========");
            System.out.println("1 - Criar processo");
            System.out.println("2 - Listar processos");
            System.out.println("3 - Remover/encerrar processo");
            System.out.println("4 - Executar processos (escolher algoritmo)");
            System.out.println("5 - Comparar todos os algoritmos");
            System.out.println("6 - Carregar cenario de teste");
            System.out.println("7 - Limpar todos os processos");
            System.out.println("0 - Sair");
            opcao = lerInt("Escolha uma opcao: ", 0, 7);

            switch (opcao) {
                case 1: criarProcesso(); break;
                case 2: listarProcessos(); break;
                case 3: removerProcesso(); break;
                case 4: executar(); break;
                case 5: comparar(); break;
                case 6: carregarCenario(); break;
                case 7: limpar(); break;
                case 0: System.out.println("Encerrando o simulador..."); break;
                default: break;
            }
        } while (opcao != 0);
    }

    private static void criarProcesso() {
        System.out.println("\n--- Novo processo (P" + proximoId + ") ---");
        int chegada = lerInt("Tempo de chegada: ", 0, 1000);
        int tempoTotal = lerInt("Tempo de execucao total: ", 1, 1000);
        int prioridade = lerInt("Prioridade (1 = mais alta): ", 1, 100);

        int momentoES = 0;
        int duracaoES = 0;
        if (tempoTotal > 1 && lerSimNao("O processo faz E/S (sera bloqueado)? (s/n): ")) {
            momentoES = lerInt("Apos quantas unidades de execucao ele pede E/S (1 a " + (tempoTotal - 1) + "): ",
                    1, tempoTotal - 1);
            duracaoES = lerInt("Duracao da E/S (tempo bloqueado): ", 1, 1000);
        }

        Processo p = new Processo(proximoId++, chegada, tempoTotal, prioridade, momentoES, duracaoES);
        processos.add(p);
        System.out.println(p.getNome() + " criado no estado " + p.getEstado()
                + " (entrara em PRONTO quando chegar ao sistema).");
    }

    private static void listarProcessos() {
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo cadastrado.");
            return;
        }
        System.out.println();
        System.out.println("+------+---------+-------+----------+------------+----------------------+--------+");
        System.out.println("| Proc | Chegada | Burst | Restante | Prioridade | E/S                  | Estado |");
        System.out.println("+------+---------+-------+----------+------------+----------------------+--------+");
        for (Processo p : processos) {
            System.out.printf("| %-4s | %7d | %5d | %8d | %10d | %-20s | %-6s |%n",
                    p.getNome(), p.getChegada(), p.getTempoTotal(), p.getRestante(),
                    p.getPrioridade(), p.descricaoES(), p.getEstado());
        }
        System.out.println("+------+---------+-------+----------+------------+----------------------+--------+");
    }

    private static void removerProcesso() {
        if (processos.isEmpty()) {
            System.out.println("\nNenhum processo cadastrado.");
            return;
        }
        listarProcessos();
        int id = lerInt("ID do processo a remover: ", 1, Integer.MAX_VALUE);
        boolean removido = processos.removeIf(p -> p.getId() == id);
        System.out.println(removido ? "P" + id + " encerrado e removido." : "Processo nao encontrado.");
    }

    private static void executar() {
        if (processos.isEmpty()) {
            System.out.println("\nCadastre processos ou carregue um cenario antes de executar.");
            return;
        }
        Algoritmo[] algoritmos = Algoritmo.values();
        System.out.println("\nEscolha o algoritmo:");
        for (int i = 0; i < algoritmos.length; i++) {
            System.out.println((i + 1) + " - " + algoritmos[i].getDescricao());
        }
        Algoritmo escolhido = algoritmos[lerInt("Opcao: ", 1, algoritmos.length) - 1];

        int quantum = 0;
        if (escolhido == Algoritmo.ROUND_ROBIN) {
            quantum = lerInt("Informe o quantum: ", 1, 100);
        }

        System.out.println("\n===== Linha do tempo - " + escolhido.getDescricao()
                + (quantum > 0 ? " (quantum = " + quantum + ")" : "") + " =====");
        Resultado r = new Simulador(escolhido, quantum, true).executar(processos);
        r.imprimir();
    }

    private static void comparar() {
        if (processos.isEmpty()) {
            System.out.println("\nCadastre processos ou carregue um cenario antes de comparar.");
            return;
        }
        int quantum = lerInt("Quantum para o Round Robin: ", 1, 100);

        System.out.println();
        System.out.println("+--------------------+------------+----------+--------+-------+---------+------------+--------+");
        System.out.println("| Algoritmo          | Turnaround | Resposta | Espera | Total | CPU (%) | Throughput | Trocas |");
        System.out.println("+--------------------+------------+----------+--------+-------+---------+------------+--------+");
        for (Algoritmo a : Algoritmo.values()) {
            Resultado r = new Simulador(a, quantum, false).executar(processos);
            String nome = a == Algoritmo.ROUND_ROBIN ? "Round Robin (q=" + quantum + ")" : a.name();
            System.out.printf("| %-18s | %10.2f | %8.2f | %6.2f | %5d | %7.2f | %10.3f | %6d |%n",
                    nome, r.getTurnaroundMedio(), r.getRespostaMedia(), r.getEsperaMedia(),
                    r.getTempoTotal(), r.getUtilizacaoCPU(), r.getThroughput(), r.getTrocasDeContexto());
        }
        System.out.println("+--------------------+------------+----------+--------+-------+---------+------------+--------+");
        System.out.println("Turnaround, Resposta e Espera sao medias. Quanto menores, melhor.");
    }

    private static void carregarCenario() {
        System.out.println("\nCenarios disponiveis:");
        for (int i = 0; i < Cenarios.NOMES.length; i++) {
            System.out.println((i + 1) + " - " + Cenarios.NOMES[i]);
        }
        int numero = lerInt("Escolha o cenario: ", 1, Cenarios.NOMES.length);
        processos.clear();
        processos.addAll(Cenarios.carregar(numero));
        proximoId = processos.size() + 1;
        System.out.println("Cenario " + numero + " carregado.");
        listarProcessos();
    }

    private static void limpar() {
        processos.clear();
        proximoId = 1;
        System.out.println("\nTodos os processos foram removidos.");
    }

    private static int lerInt(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            String linha = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Valor invalido. Digite um numero entre " + min + " e " + max + ".");
        }
    }

    private static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = entrada.nextLine().trim().toLowerCase();
            if (resposta.equals("s")) return true;
            if (resposta.equals("n")) return false;
            System.out.println("Responda com s ou n.");
        }
    }
}
