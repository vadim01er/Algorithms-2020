package lesson6;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    // Асимптотика: O(V * E)
    // Ресурсоемкость: O(V)
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();
        List<Graph.Vertex> answer = new ArrayList<>();
        for (Graph.Vertex vertex: vertices) {
            ArrayDeque<Graph.Vertex> stack = new ArrayDeque<>();
            List<Graph.Vertex> currentAnswer = new ArrayList<>();
            Set<Graph.Vertex> visited = new HashSet<>();
            stack.add(vertex);
            visited.add(vertex);
            while (!stack.isEmpty() || visited.size() != vertices.size()) {
                if (stack.isEmpty()) {
                    vertices.stream().filter(vertex1 -> !visited.contains(vertex1)).forEach(stack::add);
                }
                Graph.Vertex current = stack.pop();
                visited.add(current);
                for (Graph.Vertex vertexNow : graph.getNeighbors(current)) {
                    visited.add(vertexNow);
                    stack.remove(vertexNow);
                    graph.getNeighbors(vertexNow).stream()
                            .filter(vertex1 -> !visited.contains(vertex1))
                            .forEach(stack::add);
                }
                currentAnswer.add(current);
            }
            if (answer.size() < currentAnswer.size()) {
                answer = currentAnswer;
            }
        }
        return new HashSet<>(answer);
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    // Асимптотика: O(V + E)
    // Ресурсоемкость: O(V)
    public static Path longestSimplePath(Graph graph) {
        Path longestPath = new Path();
        Deque<Path> paths = new ArrayDeque<>();
        graph.getVertices().forEach(vertex -> paths.add(new Path(vertex)));
        while (!paths.isEmpty()) {
            Path current = paths.pollLast();
            int currentLength = current.getLength();
            longestPath = currentLength < longestPath.getLength() ? longestPath : current;
            graph.getNeighbors(current.getVertices().get(currentLength)).stream()
                    .filter(vertex -> !current.contains(vertex))
                    .forEach(vertex -> paths.add(new Path(current, graph, vertex)));
        }
        return longestPath;
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
