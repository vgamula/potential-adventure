(ns potential-adventure.core
  (:gen-class)
  (:use org.httpkit.server
        [taoensso.timbre :as log]
        [clojure.data.json :only [read-json]]
        (compojure [core :only [defroutes GET]]
                   [handler :only [site]]
                   [route :only [not-found files]])))


(defn msg-received [msg]
  (let [data (read-json msg)]
    (log/info "msg reeived" data)))


(defn ws-handler [request]
  (with-channel request channel
    (log/info channel "connected")
    (on-receive channel #'msg-received)
    (on-close channel (fn [status]
                        (log/info "Channel closed" status)))))


(defroutes routes
  (GET "/ws" request (ws-handler request))
  (files "" {:root "static"}))


(defn -main [& args]
  (run-server (-> #'routes site) {:port 3000})
  (log/info "Server running at http://localhost:3000/"))
