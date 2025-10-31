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

        System.out.println(" ====== Rede de aeroportos brasileiros ====== \n");
        grafo.exibirGrafo();

        Scanner sc = new Scanner(System.in);
        boolean agendamentoRealizado = false;
        
        while (!agendamentoRealizado) {

            Vertice origem = null;
            while (origem == null) {
                System.out.println("--> Digite o nome do aeroporto de origem (ex: São Paulo (GRU)):");
                String origemNome = sc.nextLine();
                origem = grafo.buscarVerticePorNome(origemNome);
                if (origem == null) {
                    System.out.println("X Aeroporto de origem não encontrado. Tente novamente.\n");
                }
            }

            Vertice destino = null;
            while (destino == null) {
                System.out.println("--> Digite o nome do aeroporto de destino (ex: Manaus (MAO)):");
                String destinoNome = sc.nextLine();
                destino = grafo.buscarVerticePorNome(destinoNome);
                if (destino == null) {
                    System.out.println("X Aeroporto de destino não encontrado. Tente novamente.\n");
                }
            }

            List<Vertice> caminho = Dijkstra.menorCaminho(grafo, origem, destino);

            if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
                System.out.println("\nX Não existe rota aérea de " + origem + " até " + destino);
                System.out.println("--> Mas você pode fazer uma conexão!");
                sugerirConexoes(grafo, origem, destino);
            } else {
                int distanciaTotal = Dijkstra.calcularDistanciaTotal(grafo, caminho);

                System.out.println("\n--> Menor caminho entre " + origem + " e " + destino + ":");
                
                StringBuilder caminhoStr = new StringBuilder();
                for (int i = 0; i < caminho.size(); i++) {
                    caminhoStr.append(caminho.get(i));
                    if (i < caminho.size() - 1) {
                        caminhoStr.append(" -> ");
                    }
                }
                System.out.println(caminhoStr.toString());
                System.out.println("--> Distância total: " + distanciaTotal + " km");

                if (caminho.size() == 2) {
                    System.out.println("--> Ligação direta disponível.");
                    System.out.println("\n------> VIAGEM AGENDADA COM SUCESSO! ------>");
                    System.out.println("Sua viagem de " + origem + " para " + destino + " foi confirmada!");
                    System.out.println("Rota: " + caminhoStr.toString());
                    System.out.println("Distância: " + distanciaTotal + " km");
                    System.out.println("Tempo estimado de viagem: " + calcularTempoEstimado(distanciaTotal));
    
                    agendamentoRealizado = true;
                    
                } else {
                    System.out.println("X Não há ligação direta.");
                    System.out.println("----> É necessário passar pelos seguintes aeroportos intermediários:");
                    for (int i = 1; i < caminho.size() - 1; i++) {
                        System.out.println("   - " + caminho.get(i));
                    }

                    System.out.println("\n--> Deseja agendar esta viagem com conexões?");
                    System.out.println("1 - Sim, agendar esta rota.");
                    System.out.println("2 - Não, quero buscar outra rota.");
                    System.out.println("3 - Sair do sistema.");
                    System.out.print("Escolha uma opção (1-3): ");
                    
                    String opcao = sc.nextLine().trim();
                    
                    switch (opcao) {
                        case "1":
                            System.out.println("\n------> VIAGEM AGENDADA COM SUCESSO! ------>");
                            System.out.println("Sua viagem de " + origem + " para " + destino + " foi confirmada!");
                            System.out.println("Rota: " + caminhoStr.toString());
                            System.out.println("Distância: " + distanciaTotal + " km");
                            System.out.println("Tempo estimado de viagem: " + calcularTempoEstimado(distanciaTotal));
                            System.out.println("Número de conexões: " + (caminho.size() - 2));
                            agendamentoRealizado = true;
                            break;
                            
                        case "2":
                            System.out.println("\n--> Buscando nova rota...");
                            break;
                            
                        case "3":
                            System.out.println("\n ------> Obrigado por viajar conosco! ------>");
                            System.out.println("--> Saindo...");
                            sc.close();
                            return;
                            
                        default:
                            System.out.println("\nX Opção inválida! Voltando ao menu principal...");
                            break;
                    }
                }
            }
        }
        
        System.out.println("\n------> Obrigado por viajar conosco! ------>");
        sc.close();
    }

    public static String calcularTempoEstimado(int distancia) {
        double horas = distancia / 800.0;
        int horasInt = (int) horas;
        int minutos = (int) ((horas - horasInt) * 60);
        
        if (minutos > 0) {
            return horasInt + "h" + minutos + "min";
        } else {
            return horasInt + " horas";
        }
    }

    public static void sugerirConexoes(Grafo grafo, Vertice origem, Vertice destino) {
        System.out.println("\n--> Sugestões de rota com conexão:");
        
        boolean encontrouConexao = false;
     
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
                    
                    System.out.println("--> Via " + conexao + ":");
                    System.out.println("   " + origem + " -> " + conexao + " -> " + destino);
                    System.out.println("   Distância total: " + total + " km");
                }
            }
        }
        
        if (!encontrouConexao) {
            System.out.println("X Não foram encontradas rotas possíveis entre estes aeroportos.");
        }
        
        System.out.println("\n--> Voltando ao menu principal...");
    }
}