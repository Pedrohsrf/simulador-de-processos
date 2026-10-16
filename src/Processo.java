
public class Processo {

    private final int id;
    private final int chegada;
    private final int tempoTotal;
    private final int prioridade;
    private final int momentoES;
    private final int duracaoES;

    private int restante;
    private Estado estado;
    private int executado = 0;
    private boolean fezES = false;
    private int fimBloqueio = -1;
    private int inicio = -1;
    private int conclusao = -1;

    public Processo(int id, int chegada, int tempoTotal, int prioridade, int momentoES, int duracaoES) {
        this.id = id;
        this.chegada = chegada;
        this.tempoTotal = tempoTotal;
        this.prioridade = prioridade;
        this.momentoES = momentoES;
        this.duracaoES = duracaoES;
        this.restante = tempoTotal;
        this.estado = Estado.NOVO;
    }

    public Processo copia() {
        return new Processo(id, chegada, tempoTotal, prioridade, momentoES, duracaoES);
    }

    public void executarUmaUnidade() {
        restante--;
        executado++;
    }

    public boolean devePedirES() {
        return momentoES > 0 && !fezES && executado == momentoES && restante > 0;
    }

    public void bloquear(int tempoAtual) {
        estado = Estado.BLOQUEADO;
        fezES = true;
        fimBloqueio = tempoAtual + duracaoES;
    }

    public boolean terminou() {
        return restante == 0;
    }

    public int getTurnaround() {
        return conclusao - chegada;
    }

    public int getTempoResposta() {
        return inicio - chegada;
    }

    public int getTempoEspera() {
        return getTurnaround() - tempoTotal - (fezES ? duracaoES : 0);
    }

    public int getId() { return id; }
    public int getChegada() { return chegada; }
    public int getTempoTotal() { return tempoTotal; }
    public int getPrioridade() { return prioridade; }
    public int getMomentoES() { return momentoES; }
    public int getDuracaoES() { return duracaoES; }
    public int getRestante() { return restante; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public int getFimBloqueio() { return fimBloqueio; }
    public int getInicio() { return inicio; }
    public void setInicio(int inicio) { this.inicio = inicio; }
    public int getConclusao() { return conclusao; }
    public void setConclusao(int conclusao) { this.conclusao = conclusao; }

    public String getNome() {
        return "P" + id;
    }

    public String descricaoES() {
        return momentoES > 0 ? "apos " + momentoES + "u, dura " + duracaoES + "u" : "-";
    }
}
