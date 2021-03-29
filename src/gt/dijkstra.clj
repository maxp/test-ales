(ns gt.dijkstra
  (:require 
    [clojure.data.priority-map :refer [priority-map]]))
;=

;; graph is a map of:
;   Vertice -> List of (Vertice, Weight)

(def ^:private Inf (Long/MAX_VALUE))

(defn make-path [prev-map v]    ;; !!! not lazy !!!
  (when v
    (cons v (make-path prev-map (get prev-map v)))))
;;

;; https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
;; 
(defn shortest-path [start-vert end-vert vert-dists-fn]
  ;
  (loop [q (priority-map start-vert 0)   ;; priority queue {vert, dist}
         dist {}                         ;; {vert, distance}
         prev {}]                        ;; {vert, prev-vert}
    (prn "dist/prev:" dist prev)    ;; !!! debug print !!!
    (if-let [[current-v d] (peek q)]
      (if (= current-v end-vert)
        (make-path prev end-vert)
        (let [[q' d' p']
              (->>
                  (vert-dists-fn current-v)
                  (map (fn [[k v]] [k (+ v d)]))
                  (reduce
                    (fn [[qq dd pp] [nv new-dist]]
                      (let [prev-d (get dd nv Inf)]
                        (if (< new-dist prev-d)
                          [
                            (assoc qq nv new-dist)
                            (assoc dd nv new-dist)
                            (assoc pp nv current-v)]
                          [qq dd pp])))
                    [(pop q) dist prev]))]
          ;
          (recur q' d' p')))
      nil))) ;[dist prev])))    ;; unreachable
;;


(comment

  (let [G
        {:a '([:b 1] [:c 2])
         :b `([:e 10] [:d 1])
         :c '([:d 1] [:f 1])
         :d '([:f 2])
         :e '([:f 1])}]
    ;
    (shortest-path :a :f #(get G %)))
    ;; => (:f :c :a)

  ,)

