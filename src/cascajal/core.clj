(ns cascajal.core
    (:use [cascajal.epublib :only [open-book contents read-part section-titles Book]]
        clojure.core.typed))

(ann do-stuff [Book -> nil])
(defn do-stuff [b]
    (let [sections (contents b)]
        (if sections
            (let [readers (section-titles sections)
                  a-reader (first readers)
                  n (println readers)
                  string (if a-reader (read-part a-reader) "nope")]
                  (println string))
            (println "U got a nil sections"))))

(ann -main [-> nil])
(defn -main []
    (let [book-name "samples/1984.epub"
          book (open-book book-name)]
          (if book (do-stuff book)
            (println "error opening book"))))
