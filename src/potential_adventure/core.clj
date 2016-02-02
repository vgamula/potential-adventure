(ns potential-adventure.core
  (:use org.httpkit.server
        [clojure.tools.logging :only [info]]
        (compojure [core :only [defroutes GET]]
                   [handler :only [site]]
                   [route :only [not-found files]])))

(defn vova-handler [request]
  (-> (info "get /vova")
      (str "Hello, Vova!")))

(defroutes routes
  (GET "/vova" [] vova-handler)
  (files "" {:root "static"}))


(defn -main [& args]
  (run-server (-> #'routes site) {:port 3000})
  (info "Server running at http://localhost:3000/"))
