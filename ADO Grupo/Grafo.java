import java.text.Normalizer;
import java.util.*;

public class Grafo {
    private Map<Vertice, List<Aresta>> adjacencias = new HashMap<>();

    public void adicionarVertice(Vertice v) {
        adjacencias.putIfAbsent(v, new ArrayList<>());
    }

    public void adicionarAresta(Vertice origem, Vertice destino, int peso) {
        adjacencias.get(origem).add(new Aresta(origem, destino, peso));
        adjacencias.get(destino).add(new Aresta(destino, origem, peso)); 
    }

    public List<Aresta> getAdjacencias(Vertice v) {
        return adjacencias.get(v);
    }

    public Set<Vertice> getVertices() {
        return adjacencias.keySet();
    }

    public Vertice buscarVerticePorNome(String nomeDigitado) {
    String nomeBusca = Normalizer.normalize(nomeDigitado.trim(), Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "")
            .toLowerCase();

    for (Vertice v : adjacencias.keySet()) {
        String nomeVertice = Normalizer.normalize(v.getNome(), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();

        if (nomeVertice.contains(nomeBusca)) {
            return v;
        }
    }

        return null;
    }

    public void exibirGrafo() {
        for (Vertice v : adjacencias.keySet()) {
            System.out.println("Aeroporto " + v.getNome() + " conecta-se a:");
            for (Aresta a : adjacencias.get(v)) {
                System.out.println(" --> " + a.getDestino().getNome() + " (" + a.getPeso() + " km)");
            }
            System.out.println();
        }
    }
}
