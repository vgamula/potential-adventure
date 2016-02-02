(ns potential-adventure.core
  (:use org.httpkit.server
        (compojure [core :only [defroutes GET]]
                   [handler :only [site]]
                   [route :only [not-found]])))

(defn vova-handler [request]
  "Hello, Vova!")

(defroutes routes
  (GET "/vova" [] vova-handler))


(defn -main [& args]
  (run-server (-> #'routes site) {:port 3000}))
