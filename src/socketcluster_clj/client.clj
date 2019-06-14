(ns socketcluster-clj.client
  (:import [io.github.sac
            ReconnectionStrategy
            Socket]))

(def local-url "ws://localhost:8000/socketcluster/")

;; -- Connection ---------------------

(defn new-socket
  [url]
  (Socket. url))

(defn connect
  [socket]
  (.connectAsync socket))

(defn disconnect
  ([socket] (.disconnect socket))
  ([socket reason]  (.disconnect socket reason)))

(defn reconnect
  [socket]
  (.reconnect socket))

(defn get-state
  [socket]
  (.toString (.getState socket)))

(defn is-connected?
  [socket]
  (.isconnected socket))

(defn set-extra-headers
  [socket headers override-defaults]
  (.setExtraHeaders socket headers override-defaults))

(defn get-headers
  [socket]
  (.getHeaders socket))

(defn set-reconnect-strategy
  [socket ms-delay max-attempts]
  (.setReconnection
   socket (-> (ReconnectionStrategy. (int ms-delay) (int max-attempts)))))

(defn remove-reconnect-strategy!
  [socket]
  (.setReconnection socket nil))

(defn set-url
  [socket url]
  (.setUrl socket url))

(defn set-auth-token
  [socket token]
  (.setAuthToken socket token))

;; -- Logging  ---------------------

(defn get-logger
  [socket]
  (.getLogger socket))

(defn disable-logging
  [socket]
  (.disableLogging socket))
