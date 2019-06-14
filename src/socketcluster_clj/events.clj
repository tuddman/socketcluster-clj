(ns socketcluster-clj.events
  (:require
   [socketcluster-clj.listeners :refer [define-listener define-ack-listener]]))

;; -- Events  ---------------------

(defn emit
  [socket event msg]
  (.emit socket event msg))

(defn set-event-listener
  "f must be a fn that accepts 2 args: event-name data
  e.g. (fn [event-name data] ...))"
  [socket event f]
  (let [listener (define-listener f)]
    (.on socket event listener)))

(defn set-event-listener-with-ack
  "ack-fn must be a fn that accepts 3 args: event-name error data"
  [socket event ack-fn]
  (let [ack-listener (define-ack-listener (define-ack ack-fn))]
    (.on socket event ack-listener)))
