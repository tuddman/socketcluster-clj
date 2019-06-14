(ns socketcluster-clj.channels
  (:require
   [socketcluster-clj.listeners :refer [define-listener define-ack-listener]]
   [taoensso.timbre :as log :refer [info error]]))

;; -- Channels  ---------------------

(defn create-channel
  [socket ch-name]
  (.createChannel socket ch-name))

(defn get-channel
  [socket ch-name]
  (.getChannelByName socket ch-name))

(defn get-channels
  [socket]
  (.getChannels socket))

(defn channel-name
  [channel]
  (.getChannelName channel))

(defn publish
  [socket ch-name msg]
  (.publish (get-channel socket ch-name) msg))

(defn subscribe
  [socket ch-name]
  (.subscribe (get-channel socket ch-name)))

(defn subscribe-with-ack
  [socket ch-name ack-fn]
  (let [ack (if ack-fn
              (define-ack ack-fn)
              (define-ack (fn [ch-name error data]
                            (log/info "successfully subscribed to " ch-name)
                            (if error
                              (log/error error)
                              (log/info data)))))]
    (.subscribe (get-channel socket ch-name) ack)))

(defn unsubscribe
  [socket ch-name]
  (.unsubscribe (get-channel socket ch-name)))

(defn set-channel-listener
  "f must be a fn that accepts 2 args: ch-name data
  e.g. (fn [ch-name data] ...))"
  [socket ch-name f]
  (let [channel (get-channel socket ch-name)
        listener (define-listener f)]
    (.onMessage channel listener)))
