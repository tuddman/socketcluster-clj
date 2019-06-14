(ns socketcluster-clj.listeners
  (:import [io.github.sac Ack BasicListener Emitter]))

;; -- Acks ---------------------

(defn ^:private define-ack
  "f must be a fn that accepts 3 args: event-name (nom) error data"
  [f]
  (reify Ack
    (call [_ nom error data] (f nom error data))))

;; -- Set the client Listener  ---------------------

(defn set-listener
  "define the listener-handler functions:
  :on-connected 
  :on-disconnected 
  :on-connect-error 
  :on-authentication 
  :on-set-auth-token
  in the form (set-listener <socket> :on-connected (fn [socket headers]) ...)
  "
  [socket & {:keys [on-connected on-disconnected on-connect-error on-authentication on-set-auth-token]
             :or {on-connected  (fn [_ _ headers] (printf "connected with headers %s" headers))
                  on-disconnected (fn [_ _ code reason] (printf "disconnected with code %s : reason %s" code reason))
                  on-connect-error (fn [_ _ _ response] (printf "connect error %s" response))
                  on-authentication (fn [_ _ status] (printf "authenticated with status %s" status))
                  on-set-auth-token (fn [_ token _] (printf "token %s set"))}}]
  (let [listener (reify BasicListener
                   (onConnected [this socket headers]
                     (on-connected this socket headers))
                   (onDisconnected [this socket code reason]
                     (on-disconnected this socket code reason))
                   (onConnectError [this socket throwable response]
                     (on-connect-error this socket (.getMessage throwable) response))
                   (onAuthentication [this socket status]
                     (on-authentication this socket status))
                   (onSetAuthToken [this token socket]
                     (on-set-auth-token this token socket)))]
    (.setListener socket listener)))

;; -- Listeners  ---------------------

(defn define-listener
  "f must be a fn that accepts 2 args: event-name data
  e.g. (def my-f (define-listener (fn [event-name data] ...)))"
  [f]
  (reify io.github.sac.Emitter$Listener
    (call [_ event-name data]
      (f event-name data))))

(defn define-ack-listener
  "f must be a fn that accepts 3 args: event-name data ack,
  where ack must itself also be a fn that accepts 3 args: event-name error data
  e.g. 
  (def my-ack (define-ack (fn [event-name error data] ...)))
  (def my-f (define-ack-listener (fn [event-name data my-ack] ...)))"
  [f]
  (reify io.github.sac.Emitter$AckListener
    (call [_ event-name data ack] (f event-name data ack))))
