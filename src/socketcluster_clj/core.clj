(ns socketcluster-clj.core
  (:import [io.github.sac
            Ack
            BasicListener
            Emitter
            ; EventThread
            ; Parser
            ReconnectionStrategy
            Socket]))

(def url "ws://localhost:8000/socketcluster/")

;; -- Acks ---------------------

(defn ack-fn
  [ch-name]
  (reify Ack
    (call [this ch-name error data] (print ch-name error data))))


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

;; -- Events  ---------------------

(defn emit
  [socket event msg]
  (.emit socket event msg))

(defn event-listener
  [event]
  (fn []
    (reify io.github.sac.Emitter$Listener
      (call [this event data]
        (print "event " event "data" data)))))

(defn set-event-listener
  [socket event]
  (.on socket event (event-listener event)))

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

(defn unsubscribe
  [socket ch-name]
  (.unsubscribe (get-channel socket ch-name)))

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

;; -- Logging  ---------------------

(defn get-logger
  [socket]
  (.getLogger socket))

(defn disable-logging
  [socket]
  (.disableLogging socket))

;; -- Set Stuff  ---------------------

(defn set-auth-token
  [socket token]
  (.setAuthToken socket token))

(defn set-listener
  "define the listener-handler functions:
    - :on-connected 
    - :on-disconnected 
    - :on-connect-error 
    - :on-authentication 
    - :on-set-auth-token
  in the form (set-listener <socket> :on-connected (fn [socket headers]) ...)
  "
  [socket & {:keys [on-connected on-disconnected on-connect-error on-authentication on-set-auth-token]
             :or {on-connected  (fn [_ _ headers] (printf "connected with headers %s" headers))
                  on-disconnected (fn [_ _ code reason] (printf "disconnected from server %s %s" code reason))
                  on-connect-error (fn [_ _ throwable response] (printf "connect error :  %s" throwable response))
                  on-authentication (fn [_ _ status] (printf "authenticated with status %s" status))
                  on-set-auth-token (fn [_ token _]  (printf "token %s set" token))}}]
  (.setListener socket
                (reify BasicListener
                  (onConnected [this socket headers]
                    (on-connected this socket headers))
                  (onDisconnected [this socket code reason]
                    (on-disconnected this socket code reason))
                  (onConnectError [this socket throwable response]
                    (on-connect-error this socket throwable response))
                  (onAuthentication [this socket status]
                    (on-authentication this socket status))
                  (onSetAuthToken [this token socket]
                    (on-set-auth-token this token socket)))))

(defn print-msg
  "name can either be channel name or event name"
  [name data]
  (printf "thing named %s emitted %s" name data))

(defn set-ch-on-message-fn
  "f must take two args: ch-name data.
  e.g. (defn f [ch-name data] (fn [ch-name data] ...))"
  [socket ch-name f]
  (.onMessage (get-channel socket ch-name)
              (reify io.github.sac.Emitter$Listener
                (call [this ch-name data]
                  (print-msg ch-name data)))))

(defn set-reconnect-strategy
  [socket ms-delay max-attempts]
  (.setReconnection socket (-> (ReconnectionStrategy. (Integer. ms-delay) (Integer. max-attempts)))))

(defn set-url
  [socket url]
  (.setUrl socket url))
