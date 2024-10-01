require "set"
require "pqueue"

module GraphTraversal
  def self.seq_graph(d, g, s)
    Enumerator.new do |yielder|
      explored = Set.new([s])
      frontier = d.is_a?(Array) ? d.dup : d
      frontier << s

      until frontier.empty?
        v = d.is_a?(Array) ? frontier.pop : frontier.shift
        neighbors = g[v]&.map(&:first) || []
        yielder << v
        (neighbors - explored.to_a).each do |neighbor|
          explored.add(neighbor)
          frontier << neighbor
        end
      end
    end
  end

  def self.random_graph_generation(n, s)
    min_edges = n - 1
    max_edges = n * (n - 1)

    raise ArgumentError, "Invalid input: N must be >= 2, and S must be between #{min_edges} and #{max_edges} (inclusive)" if n < 2 || s < min_edges || s > max_edges

    graph = (1..n).map { |i| [i.to_s, []] }.to_h
    edges_count = 0
    unvisited = Set.new(graph.keys[1..])

    until edges_count == s
      source = if unvisited.empty?
        graph.keys.sample
      elsif edges_count.zero?
        graph.keys.first
      else
        (graph.keys.to_set - unvisited).to_a.sample
      end

      target = unvisited.empty? ? (graph.keys - [source]).sample : unvisited.to_a.sample
      weight = rand(1..10)
      new_edge = [target, weight]

      unless graph[source].any? { |edge| edge.first == target }
        graph[source] << new_edge
        edges_count += 1
        unvisited.delete(target)
      end
    end

    graph
  end

  def self.count_edges(graph)
    graph.values.map(&:size).sum
  end

  def self.seq_graph_dfs(g, s)
    seq_graph([], g, s)
  end

  def self.seq_graph_bfs(g, s)
    seq_graph(Queue.new, g, s)
  end

  def self.shortest_path(graph, start, end_node)
    distances, predecessors = dijkstra(graph, start)
    return nil if distances[end_node] == Float::INFINITY

    path = [end_node]
    current = end_node
    until current == start
      current = predecessors[current]
      path.unshift(current)
    end
    path
  end

  def self.dijkstra(graph, source)
    distances = graph.keys.map { |k| [k, Float::INFINITY] }.to_h
    distances[source] = 0
    predecessors = {}
    pq = PQueue.new([[source, 0]]) { |a, b| a[1] < b[1] }

    until pq.empty?
      v, d = pq.pop
      graph[v]&.each do |neighbor, weight|
        new_distance = d + weight
        if new_distance < distances[neighbor]
          distances[neighbor] = new_distance
          predecessors[neighbor] = v
          pq.push([neighbor, new_distance])
        end
      end
    end

    [distances, predecessors]
  end

  def self.make_graph(n, s)
    random_graph_generation(n, s)
  end
end
