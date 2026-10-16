import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class Simulador {

    private final Algoritmo algoritmo;
    private final int quantum;
    private final boolean mostrarLinhaDoTempo;

    public Simulador(Algoritmo algoritmo, int quantum, boolean mostrarLinhaDoTempo) {
        this.algoritmo = algoritmo;
        this.quantum = quantum;
        this.mostrarLinhaDoTempo = mostrarLinhaDoTempo;
    }

    public Resultado executar(List<Processo> originais) {
        // Trabalha com copias para nao alterar os processos cadastrados
        List<Processo> processos = new ArrayList<>();
        for (Processo p : originais) {
            processos.add(p.copia());
        }
        processos.sort(Comparator.comparingInt(Processo::getChegada).thenComparingInt(Processo::getId));

        Deque<Processo> filaProntos = new ArrayDeque<>();
        List<Processo> bloqueados = new ArrayList<>();
        List<String> linhaGantt = new ArrayList<>();   // quem usou a CPU em cada instante

        Processo atual = null;           // processo na CPU
        Processo ultimoExecutado = null; // para contar trocas de contexto
        Processo voltaParaFila = null;   // processo que esgotou o quantum
        int quantumUsado = 0;
        int tempo = 0;
        int finalizados = 0;
        int tempoOcupado = 0;
        int trocas = 0;

        while (finalizados < processos.size()) {

            for (Processo p : processos) {
                if (p.getEstado() == Estado.NOVO && p.getChegada() == tempo) {
                    p.setEstado(Estado.PRONTO);
                    filaProntos.addLast(p);
                    log(tempo, p.getNome() + " chegou a fila de prontos");
                }
            }

            Iterator<Processo> it = bloqueados.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.getFimBloqueio() == tempo) {
                    it.remove();
                    p.setEstado(Estado.PRONTO);
                    filaProntos.addLast(p);
                    log(tempo, p.getNome() + " terminou a E/S e voltou para a fila de prontos (BLOQUEADO -> PRONTO)");
                }
            }

            if (voltaParaFila != null) {
                filaProntos.addLast(voltaParaFila);
                voltaParaFila = null;
            }

            if (atual == null && !filaProntos.isEmpty()) {
                atual = escolherProximo(filaProntos);
                filaProntos.remove(atual);
                atual.setEstado(Estado.EM_EXECUCAO);
                quantumUsado = 0;

                if (ultimoExecutado != null && ultimoExecutado != atual) {
                    trocas++;
                }
                ultimoExecutado = atual;

                if (atual.getInicio() == -1) {
                    atual.setInicio(tempo);
                }
                log(tempo, atual.getNome() + " entrou em execucao (PRONTO -> EM_EXECUCAO)");
            } else if (atual != null) {
                log(tempo, atual.getNome() + " continua em execucao");
            }

            if (atual == null) {
                log(tempo, "CPU ociosa");
                linhaGantt.add("--");
                tempo++;
                continue;
            }
            atual.executarUmaUnidade();
            linhaGantt.add(atual.getNome());
            tempoOcupado++;
            quantumUsado++;
            tempo++;

            if (atual.terminou()) {
                atual.setEstado(Estado.FINALIZADO);
                atual.setConclusao(tempo);
                finalizados++;
                log(tempo, atual.getNome() + " concluido (EM_EXECUCAO -> FINALIZADO)");
                atual = null;
            } else if (atual.devePedirES()) {
                atual.bloquear(tempo);
                bloqueados.add(atual);
                log(tempo, atual.getNome() + " foi bloqueado por E/S ate o tempo "
                        + atual.getFimBloqueio() + " (EM_EXECUCAO -> BLOQUEADO)");
                atual = null;
            } else if (algoritmo == Algoritmo.ROUND_ROBIN && quantumUsado == quantum) {
                atual.setEstado(Estado.PRONTO);
                voltaParaFila = atual;
                log(tempo, atual.getNome() + " esgotou o quantum e voltou para o fim da fila (EM_EXECUCAO -> PRONTO)");
                atual = null;
            }
        }

        if (mostrarLinhaDoTempo) {
            System.out.println("tempo " + tempo + ": todos os processos foram concluidos");
        }

        processos.sort(Comparator.comparingInt(Processo::getId));
        return new Resultado(algoritmo, processos, tempo, tempoOcupado, trocas, montarGantt(linhaGantt));
    }

    private Processo escolherProximo(Deque<Processo> fila) {
        switch (algoritmo) {
            case SJF:
                return fila.stream().min(Comparator.comparingInt(Processo::getRestante)).get();
            case PRIORIDADE:
                return fila.stream().min(Comparator.comparingInt(Processo::getPrioridade)).get();
            case FCFS:
            case ROUND_ROBIN:
            default:
                return fila.peekFirst();
        }
    }

    private void log(int tempo, String mensagem) {
        if (mostrarLinhaDoTempo) {
            System.out.println("tempo " + tempo + ": " + mensagem);
        }
    }

    private String montarGantt(List<String> linha) {
        StringBuilder barras = new StringBuilder();
        StringBuilder tempos = new StringBuilder();
        int inicio = 0;
        for (int i = 1; i <= linha.size(); i++) {
            if (i == linha.size() || !linha.get(i).equals(linha.get(inicio))) {
                String rotulo = linha.get(inicio);
                int largura = Math.max(rotulo.length() + 2, String.valueOf(inicio).length() + 1);
                barras.append("|").append(centralizar(rotulo, largura));
                tempos.append(String.format("%-" + (largura + 1) + "s", inicio));
                inicio = i;
            }
        }
        barras.append("|");
        tempos.append(linha.size());
        return barras + "\n" + tempos;
    }

    private String centralizar(String texto, int largura) {
        int esquerda = (largura - texto.length()) / 2;
        int direita = largura - texto.length() - esquerda;
        return " ".repeat(esquerda) + texto + " ".repeat(direita);
    }
}
