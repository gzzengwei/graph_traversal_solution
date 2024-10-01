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

(defn dijkstra
  "Compute single-source shortest path distances in a weighted graph."
  [graph source]
  (loop [distances (assoc (zipmap (keys graph) (repeat Double/POSITIVE_INFINITY)) source 0)
         predecessors {}
         pq (priority-map source 0)]
    (if-let [[v d] (peek pq)]
      (let [neighbors (get graph v)
            relaxed (reduce
                     (fn [[distances predecessors pq] [neighbor weight]]
                       (let [new-distance (+ d weight)]
                         (if (< new-distance (get distances neighbor Double/POSITIVE_INFINITY))
                           [(assoc distances neighbor new-distance)
                            (assoc predecessors neighbor v)
                            (assoc pq neighbor new-distance)]
                           [distances predecessors pq])))
                     [distances predecessors (pop pq)]
                     neighbors)]
        (recur (first relaxed) (second relaxed) (nth relaxed 2)))
      [distances predecessors])))

(defn shortest-path
  "Find the shortest path between start and end nodes in the graph."
  [graph start end]
  (let [[distances predecessors] (dijkstra graph start)]
    (if (= (get distances end) Double/POSITIVE_INFINITY)
      nil  ; No path exists
      (loop [path (list end)
             current end]
        (if (= current start)
          path
          (let [predecessor (get predecessors current)]
            (recur (conj path predecessor) predecessor)))))))

(defn make-graph
  "Alias for random-graph-generation function"
  [N S]
  (random-graph-generation N S))

(defn eccentricity
  "Calculate the eccentricity of a vertex in the graph."
  [graph vertex]
  (let [[distances _] (dijkstra graph vertex)]
    (apply max (remove #(= % Double/POSITIVE_INFINITY) (vals distances)))))

(defn radius
  "Calculate the radius of the graph."
  [graph]
  (apply min (map #(eccentricity graph %) (keys graph))))

(defn diameter
  "Calculate the diameter of the graph."
  [graph]
  (apply max (map #(eccentricity graph %) (keys graph))))
