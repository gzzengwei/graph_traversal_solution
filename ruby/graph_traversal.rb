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

  def self.seq_graph_dfs(g, s)
    seq_graph([], g, s)
  end

  def self.seq_graph_bfs(g, s)
    seq_graph(Queue.new, g, s)
  end
end
