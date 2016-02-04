(ns potential-adventure.handlers
  (:gen-class)
  (:use org.httpkit.server
        [taoensso.timbre :as log]
        [clojure.data.json :only [read-json]]))


(defn data-received [channel msg]
  (let [data (read-json msg)]
    (log/info "msg reeived" data "from" channel)))


(defn channel-closed [channel status]
  (log/info "Channel" channel "closed" status))


(defn main-ws-handler [request]
  (with-channel request channel
    (log/info channel "connected")
    (on-receive channel (partial data-received channel))
    (on-close channel (partial channel-closed channel))))
