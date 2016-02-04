(ns potential-adventure.core
  (:gen-class)
  (:use org.httpkit.server
        [potential-adventure.handlers :as handlers]
        [taoensso.timbre :as log]
        [clojure.data.json :only [read-json]]
        (compojure [core :only [defroutes GET]]
                   [handler :only [site]]
                   [route :only [not-found files]]))) 


(defroutes routes
  (GET "/ws" request (handlers/main-ws-handler request))
  (files "" {:root "static"}))


(defn -main [& args]
  (run-server (-> #'routes site) {:port 3000})
  (log/info "Server running at http://localhost:3000/"))
