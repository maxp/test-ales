(ns gt.weighted)

;; (def G {:1 [:2 :3]
;;         :2 [:4]
;;         :3 [:4]
;;         :4 []})


; GRAPH structure
; map of:
;   Vertice -> List of (Vertice, Weight)
; 
(def WG '{:1 [(:2 1) (:3 2)]
          :2 [(:4 4)]
          :3 [(:4 2)]
          :4 []})


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

(def seq-graph-dfs 
  (partial seq-graph []))

(def seq-graph-bfs 
  (partial seq-graph (clojure.lang.PersistentQueue/EMPTY)))


(comment

  (seq-graph-dfs WG :1) ; => (:1 :3 :4 :2)
  (seq-graph-bfs WG :1) ; => (:1 :2 :3 :4)

  ,)

; - - - - - - - - - - - - - - - - - - -

(def ^:const WEIGHT_RANGE 10)

(defn triangle-pairs [n]
  (mapcat
    (fn [i]
      (map #(list i %) (range (inc i) n)))
    (range n)))
;

(defn random-graph 
  "generate wighted directed graph in form
    {vertice [
                [destination-vertice weight]
                ...
              ]
              ...
              }
  "
  [num-vertices num-edges]
  (let [graph (into {} (map #(vector % []) (range num-vertices)))
        ;; NOTE: inefficient for large number of vertices !
        vpairs (->> (triangle-pairs num-vertices) (shuffle) (take num-edges))]
    ;
    (->> vpairs
      (reduce
        (fn [g [v-from v-to]]
          (let [weight (rand-int WEIGHT_RANGE)]
            (update g v-from
              conj [v-to weight])))
        graph))))

(comment

  (triangle-pairs 5)
  ;; => ([0 1] [0 2] [0 3] [0 4] [1 2] [1 3] [1 4] [2 3] [2 4] [3 4])

  (random-graph 6 10)
  ;; => {0 [[4 7] [1 8] [5 8] [2 1] [3 3]], 1 [[4 4] [3 9] [2 9]], 2 [[4 1]], 3 [[5 0]], 4 [], 5 []}


  (list 1 2)
  ,)
