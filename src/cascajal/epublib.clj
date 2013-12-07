(ns cascajal.epublib
    (:use clojure.core.typed))

(import [nl.siegmann.epublib.epub EpubReader])

(def-alias Reader nl.siegmann.epublib.epub.EpubReader)
(def-alias Book nl.siegmann.epublib.domain.Book)
(def-alias Section nl.siegmann.epublib.domain.Resource)

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
(ann ^:no-check contents [Book -> (Option (Vec Section))])
(defjava contents [book]
    (vec (.getContents book)))

(ann ^:no-check section-titles
    [(Vec Section) -> (Seqable (Option java.io.Reader))])
(defjava section-titles [sections]
    (map #(.getReader %) sections))

(ann ^:no-check read-part
    (Fn [java.io.Reader -> String]
        [java.io.Reader AnyInteger -> String]))
(defn read-part
    ([read-me]
        (read-part read-me 0))
    ([read-me offset]
        (let [length 100
              characters (char-array length)]
            (.read read-me characters offset length)
            (apply str characters))))
