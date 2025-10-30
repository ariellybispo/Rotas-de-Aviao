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
        Vertice cgb = new Vertice("Cuiabá (CGB)");

        Vertice[] vertices = {gru, gig, bsb, cnf, rec, forz, ssa, poa, cwb, mao, cgb};
        for (Vertice v : vertices) grafo.adicionarVertice(v);

        // --- Adicionando as rotas (arestas com pesos em km) ---
        grafo.adicionarAresta(gru, gig, 360);
        grafo.adicionarAresta(gru, bsb, 870);
        grafo.adicionarAresta(gru, cnf, 490);
        grafo.adicionarAresta(gru, rec, 2120);
        grafo.adicionarAresta(gig, ssa, 1210);
        grafo.adicionarAresta(ssa, rec, 675);
        grafo.adicionarAresta(rec, forz, 630);
        grafo.adicionarAresta(bsb, cgb, 875);
        grafo.adicionarAresta(cgb, mao, 1450);
        grafo.adicionarAresta(cwb, poa, 710);
        grafo.adicionarAresta(cwb, gru, 340);

        System.out.println("🌍 === Rede de aeroportos brasileiros === 🌍\n");
        grafo.exibirGrafo();

        Scanner sc = new Scanner(System.in);
        
        // 🛫 Loop para origem até acertar
        Vertice origem = null;
        while (origem == null) {
            System.out.println("🛫 Digite o nome do aeroporto de origem (ex: São Paulo (GRU)):");
            String origemNome = sc.nextLine();
            origem = grafo.buscarVerticePorNome(origemNome);
            if (origem == null) {
                System.out.println("❌ > Aeroporto de origem não encontrado. Tente novamente.\n");
            }
        }

        // 🛬 Loop para destino até acertar
        Vertice destino = null;
        while (destino == null) {
            System.out.println("🛬 Digite o nome do aeroporto de destino (ex: Manaus (MAO)):");
            String destinoNome = sc.nextLine();
            destino = grafo.buscarVerticePorNome(destinoNome);
            if (destino == null) {
                System.out.println("❌ > Aeroporto de destino não encontrado. Tente novamente.\n");
            }
        }

        List<Vertice> caminho = Dijkstra.menorCaminho(grafo, origem, destino);

        // Verifica se o caminho existe
        if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
            System.out.println("\n🚫 > Não existe rota aérea de " + origem + " até " + destino);
            System.out.println("💡 Mas você pode fazer uma conexão!");
            
            // Sugere conexões possíveis
            sugerirConexoes(grafo, origem, destino);
        } else {
            int distanciaTotal = Dijkstra.calcularDistanciaTotal(grafo, caminho);

            System.out.println("\n🎯 === ROTA ENCONTRADA ===");
            System.out.println("📍 De: " + origem + " até " + destino);
            
            // Verifica se é voo direto ou com conexões
            if (caminho.size() == 2) {
                System.out.println("✅ **VOO DIRETO DISPONÍVEL**");
                System.out.println("🎫 Você pode fazer esta rota sem escalas!");
            } else {
                System.out.println("🔄 **VOO COM CONEXÕES**");
                System.out.println("📋 Esta rota requer " + (caminho.size() - 2) + " conexão(ões)");
                
                // Explica cada trecho da viagem
                explicarConexoes(grafo, caminho);
            }
            
            System.out.println("\n✈️ **Itinerário completo:**");
            for (int i = 0; i < caminho.size(); i++) {
                System.out.print("➡️ " + caminho.get(i));
                if (i < caminho.size() - 1) {
                    System.out.print(" \n    ↓\n");
                }
            }
            System.out.println("\n\n📏 **Distância total:** " + distanciaTotal + " km");
            System.out.println("🏁 **Total de aeroportos:** " + caminho.size());
            
            // Dica educativa
            System.out.println("\n💡 **Por que voos com conexões?**");
            System.out.println("   ✈️  Nem todas as cidades têm voos diretos entre si");
            System.out.println("   💰  Voo direto depende da demanda de passageiros");
            System.out.println("   🏢  Acordos entre companhias aéreas");
            System.out.println("   🛠️  Infraestrutura aeroportuária disponível");
            System.out.println("✅ Viagem planejada com sucesso!");
        }

        sc.close();
    }

    // Método para explicar cada conexão da rota
    public static void explicarConexoes(Grafo grafo, List<Vertice> caminho) {
        System.out.println("\n🔍 **Detalhes das conexões:**");
        
        for (int i = 0; i < caminho.size() - 1; i++) {
            Vertice atual = caminho.get(i);
            Vertice proximo = caminho.get(i + 1);
            
            // Encontra a distância deste trecho
            int distanciaTrecho = 0;
            for (Aresta a : grafo.getAdjacencias(atual)) {
                if (a.getDestino().equals(proximo)) {
                    distanciaTrecho = a.getPeso();
                    break;
                }
            }
            
            System.out.println("   🛫 Trecho " + (i + 1) + ": " + atual + " → " + proximo);
            System.out.println("      📏 " + distanciaTrecho + " km");
            
            // Dica sobre por que não é direto
            if (i == 0 && caminho.size() > 2) {
                System.out.println("      💡 Não há voo direto entre " + caminho.get(0) + " e " + caminho.get(caminho.size() - 1));
            }
        }
    }

    // Método para sugerir conexões quando não há rota direta
    public static void sugerirConexoes(Grafo grafo, Vertice origem, Vertice destino) {
        System.out.println("\n💡 **Sugestões de rota com conexão:**");
        
        boolean encontrouConexao = false;
        
        // Busca aeroportos que conectam origem e destino
        for (Vertice conexao : grafo.getVertices()) {
            if (!conexao.equals(origem) && !conexao.equals(destino)) {
                List<Vertice> caminho1 = Dijkstra.menorCaminho(grafo, origem, conexao);
                List<Vertice> caminho2 = Dijkstra.menorCaminho(grafo, conexao, destino);
                
                if (!caminho1.isEmpty() && caminho1.get(0).equals(origem) && 
                    !caminho2.isEmpty() && caminho2.get(0).equals(conexao)) {
                    
                    encontrouConexao = true;
                    int dist1 = Dijkstra.calcularDistanciaTotal(grafo, caminho1);
                    int dist2 = Dijkstra.calcularDistanciaTotal(grafo, caminho2);
                    int total = dist1 + dist2;
                    
                    System.out.println("🔄 **Via " + conexao + ":**");
                    System.out.println("   📍 " + origem + " → " + conexao + ": " + dist1 + " km");
                    System.out.println("   📍 " + conexao + " → " + destino + ": " + dist2 + " km");
                    System.out.println("   📏 Total: " + total + " km");
                    System.out.println();
                }
            }
        }
        
        if (!encontrouConexao) {
            System.out.println("😔 Não foram encontradas rotas possíveis entre estes aeroportos.");
            System.out.println("📞 Consulte as companhias aéreas para opções especiais.");
        }
        
        // Explica por que não há voo direto
        System.out.println("🔍 **Por que não há voo direto?**");
        System.out.println("📍 " + origem + " não tem conexão direta com " + destino);
        System.out.println("💼 **Fatores que influenciam voos diretos:**");
        System.out.println("   ✈️  Demanda de passageiros insuficiente");
        System.out.println("   🤝  Acordos comerciais entre companhias aéreas");
        System.out.println("   🏗️  Infraestrutura aeroportuária disponível");
        System.out.println("   ⏰  Horários e slots de pouso/decolagem");
        System.out.println("   💵  Viabilidade econômica da rota");
    }
}