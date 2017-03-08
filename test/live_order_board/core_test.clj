(ns live-order-board.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [live-order-board.core :refer :all])
  (:import [live_order_board.core Order]))

(def buy-a (Order. 1 3.5 306 :buy))
(def buy-b (Order. 2 1.2 310 :buy))
(def buy-c (Order. 3 1.5 307 :buy))
(def buy-d (Order. 4 2   306 :buy))

(def sell-a (Order. 1 3.5 306 :sell))
(def sell-b (Order. 2 1.2 310 :sell))
(def sell-c (Order. 3 1.5 307 :sell))
(def sell-d (Order. 4 2   306 :sell))

(deftest live-order-board-test
  (testing "Register an order"
    (let [orders (atom [])]
      (register-order! orders buy-a)
      (is (= [buy-a] @orders))))

  (testing "Cancel an order"
    (let [orders (atom [buy-a buy-b])]
      (cancel-order! orders buy-a)
      (is (= [buy-b] @orders))))

  (testing "Cancel order only removes the first match"
    (let [orders (atom [buy-a buy-b buy-a])]
      (cancel-order! orders buy-a)
      (is (= [buy-b buy-a] @orders))))

  (testing "Order summary for sell orders"
    (let [orders (atom [sell-a sell-b sell-c sell-d])
          summary (order-summary-sell @orders)]
      (is (= ["5.5 kg for £306"
              "1.5 kg for £307"
              "1.2 kg for £310"] summary))))

  (testing "Buy orders not included in order-summary-sell"
    (let [orders (atom [sell-a sell-b sell-c sell-d buy-a])
          summary (order-summary-sell @orders)]
      (is (= ["5.5 kg for £306"
              "1.5 kg for £307"
              "1.2 kg for £310"] summary))))

  (testing "Order summary for buy orders"
    (let [orders (atom [buy-a buy-b buy-c buy-d])
          summary (order-summary-buy @orders)]
      (is (= ["1.2 kg for £310"
              "1.5 kg for £307"
              "5.5 kg for £306"] summary))))

  (testing "Sell orders not included in order-summary-buy"
    (let [orders (atom [buy-a buy-b buy-c buy-d sell-a])
          summary (order-summary-buy @orders)]
      (is (= ["1.2 kg for £310"
              "1.5 kg for £307"
              "5.5 kg for £306"] summary)))))