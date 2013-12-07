(ns cascajal.core
    (:use clojure.core.typed))

;epublib
(import [nl.siegmann.epublib.epub EpubReader])

(def-alias Reader nl.siegmann.epublib.epub.EpubReader)
(def-alias Book nl.siegmann.epublib.domain.Book)
(def-alias Resource nl.siegmann.epublib.domain.Resource)

(ann reader Reader)
(def ^:private reader (EpubReader.))

(defmacro defjava [name args body]
    `(defn ~name ~args
        (try
            (~@body)
            (catch Exception e#
                (identity nil)))))

;(ann .readEpub [Reader java.io.FileInputStream -> Book])
(ann ^:no-check open-book [String -> (Option Book)])
(defjava open-book [name]
    (.readEpub reader
        (java.io.FileInputStream. name)))

;(ann Book/getContents [-> (Seqable Resource)])
(ann ^:no-check contents [Book -> (Option (Seqable Resource))])
(defjava contents [book]
    (.getContents book))

(ann do-stuff [Book -> nil])
(defn do-stuff [b] (println (contents b)))

(ann -main [-> nil])
(defn -main []
    (let [book-name "samples/1984.epub"
          book (open-book book-name)]
          (if book (do-stuff book)
            (println "error opening book"))))
