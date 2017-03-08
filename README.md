# live-order-board

A live order board for buy and sell orders

## Usage
```
lein repl
```

```clojure
(require '[live-order-board.core :refer :all])

; define an atom to contain orders
(def orders (atom []))

(def order {:user-id 1, :quantity 3.5, :price 306, :order-type :sell})

; register an order
(register-order! orders order)

; view the sell order summary
(order-summary-sell @orders)

; remove an order
(cancel-order! orders order)
```

## Notes
```clojure
(defn cancel-order!
  "takes an atom of a sequence of orders and an order, removes
  the order from the sequence"
  [orders order]
  (swap! orders (fn [coll]
                  (let [[coll-a coll-b] (split-with #(not= order %) coll)]
                    (concat coll-a (rest coll-b))))))
```
Cancel order removes the first match, in a real system I would introduce order-id
and use remove instead of splitting the list, removing the first match then joining 
the lists.
