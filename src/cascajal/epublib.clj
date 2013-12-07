(ns cascajal.epublib
    (:use clojure.core.typed))

(import [nl.siegmann.epublib.epub EpubReader])

(def-alias Reader nl.siegmann.epublib.epub.EpubReader)
(def-alias Book nl.siegmann.epublib.domain.Book)
(def-alias Resource nl.siegmann.epublib.domain.Resource)

(ann reader Reader)
(def ^:private reader (EpubReader.))

(defmacro defjava
    "Define a function that returns nil instead of throwing exceptions,
    so we can have a meaningful Option type. Right now used just for java
    interop, hence the name"
    [name args body]
    `(defn ~name ~args
        (try
            (~@body)
            (catch Exception e#
                (do
                    (println (.getMessage e#))
                    nil)))))

;(ann .readEpub [Reader java.io.FileInputStream -> Book])
(ann ^:no-check open-book [String -> (Option Book)])
(defjava open-book [name]
    (.readEpub reader
        (java.io.FileInputStream. name)))

;(ann Book/getContents [-> (Seqable Resource)])
(ann ^:no-check contents [Book -> (Option (Vec Resource))])
(defjava contents [book]
    (vec (.getContents book)))
