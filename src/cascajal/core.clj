(ns cascajal.core
    (:use [cascajal.epublib :only
        [open-book get-text get-title Book]]
        clojure.core.typed
        clojure.pprint))

(ann -main [-> nil])
(defn -main []
    (let [book-name "samples/seven-habits.epub"
          book (open-book book-name)]
          (if book
            (pprint  (get-text book));(to-strings (get-char-stream book)))
            (println "error opening book"))))
