(ns graph-traversal.core-test
  (:require [clojure.test :refer :all]
            [graph-traversal.core :refer :all]))

(def G {:1 [[:2 1] [:3 2]]
        :2 [[:4 4]]
        :3 [[:4 2]]
        :4 []})

(deftest seq-graph-dfs-test
  (testing "Depth-First Search traversal"
    (is (= '(:1 :3 :4 :2) (seq-graph-dfs G :1)))
    (is (= '(:2 :4) (seq-graph-dfs G :2)))
    (is (= '(:3 :4) (seq-graph-dfs G :3)))
    (is (= '(:4) (seq-graph-dfs G :4)))))

(deftest seq-graph-bfs-test
  (testing "Breadth-First Search traversal"
    (is (= '(:1 :2 :3 :4) (seq-graph-bfs G :1)))
    (is (= '(:2 :4) (seq-graph-bfs G :2)))
    (is (= '(:3 :4) (seq-graph-bfs G :3)))
    (is (= '(:4) (seq-graph-bfs G :4)))))

(deftest empty-graph-test
  (testing "Traversal on empty graph"
    (let [empty-graph {}]
      (is (= '(:start) (seq-graph-dfs empty-graph :start)))
      (is (= '(:start) (seq-graph-bfs empty-graph :start))))))

(deftest disconnected-graph-test
  (testing "Traversal on disconnected graph"
    (let [disconnected-graph {:1 [[:2 1]], :3 [[:4 3]], :5 []}]
      (is (= '(:1 :2) (seq-graph-dfs disconnected-graph :1)))
      (is (= '(:1 :2) (seq-graph-bfs disconnected-graph :1)))
      (is (= '(:3 :4) (seq-graph-dfs disconnected-graph :3)))
      (is (= '(:5) (seq-graph-dfs disconnected-graph :5))))))

(deftest cyclic-graph-test
  (testing "Traversal on cyclic graph"
    (let [cyclic-graph {:1 [[:2 1]], :2 [[:3 2]], :3 [[:1 3]]}]
      (is (= '(:1 :2 :3) (seq-graph-dfs cyclic-graph :1)))
      (is (= '(:1 :2 :3) (seq-graph-bfs cyclic-graph :1))))))

(deftest random-graph-generation-test
  (testing "Random graph generation"
    (let [N 5
          S 7
          graph (random-graph-generation N S)]
      (is (= N (count (keys graph))) 
          (str "Graph should have " N " nodes, but has " (count (keys graph))))
      (is (= S (count-edges graph))
          (str "Graph should have " S " edges, but has " (count-edges graph)))
      (let [start-node (first (keys graph))
            visited (set (seq-graph-dfs graph start-node))]
        (is (= (set (keys graph)) visited) 
            (str "All nodes should be reachable. Reachable: " visited ", All nodes: " (set (keys graph))))))))

(deftest random-graph-edge-cases-test
  (testing "Edge cases for random graph generation"
    (is (thrown? IllegalArgumentException 
                 (random-graph-generation 1 1)) 
        "Should throw exception for N < 2")
    (is (thrown? IllegalArgumentException 
                 (random-graph-generation 5 3)) 
        "Should throw exception for S < N-1")
    (is (thrown? IllegalArgumentException 
                 (random-graph-generation 5 21)) 
        "Should throw exception for S > N(N-1)")))

(deftest random-graph-structure-test
  (testing "Structure of generated graph"
    (let [graph (random-graph-generation 5 7)]
      (is (every? keyword? (keys graph)) "All nodes should be keywords")
      (is (every? vector? (vals graph)) "All node values should be vectors")
      (is (every? (fn [edges] 
                    (every? (fn [[node weight]]
                              (and (keyword? node) 
                                   (integer? weight)
                                   (<= 1 weight 10))) 
                            edges))
                  (vals graph))
          "All edges should be [keyword, integer] pairs with weight between 1 and 10"))))

(deftest shortest-path-test
  (testing "Shortest path in a random graph"
    (let [graph (make-graph 10 15)
          start (first (keys graph))
          end (last (keys graph))
          path (shortest-path graph start end)]
      (is (not (nil? path)) "A path should exist in a connected graph")
      (is (= start (first path)) "Path should start with the start node")
      (is (= end (last path)) "Path should end with the end node"))))

(deftest graph-distance-properties-test
  (testing "Graph distance properties"
    (let [graph (make-graph 10 15)
          first-vertex (first (keys graph))]
      
      (let [ecc (eccentricity graph first-vertex)]
        (is (number? ecc) "Eccentricity should be a number"))
      
      (let [rad (radius graph)]
        (is (number? rad) "Radius should be a number"))
      
      (let [diam (diameter graph)]
        (is (number? diam) "Diameter should be a number"))
      
      (is (<= (radius graph) (diameter graph)) "Radius should be less than or equal to diameter"))))
