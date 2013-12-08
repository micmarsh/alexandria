(ns alexandria.server.resources
    (:use [liberator.core :refer [defresource]]
          [alexandria.books.html :only [books-page]]
          alexandria.server.types
          clojure.core.typed))

(ann ^:no-check all-books ServerThing)
(defresource all-books
    :available-media-types ["text/html"]
    :allowed-methods [:get]
    :handle-ok books-page)
