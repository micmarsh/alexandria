(ns cascajal.core
    (:use [cascajal.epublib :only
        [open-book book-stream Book]]
        clojure.core.typed))

(ann do-stuff [Book -> nil])
(defn do-stuff [b]
    (let [sections true]
        (if sections
            ; (let [streams (section-streams sections)
            ;       a-reader (last streams)
            ;       n (println streams)
            ;       string (if a-reader (read-part a-reader) "nope")]
            ;       (println string))
            (println "U didn't get a nil sections"))))

(ann -main [-> nil])
(defn -main []
    (let [book-name "samples/1984.epub"
          book (open-book book-name)]
          (if book
            (println  (book-stream book-name))
            (println "error opening book"))))
