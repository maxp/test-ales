(ns gt.weighted)


(def G {:1 [:2 :3]
        :2 [:4]
        :3 [:4]
        :4 []})


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
