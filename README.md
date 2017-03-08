# live-order-board

A live order board for buy and sell orders

## Usage

```
lein repl
```

```clojure
(require '[live-order-board.core :refer :all])
(import '[live_order_board.core Order])
```

### Register a sell order, view the sell order summary, cancel the order
```clojure
; define an atom to contain orders
(def orders (atom []))

; register an order
(def sell-a (Order. 1 3.5 306 :sell))
(register-order! orders sell-a)

; view the sell order summary
(order-summary-sell @orders)

; remove an order
(cancel-order! orders sell-a)
```