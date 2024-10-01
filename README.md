# Graph Traversal Project

## Summary

This project implements various graph traversal algorithms and utilities in both Ruby and Clojure. It provides functionality for depth-first search (DFS), breadth-first search (BFS), shortest path finding, and calculation of graph properties such as eccentricity, radius, and diameter. The project also includes a random graph generator for testing purposes.

## Main Functions

- `seq_graph_dfs`: Performs a depth-first search traversal of the graph.
- `seq_graph_bfs`: Performs a breadth-first search traversal of the graph.
- `random_graph_generation`: Generates a random connected graph with specified number of nodes and edges.
- `shortest_path`: Finds the shortest path between two nodes in the graph using Dijkstra's algorithm.
- `eccentricity`: Calculates the eccentricity of a vertex in the graph.
- `radius`: Calculates the radius of the graph.
- `diameter`: Calculates the diameter of the graph.

## Ruby Solution

### Project Setup

1. Ensure you have Ruby installed on your system.
2. Clone this repository:
3. Install the required gems:
   ```
   bundle install
   ```

### Usage

To use the graph traversal functions in your Ruby code:

```ruby
require_relative 'lib/graph_traversal'

# Example usage
graph = {
  '1' => {'2' => 1, '3' => 2},
  '2' => {'4' => 4},
  '3' => {'4' => 2},
  '4' => {}
}

puts GraphTraversal.seq_graph_dfs(graph, '1').to_a
puts GraphTraversal.shortest_path(graph, '1', '4')
```

### Lint

To lint your Ruby code using StandardRB:

```
standardrb
```

To automatically fix linting issues:

```
standardrb --fix
```

### Test

To run the RSpec tests:

```
bundle exec rspec
```

## Clojure Solution

The Clojure solution is provided in the `src/graph_traversal/core.clj` file. It implements the same functionality as the Ruby solution.

### Usage

To use the Clojure implementation interactively, you can use `lein repl`. Here's how to get started:

1. Ensure you have Leiningen installed on your system.
2. Navigate to the Clojure project directory.
3. Start the REPL by running:
   ```
   lein repl
   ```
4. Once in the REPL, you can require the namespace and use the functions:
   ```clojure
   (require '[graph-traversal.core :as gt])

   ;; Define a sample graph
   (def G {:1 {:2 1, :3 2}
           :2 {:4 4}
           :3 {:4 2}
           :4 {}})

   ;; Use the functions
   (gt/seq-graph-dfs G :1)
   (gt/shortest-path G :1 :4)
   ```

### Test

To run the Clojure tests:

1. Ensure you have Leiningen installed.
2. Navigate to the Clojure project directory.
3. Run the tests using:
   ```
   lein test
   ```

For more details on the Clojure implementation, refer to the `core.clj` and `core_test.clj` files in the Clojure project directory.

