(ns cascajal.core
    (:use [cascajal.epublib :only
        [open-book get-text get-title Book]]
        clojure.core.typed
        clojure.pprint))

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
    (let [book-name "samples/seven-habits.epub"
          book (open-book book-name)]
          (if book
            (pprint  (get-text book));(to-strings (get-char-stream book)))
            (println "error opening book"))))
