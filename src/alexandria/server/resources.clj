(ns alexandria.server.resources
    (:use [liberator.core :refer [defresource]]
          [alexandria.books.html :only [books-page book-page]]
          alexandria.server.types
          clojure.core.typed))

(defn- get-params [context]
    (get-in context [:request :params]))

(def get-id (comp :id get-params))

(defresource read-book
    :available-media-types ["text/html"]
    :allowed-methods [:get]
    :handle-ok (fn [ctx]
        (let [id (get-id ctx)]
            (book-page id))))

(ann ^:no-check all-books ServerThing)
(defresource all-books
    :available-media-types ["text/html"]
    :allowed-methods [:get]
    :handle-ok (fn [ctx] (books-page)))
