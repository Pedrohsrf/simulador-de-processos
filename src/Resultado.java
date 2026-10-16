import java.util.List;

public class Resultado {

    private final Algoritmo algoritmo;
    private final List<Processo> processos;
    private final int tempoTotal;
    private final int tempoOcupado;
    private final int trocasDeContexto;
    private final String gantt;

    public Resultado(Algoritmo algoritmo, List<Processo> processos, int tempoTotal,
                     int tempoOcupado, int trocasDeContexto, String gantt) {
        this.algoritmo = algoritmo;
        this.processos = processos;
        this.tempoTotal = tempoTotal;
        this.tempoOcupado = tempoOcupado;
        this.trocasDeContexto = trocasDeContexto;
        this.gantt = gantt;
    }

    public double getTurnaroundMedio() {
        return processos.stream().mapToInt(Processo::getTurnaround).average().orElse(0);
    }

    public double getRespostaMedia() {
        return processos.stream().mapToInt(Processo::getTempoResposta).average().orElse(0);
    }

    public double getEsperaMedia() {
        return processos.stream().mapToInt(Processo::getTempoEspera).average().orElse(0);
    }

    public int getConcluidos() {
        return (int) processos.stream().filter(p -> p.getEstado() == Estado.FINALIZADO).count();
    }

    public double getUtilizacaoCPU() {
        return tempoTotal == 0 ? 0 : 100.0 * tempoOcupado / tempoTotal;
    }

    public double getThroughput() {
        return tempoTotal == 0 ? 0 : (double) getConcluidos() / tempoTotal;
    }

    public Algoritmo getAlgoritmo() { return algoritmo; }
    public int getTempoTotal() { return tempoTotal; }
    public int getTrocasDeContexto() { return trocasDeContexto; }

    public void imprimir() {
        System.out.println();
        System.out.println("Diagrama de Gantt:");
        System.out.println(gantt);
        System.out.println();
        System.out.println("+------+---------+-------+-----------+------------+----------+--------+");
        System.out.println("| Proc | Chegada | Burst | Conclusao | Turnaround | Resposta | Espera |");
        System.out.println("+------+---------+-------+-----------+------------+----------+--------+");
        for (Processo p : processos) {
            System.out.printf("| %-4s | %7d | %5d | %9d | %10d | %8d | %6d |%n",
                    p.getNome(), p.getChegada(), p.getTempoTotal(), p.getConclusao(),
                    p.getTurnaround(), p.getTempoResposta(), p.getTempoEspera());
        }
        System.out.println("+------+---------+-------+-----------+------------+----------+--------+");
        System.out.println();
        System.out.println("Metricas - " + algoritmo.getDescricao());
        System.out.printf("  Tempo de retorno medio (turnaround): %.2f%n", getTurnaroundMedio());
        System.out.printf("  Tempo de resposta medio:             %.2f%n", getRespostaMedia());
        System.out.printf("  Tempo de espera medio:               %.2f%n", getEsperaMedia());
        System.out.printf("  Processos concluidos:                %d%n", getConcluidos());
        System.out.printf("  Tempo total da simulacao:            %d%n", tempoTotal);
        System.out.printf("  Utilizacao da CPU:                   %.2f%%%n", getUtilizacaoCPU());
        System.out.printf("  Throughput:                          %.3f processos/u.t.%n", getThroughput());
        System.out.printf("  Trocas de contexto:                  %d%n", trocasDeContexto);
    }
}
