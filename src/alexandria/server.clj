(ns alexandria.server
    (:use [compojure.core :only [ANY GET POST defroutes]]
          [alexandria.server.resources :only [all-books]]
          clojure.core.typed
          alexandria.server.types
          ring.middleware.cors)
    (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :as reload]))

(ann ^:no-check routes ServerThing)
(defroutes routes
    (ANY "/" [] all-books)
    (route/resources "/"))

(ann ^:no-check app ServerThing)
(def app
    (-> routes
        handler/site
        reload/wrap-reload
    (wrap-cors
        :access-control-allow-origin #".+")))
