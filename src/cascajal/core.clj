(ns cascajal.core
    (:use [cascajal.epublib :only [open-book contents Book]]
        clojure.core.typed))

(ann do-stuff [Book -> nil])
(defn do-stuff [b] (println (contents b)))

(ann -main [-> nil])
(defn -main []
    (let [book-name "samples/1984.epub"
          book (open-book book-name)]
          (if book (do-stuff book)
            (println "error opening book"))))
