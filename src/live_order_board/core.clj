(ns live-order-board.core)

(defrecord Order [user-id quantity price order-type])

(defn register-order!
  "takes an atom of a sequence of orders and an order, adds
  the order to the sequence"
  [orders order]
  (swap! orders conj order))

(defn cancel-order!
  "takes an atom of a sequence of orders and an order, removes
  the order from the sequence"
  [orders order]
  (swap! orders (fn [coll]
                  (let [[coll-a coll-b] (split-with #(not= order %) coll)]
                    (concat coll-a (rest coll-b))))))

(defn- order-summary->str [order]
  (map #(str (:quantity %) " kg for £" (:price %)) order))

(defn- sum-quantities [[price orders]]
  (let [total (reduce + (map :quantity orders))]
    {:quantity total :price price}))

(defn- order-summary [orders order-type sort-orders]
  (->> (filter #(= order-type (:order-type %)) orders)
       (group-by :price)
       (map sum-quantities)
       sort-orders
       order-summary->str))

(defn order-summary-sell
  "Returns an order summary for sell orders, filters out buy orders
  Orders by price ascending."
  [orders]
  (order-summary orders :sell (partial sort-by :price)))

(def desc #(compare %2 %1))

(defn order-summary-buy
  "Returns an order summary for buy orders, filters out sell orders.
  Orders by price descending."
  [orders]
  (order-summary orders :buy (partial sort-by :price desc)))


;; TODO: document any complicated functions - update project.clj with decent description, add readme etc - double check the SPEC!
; (remove #(= % order) coll) is simpler but this removes all orders that match
; Ideally I would introduce order-id and search for this instead