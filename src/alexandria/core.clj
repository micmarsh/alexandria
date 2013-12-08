(ns alexandria.core
    (:use clojure.core.typed
        org.httpkit.server
        alexandria.server.types
        [alexandria.server :only [app]]))

(ann org.httpkit.server/run-server
    [ServerThing
    (HMap :mandatory
        {:port AnyInteger :join? Boolean})
        -> nil])
;doesn't seem like you should have to do this
(ann ^:no-check alexandria.server/app
    ServerThing)

(ann ^:no-check to-int [String -> AnyInteger])
(defn to-int [str]
    (Integer. str))

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
