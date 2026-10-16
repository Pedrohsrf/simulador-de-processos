public enum Algoritmo {
    FCFS("FCFS (First-Come, First-Served)"),
    SJF("SJF (Shortest Job First - nao preemptivo)"),
    PRIORIDADE("Prioridade (nao preemptivo, menor numero = maior prioridade)"),
    ROUND_ROBIN("Round Robin");

    private final String descricao;

    Algoritmo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
