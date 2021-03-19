(ns g)

(def G {:1 [:2 :3]
        :2 [:4]
        :3 [:4]
        :4 []})

(defn traverse-graph-dfs [g s]
  (loop [vertices [] explored #{s} frontier [s]]
    (if (empty? frontier)
      vertices
      (let [v (peek frontier)
            neighbors (g v)]
        (recur
         (conj vertices v)
         (into explored neighbors)
         (into (pop frontier) (remove explored neighbors)))))))

(defn seq-graph-dfs [g s]
  ((fn rec-dfs [explored frontier]
     (lazy-seq
      (if (empty? frontier)
        nil
        (let [v (peek frontier)
              neighbors (g v)]
          (cons v (rec-dfs
                   (into explored neighbors)
                   (into (pop frontier) (remove explored neighbors))))))))
   #{s} [s]))

(defn seq-graph-bfs [g s]
  ((fn rec-bfs [explored frontier]
     (lazy-seq
      (if (empty? frontier)
        nil
        (let [v (peek frontier)
              neighbors (g v)]
          (cons v (rec-bfs
                   (into explored neighbors)
                   (into (pop frontier) (remove explored neighbors))))))))
   #{s} (conj (clojure.lang.PersistentQueue/EMPTY) s)))


(comment
  (traverse-graph-dfs G :1) ; => [:1 :3 :4 :2]
  (seq-graph-dfs G :1) ; => (:1 :3 :4 :2)
  (seq-graph-bfs G :1) ; => (:1 :2 :3 :4)
  ,)
;

(defn seq-graph [d g s]
  ((fn rec-seq [explored frontier]
     (lazy-seq
      (if (empty? frontier)
        nil
        (let [v (peek frontier)
              neighbors (g v)]
          (cons v (rec-seq
                   (into explored neighbors)
                   (into (pop frontier) (remove explored neighbors))))))))
   #{s} (conj d s)))

(comment
  (def seq-graph-dfs (partial seq-graph []))
  (def seq-graph-bfs (partial seq-graph (clojure.lang.PersistentQueue/EMPTY)))

  (seq-graph-dfs G :1) ; => (:1 :3 :4 :2)
  (seq-graph-bfs G :1) ; => (:1 :2 :3 :4)
  ,)
;
