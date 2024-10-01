require_relative "../graph_traversal"
require "set"

RSpec.describe GraphTraversal do
  let(:g) do
    {
      "1" => ["2", "3"],
      "2" => ["4"],
      "3" => ["4"],
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
    let(:disconnected_graph) { {"1" => ["2"], "3" => ["4"], "5" => []} }

    it "handles disconnected graphs correctly" do
      expect(described_class.seq_graph_dfs(disconnected_graph, "1").to_a).to eq ["1", "2"]
      expect(described_class.seq_graph_bfs(disconnected_graph, "1").to_a).to eq ["1", "2"]
      expect(described_class.seq_graph_dfs(disconnected_graph, "3").to_a).to eq ["3", "4"]
      expect(described_class.seq_graph_dfs(disconnected_graph, "5").to_a).to eq ["5"]
    end
  end

  describe "cyclic graph" do
    let(:cyclic_graph) { {"1" => ["2"], "2" => ["3"], "3" => ["1"]} }

    it "handles cyclic graphs correctly" do
      expect(described_class.seq_graph_dfs(cyclic_graph, "1").to_a).to eq ["1", "2", "3"]
      expect(described_class.seq_graph_bfs(cyclic_graph, "1").to_a).to eq ["1", "2", "3"]
    end
  end
end
