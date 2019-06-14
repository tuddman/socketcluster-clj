(ns socketcluster-clj.ping)

;; -- Ping  ---------------------

(defn enable-ping
  [socket]
  (.enablePing socket))

(defn disable-ping
  [socket]
  (.disablePing socket))

(defn is-ping-enabled?
  [socket]
  (.isPingEnabled socket))

(defn set-ping-interval
  "interval is in milliseconds"
  [socket interval]
  (.setPingInterval socket (long interval)))
