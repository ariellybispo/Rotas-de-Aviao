import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // --- Aeroportos ---
        Vertice gru = new Vertice("São Paulo (GRU)");
        Vertice gig = new Vertice("Rio de Janeiro (GIG)");
        Vertice bsb = new Vertice("Brasília (BSB)");
        Vertice cnf = new Vertice("Belo Horizonte (CNF)");
        Vertice rec = new Vertice("Recife (REC)");
        Vertice forz = new Vertice("Fortaleza (FOR)");
        Vertice ssa = new Vertice("Salvador (SSA)");
        Vertice poa = new Vertice("Porto Alegre (POA)");
        Vertice cwb = new Vertice("Curitiba (CWB)");
        Vertice mao = new Vertice("Manaus (MAO)");
        Vertice cui = new Vertice("Cuiabá (CGB)");

        Vertice[] vertices = {gru, gig, bsb, cnf, rec, forz, ssa, poa, cwb, mao, cui};
        for (Vertice v : vertices) grafo.adicionarVertice(v);

        // --- Adicionando as rotas (arestas com pesos em km) ---
        grafo.adicionarAresta(gru, gig, 360);
        grafo.adicionarAresta(gru, bsb, 870);
        grafo.adicionarAresta(gru, cnf, 490);
        grafo.adicionarAresta(gru, rec, 2120);
        grafo.adicionarAresta(gig, ssa, 1210);
        grafo.adicionarAresta(ssa, rec, 675);
        grafo.adicionarAresta(rec, forz, 630);
        grafo.adicionarAresta(bsb, cui, 875);
        grafo.adicionarAresta(cui, mao, 1450);
        grafo.adicionarAresta(cwb, poa, 710);
        grafo.adicionarAresta(cwb, gru, 340);

        System.out.println("=== Rede de aeroportos ===\n");
        grafo.exibirGrafo();

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome do aeroporto de origem (ex: São Paulo (GRU)):");
        String origemNome = sc.nextLine();

        System.out.println("Digite o nome do aeroporto de destino (ex: Manaus (MAO)):");
        String destinoNome = sc.nextLine();

        Vertice origem = grafo.buscarVerticePorNome(origemNome);
        Vertice destino = grafo.buscarVerticePorNome(destinoNome);

        if (origem == null || destino == null) {
            System.out.println("❌ Um dos aeroportos digitados não existe.");
            return;
        }

        List<Vertice> caminho = Dijkstra.menorCaminho(grafo, origem, destino);
        int distanciaTotal = Dijkstra.calcularDistanciaTotal(grafo, caminho);

        System.out.println("\n=== Menor caminho de " + origem + " até " + destino + " ===");
        for (Vertice v : caminho) {
            System.out.print(v + " -> ");
        }
        System.out.println("Fim");
        System.out.println("Distância total: " + distanciaTotal + " km");

        sc.close();
    }
}
