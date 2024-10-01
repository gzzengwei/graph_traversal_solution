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
