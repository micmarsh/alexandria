(ns cascajal.core
    (:use clojure.core.typed))

;epublib
(import [nl.siegmann.epublib.epub EpubReader])

(ann -main [-> nil])
(defn -main []
    (println "yo"))
