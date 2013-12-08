(ns cascajal.core
    (:use [compojure.core :only [ANY GET POST defroutes]]
        clojure.core.typed
        ring.middleware.cors
        org.httpkit.server)
    (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :as reload]))

(def-alias ServerThing Any)

(ann ^:no-check routes ServerThing)
(defroutes routes
    (ANY "/" [] "woffffooo")
    (route/resources "/"))

(ann ^:no-check app ServerThing)
(def app
    (-> routes
        handler/site
        reload/wrap-reload
    (wrap-cors
        :access-control-allow-origin #".+")))

(ann ^:no-check to-int [String -> AnyInteger])
(defn to-int [str]
    (Integer. str))

(ann org.httpkit.server/run-server
    [ServerThing
    (HMap :mandatory {:port AnyInteger :join? Boolean})
        -> nil])

(ann -main
    (Fn [-> nil]
        [String -> nil]))
(defn -main
    ([]
        (-main "3000"))
    ([port] ;heroku!
        (println (str "Running server on port " port))
        (run-server app
            {:port (to-int port) :join? false})))
