(ns cascajal.core
    (:use clojure.core.typed))

;epublib
(import [nl.siegmann.epublib.epub EpubReader])

(def-alias Reader nl.siegmann.epublib.epub.EpubReader)
(def-alias Book nl.siegmann.epublib.domain.Book)

(ann reader Reader)
(def ^:private reader (EpubReader.))

;(ann .readEpub [Reader java.io.FileInputStream -> Book])
(ann ^:no-check open-book [String -> Book])
(defn open-book [name]
    (.readEpub reader
        (java.io.FileInputStream. name)))

(ann -main [-> nil])
(defn -main []
    (println "yo"))
