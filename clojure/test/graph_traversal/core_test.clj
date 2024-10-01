(ns graph-traversal.core-test
  (:require [clojure.test :refer :all]
            [graph-traversal.core :refer :all]
            [clojure.pprint :refer [pprint]]))

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
