(ns cascajal.epublib
    (:use clojure.core.typed
            [clj-xpath.core :only [$x]])
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

;(ann section-map [java.io.InputStream -> (HMap (something))])
(defn section-map [xml-stream]
    (->> xml-stream
        slurp
        ($x "//child::p")))

(ann book-stream [String -> (NonEmptyLazySeq String)])
(defn get-char-stream [book]
    (let [sections (contents book)
        ;TODO these^ sections are also an Option
          streams (section-streams sections)
        ;TODO each stream^ is an Option
          xml-maps (map section-map streams)
        ; probably not an Option, but beware^
        ]
        (mapcat :text (flatten xml-maps))))

;Okay, so ideally you want to provide a layer of abstraction where you can open
; a book-stream with one function. The layers involved Book -> Resources
; -> Readers -> XMLThingy (lazy) -> book contents (lazy, not 1-1 with XMLThingy)
;
