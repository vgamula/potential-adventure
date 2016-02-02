(ns potential-adventure.core
  (:gen-class)
  (:use org.httpkit.server
        [taoensso.timbre :as log]
        (compojure [core :only [defroutes GET]]
                   [handler :only [site]]
                   [route :only [not-found files]])))

(defn request-demo-handler [request]
  (-> (log/info "get /request-demo-handler")
      (str request)))

(defroutes routes
  (GET "/request-demo-handler" request (request-demo-handler request))
  (files "" {:root "static"}))


(defn -main [& args]
  (run-server (-> #'routes site) {:port 3000})
  (log/info "Server running at http://localhost:3000/"))
