#!clojure

(ns graph-traversal.core
  (:require [clojure.set :as set]
            [clojure.data.priority-map :refer [priority-map]]))

(defn seq-graph [d g s]
  ((fn rec-seq [explored frontier]
     (lazy-seq
       (if (empty? frontier)
         nil
         (let [v (peek frontier)
               neighbors (map first (g v))]
           (cons v (rec-seq
                     (into explored neighbors)
                     (into (pop frontier) (remove explored neighbors))))))))
   #{s} (conj d s)))

(def seq-graph-dfs (partial seq-graph []))
(def seq-graph-bfs (partial seq-graph clojure.lang.PersistentQueue/EMPTY))

(defn random-graph-generation
  "Generate a connected random simple directed graph with N vertices and S edges."
  [N S]
  (let [min-edges (dec N)
        max-edges (* N (dec N))]
    (when (or (< N 2) (< S min-edges) (> S max-edges))
      (throw (IllegalArgumentException. 
               (str "Invalid input: N must be >= 2, and S must be between " 
                    min-edges " and " max-edges " (inclusive)"))))
    
    (loop [graph (into {} (map (fn [n] [(keyword (str n)) []]) (range 1 (inc N))))
           edges-count 0
           unvisited (set (rest (keys graph)))]
      (if (= edges-count S)
        graph
        (let [source (if (empty? unvisited)
                       (rand-nth (keys graph))
                       (if (zero? edges-count)
                         (first (keys graph))
                         (rand-nth (vec (set/difference (set (keys graph)) unvisited)))))
              target (if (empty? unvisited)
                       (rand-nth (remove #{source} (keys graph)))
                       (rand-nth (vec unvisited)))
              weight (inc (rand-int 10))
              new-edge [target weight]
              updated-graph (update graph source conj new-edge)]
          (if (some #(= (first %) target) (graph source))
            (recur graph edges-count unvisited)
            (recur updated-graph 
                   (inc edges-count) 
                   (disj unvisited target))))))))

(defn count-edges
  "Count the total number of edges in the graph"
  [graph]
  (reduce + (map count (vals graph))))
