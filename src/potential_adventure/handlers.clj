(ns potential-adventure.handlers
  (:gen-class)
  (:use org.httpkit.server
        [taoensso.carmine :as car :refer (wcar)]
        [taoensso.timbre :as log]
        [clojure.data.json :only [read-json write-str]]))

(def channel "my-channel")
(def redis-conn {:pool {} :spec {:host "docker.loc" :port 6379}})
(defmacro redis [& body] `(car/wcar redis-conn ~@body))


(defn get-redis-listener [socket]
  (car/with-new-pubsub-listener (:spec redis-conn)
    {channel (fn [[type match content]]
               (send! socket (write-str {:msg content}))
               (log/info "new pubsub msg:" content))}
    (car/subscribe channel)))


(defn data-received [socket msg]
  (let [data (read-json msg)]
    (redis (car/publish channel (get data :msg)))
    (log/info "New msg received" (get data :msg) "from" socket)))


(defn socket-closed [socket listener status]
  (car/with-open-listener listener
    (car/unsubscribe)
    (log/info "Disconnected from pubsub"))
  (log/info "Socket" socket "closed" status))


(defn main-ws-handler [request]
  (with-channel request socket
    (def listener (get-redis-listener socket))
    (on-receive socket (partial data-received socket))
    (on-close socket (partial socket-closed socket listener))
    (log/info "New socket connected:" socket)))