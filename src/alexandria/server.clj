(ns alexandria.server
    (:use [compojure.core :only [ANY GET POST defroutes]]
          [alexandria.server.resources :only [all-books read-book]]
          clojure.core.typed
          alexandria.server.types
          ring.middleware.cors)
    (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :as reload]))

(ann ^:no-check routes ServerThing)
(defroutes routes
    (ANY "/" [] all-books)
    (ANY "/books" [] all-books)
    (ANY "/books/:id" [] read-book)
    (route/resources "/"))

(ann ^:no-check app ServerThing)
(def app
    (-> routes
        handler/site
        reload/wrap-reload
    (wrap-cors
        :access-control-allow-origin #".+")))
