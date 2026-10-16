import java.util.ArrayList;
import java.util.List;

public class Cenarios {

    public static final String[] NOMES = {
            "Processos curtos e semelhantes",
            "Processos muito diferentes entre si (um longo e varios curtos)",
            "Chegadas em tempos diferentes e com bloqueios de E/S",
            "Processos com prioridades diferentes"
    };

    public static List<Processo> carregar(int numero) {
        List<Processo> lista = new ArrayList<>();
        switch (numero) {
            case 1:
                lista.add(new Processo(1, 0, 3, 2, 0, 0));
                lista.add(new Processo(2, 0, 4, 1, 0, 0));
                lista.add(new Processo(3, 1, 3, 3, 0, 0));
                lista.add(new Processo(4, 2, 4, 2, 0, 0));
                break;
            case 2:
                lista.add(new Processo(1, 0, 20, 3, 0, 0));
                lista.add(new Processo(2, 1, 2, 1, 0, 0));
                lista.add(new Processo(3, 2, 8, 2, 0, 0));
                lista.add(new Processo(4, 3, 1, 4, 0, 0));
                break;
            case 3:
                lista.add(new Processo(1, 0, 6, 2, 2, 3));   // faz E/S apos 2u, fica 3u bloqueado
                lista.add(new Processo(2, 1, 4, 1, 0, 0));
                lista.add(new Processo(3, 3, 5, 3, 1, 4));   // faz E/S apos 1u, fica 4u bloqueado
                lista.add(new Processo(4, 6, 2, 2, 0, 0));
                lista.add(new Processo(5, 20, 3, 1, 0, 0));  // chega com a CPU ociosa
                break;
            case 4:
                lista.add(new Processo(1, 0, 8, 3, 0, 0));
                lista.add(new Processo(2, 1, 4, 1, 0, 0));
                lista.add(new Processo(3, 2, 6, 4, 0, 0));
                lista.add(new Processo(4, 3, 3, 2, 0, 0));
                break;
            default:
                break;
        }
        return lista;
    }
}
