require_relative "../graph_traversal"
require "set"

RSpec.describe GraphTraversal do
  let(:g) do
    {
      "1" => [["2", 1], ["3", 2]],
      "2" => [["4", 4]],
      "3" => [["4", 2]],
      "4" => []
    }
  end

  describe ".seq_graph_dfs" do
    it "performs depth-first search correctly" do
      expect(described_class.seq_graph_dfs(g, "1").to_a).to eq ["1", "3", "4", "2"]
      expect(described_class.seq_graph_dfs(g, "2").to_a).to eq ["2", "4"]
      expect(described_class.seq_graph_dfs(g, "3").to_a).to eq ["3", "4"]
      expect(described_class.seq_graph_dfs(g, "4").to_a).to eq ["4"]
    end
  end

  describe ".seq_graph_bfs" do
    it "performs breadth-first search correctly" do
      expect(described_class.seq_graph_bfs(g, "1").to_a).to eq ["1", "2", "3", "4"]
      expect(described_class.seq_graph_bfs(g, "2").to_a).to eq ["2", "4"]
      expect(described_class.seq_graph_bfs(g, "3").to_a).to eq ["3", "4"]
      expect(described_class.seq_graph_bfs(g, "4").to_a).to eq ["4"]
    end
  end

  describe "empty graph" do
    let(:empty_graph) { {} }

    it "handles empty graphs correctly" do
      expect(described_class.seq_graph_dfs(empty_graph, "start").to_a).to eq ["start"]
      expect(described_class.seq_graph_bfs(empty_graph, "start").to_a).to eq ["start"]
    end
  end

  describe "disconnected graph" do
    let(:disconnected_graph) { {"1" => [["2", 1]], "3" => [["4", 3]], "5" => []} }

    it "handles disconnected graphs correctly" do
      expect(described_class.seq_graph_dfs(disconnected_graph, "1").to_a).to eq ["1", "2"]
      expect(described_class.seq_graph_bfs(disconnected_graph, "1").to_a).to eq ["1", "2"]
      expect(described_class.seq_graph_dfs(disconnected_graph, "3").to_a).to eq ["3", "4"]
      expect(described_class.seq_graph_dfs(disconnected_graph, "5").to_a).to eq ["5"]
    end
  end

  describe "cyclic graph" do
    let(:cyclic_graph) { {"1" => [["2", 1]], "2" => [["3", 2]], "3" => [["1", 3]]} }

    it "handles cyclic graphs correctly" do
      expect(described_class.seq_graph_dfs(cyclic_graph, "1").to_a).to eq ["1", "2", "3"]
      expect(described_class.seq_graph_bfs(cyclic_graph, "1").to_a).to eq ["1", "2", "3"]
    end
  end

  describe ".random_graph_generation" do
    let(:n) { 5 }
    let(:s) { 7 }

    it "generates a graph with correct number of nodes and edges" do
      graph = described_class.random_graph_generation(n, s)

      expect(graph.keys.size).to eq n
      expect(described_class.count_edges(graph)).to eq s
    end

    it "generates a connected graph" do
      graph = described_class.random_graph_generation(n, s)
      start_node = graph.keys.first
      visited = Set.new(described_class.seq_graph_dfs(graph, start_node))
      expect(visited).to eq Set.new(graph.keys)
    end

    it "raises an error for invalid inputs" do
      expect { described_class.random_graph_generation(1, 1) }.to raise_error(ArgumentError)
      expect { described_class.random_graph_generation(5, 3) }.to raise_error(ArgumentError)
      expect { described_class.random_graph_generation(5, 21) }.to raise_error(ArgumentError)
    end

    it "generates a graph with correct structure" do
      graph = described_class.random_graph_generation(5, 7)
      expect(graph.keys).to all(be_a(String))
      expect(graph.values).to all(be_an(Array))
      expect(graph.values.flatten(1)).to all(satisfy { |edge| edge.first.is_a?(String) && edge.last.is_a?(Integer) && edge.last.between?(1, 10) })
    end
  end

  describe ".shortest_path" do
    it "finds the shortest path between two nodes" do
      graph = described_class.make_graph(10, 15)
      start = graph.keys.first
      end_node = graph.keys.last
      path = described_class.shortest_path(graph, start, end_node)

      expect(path).not_to be_nil
      expect(path.first).to eq start
      expect(path.last).to eq end_node
    end
  end

  describe "graph distance properties" do
    let(:graph) { described_class.make_graph(10, 15) }
    let(:first_vertex) { graph.keys.first }

    it "calculates eccentricity correctly" do
      ecc = described_class.eccentricity(graph, first_vertex)
      puts "Eccentricity of #{first_vertex}: #{ecc}"
      expect(ecc).to be_a(Numeric)
    end

    it "calculates radius correctly" do
      rad = described_class.radius(graph)
      puts "Radius of the graph: #{rad}"
      expect(rad).to be_a(Numeric)
    end

    it "calculates diameter correctly" do
      diam = described_class.diameter(graph)
      puts "Diameter of the graph: #{diam}"
      expect(diam).to be_a(Numeric)
    end

    it "ensures radius is less than or equal to diameter" do
      rad = described_class.radius(graph)
      diam = described_class.diameter(graph)
      expect(rad).to be <= diam
    end
  end
end
