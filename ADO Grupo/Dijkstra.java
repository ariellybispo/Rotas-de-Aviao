import java.util.*;

public class Dijkstra {

    public static List<Vertice> menorCaminho(Grafo grafo, Vertice origem, Vertice destino) {
        Map<Vertice, Integer> distancias = new HashMap<>();
        Map<Vertice, Vertice> anteriores = new HashMap<>();
        PriorityQueue<Vertice> fila = new PriorityQueue<>(Comparator.comparingInt(distancias::get));
    
        for (Vertice v : grafo.getVertices()) {
            distancias.put(v, Integer.MAX_VALUE);
            anteriores.put(v, null);
        }
    
        distancias.put(origem, 0);
        fila.add(origem);
    
        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();
    
            if (atual.equals(destino)) break;
    
            for (Aresta aresta : grafo.getAdjacencias(atual)) {
                Vertice vizinho = aresta.getDestino();
                int novaDistancia = distancias.get(atual) + aresta.getPeso();
    
                if (novaDistancia < distancias.get(vizinho)) {
                    distancias.put(vizinho, novaDistancia);
                    anteriores.put(vizinho, atual);
                    if (fila.contains(vizinho)) {
                        fila.remove(vizinho);
                    }
                    fila.add(vizinho);
                }
            }
        }
    
        List<Vertice> caminho = new ArrayList<>();
        Vertice atual = destino;
    
        while (atual != null) {
            caminho.add(atual);
            atual = anteriores.get(atual);
        }
    
        Collections.reverse(caminho);
    
     
        if (caminho.isEmpty() || !caminho.get(0).equals(origem)) {
            return new ArrayList<>();
        }
    
        return caminho;
    }

    public static int calcularDistanciaTotal(Grafo grafo, List<Vertice> caminho) {
        int total = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            Vertice atual = caminho.get(i);
            Vertice prox = caminho.get(i + 1);
            for (Aresta a : grafo.getAdjacencias(atual)) {
                if (a.getDestino().equals(prox)) {
                    total += a.getPeso();
                    break;
                }
            }
        }
        return total;
    }
    
    }

