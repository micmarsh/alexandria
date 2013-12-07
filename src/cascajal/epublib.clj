(ns cascajal.epublib
    (:use clojure.core.typed)
    (:require [clojure.xml :as xml]
              [clojure.zip :as zip]))

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
(defn open-book [name]
    (.readEpub reader
        (java.io.FileInputStream. name)))

;(ann Book/getContents [-> (Seqable Resource)])
(ann ^:no-check contents [Book -> (Option (Vec Section))])
(defn contents [book]
    (vec (.getContents book)))

(ann ^:no-check section-streams
    [(Vec Section) -> (Seqable (Option java.io.InputStream))])
(defn section-streams [sections]
    (map #(.getInputStream %) sections))

(ann ^:no-check read-part
    (Fn [java.io.Reader -> String]
        [java.io.Reader AnyInteger -> String]))
(defn read-part
    ([read-me]
        (read-part read-me 0))
    ([read-me offset]
        (let [length 1000
              characters (char-array length)]
            (.read read-me characters offset length)
            (apply str characters))))

;(ann section-map [java.io.InputStream -> (HMap (something))])
(defn section-map [xml-stream]
    (-> xml-stream
        xml/parse
        zip/xml-zip))

(ann book-stream [String -> (NonEmptyLazySeq String)])
(defn book-stream [book-name]
    (let [book (open-book book-name)
        ;TODO this^ book is actually an Option
          sections (contents book)
        ;TODO these^ sections are also an Option
          streams (section-streams sections)
        ;TODO each stream^ is an Option
          xml-maps (map section-map streams)
        ; probably not an Option, but beware^
        ]
        (second (rest xml-maps)) ))

;Okay, so ideally you want to provide a layer of abstraction where you can open
; a book-stream with one function. The layers involved Book -> Resources
; -> Readers -> XMLThingy (lazy) -> book contents (lazy, not 1-1 with XMLThingy)
;
