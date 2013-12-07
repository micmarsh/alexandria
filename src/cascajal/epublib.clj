(ns cascajal.epublib
    (:use clojure.core.typed
            [clj-xpath.core :only [$x]])
    (:require [clojure.xml :as xml]
              [clojure.zip :as zip]))

(import [nl.siegmann.epublib.epub EpubReader])

(def-alias Book nl.siegmann.epublib.domain.Book)
(def-alias Resource nl.siegmann.epublib.domain.Resource)

(def-alias StringSeq (NonEmptyLazySeq String))
(def-alias CharSeq (NonEmptyLazySeq Character))

(ann ^:no-check open-book [String -> (Option Book)])
(def open-book
    (let [reader (EpubReader.)]
        (fn [name]
            (.readEpub reader
                (java.io.FileInputStream. name)))))

(ann ^:no-check contents [Book -> (Vec Resource)])
(defn contents [book]
    (vec (.getContents book)))

(ann ^:no-check section-streams
    [(Vec Resource) -> (Seqable (Option java.io.InputStream))])
(defn section-streams [sections]
    (map #(.getInputStream %) sections))

(ann ^:no-check section-map [java.io.InputStream -> Seqable])
(defn section-map [xml-stream]
    (->> xml-stream
        slurp
        ($x "//child::p")))

(ann get-char-stream [Book -> CharSeq])
(defn-  get-char-stream [book]
    (let [sections (contents book)
          streams (section-streams sections)
        ;TODO each stream^ is an Option
          xml-maps (map section-map streams)
        ; probably not an Option, but beware^
        ]
        (mapcat :text (flatten xml-maps))))

(ann to-strings
    (Fn [CharSeq -> StringSeq]
        [CharSeq AnyInteger -> StringSeq]))
(defn- to-strings
    ([char-stream]
        (to-strings char-stream 1000))
    ([char-stream length]
        (cons
            (apply str (take length char-stream))
            (lazy-seq
                (to-strings (drop length char-stream) length)))))

(ann get-text [Book -> StringSeq])
(def get-text (comp to-strings get-char-stream))

(ann ^:no-check get-title [Book -> (Option String)])
(defn get-title [book]
    (.getTitle book))

;Okay, so ideally you want to provide a layer of abstraction where you can open
; a book-stream with one function. The layers involved Book -> Resources
; -> Readers -> XMLThingy (lazy) -> book contents (lazy, not 1-1 with XMLThingy)
;
