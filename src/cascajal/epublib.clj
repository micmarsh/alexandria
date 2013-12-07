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

(ann ^:no-check book-stream [Book -> (NonEmptyLazySeq Char)])
(defn  get-char-stream [book]
    (let [sections (contents book)
        ;TODO these^ sections are also an Option
          streams (section-streams sections)
        ;TODO each stream^ is an Option
          xml-maps (map section-map streams)
        ; probably not an Option, but beware^
        ]
        (mapcat :text (flatten xml-maps))))

(defn get-title [book]
    (.getTitle book))

(defn- to-strings
    ([char-stream]
        (to-strings char-stream 1000))
    ([char-stream length]
        (cons
            (apply str (take length char-stream))
            (lazy-seq
                (to-strings (drop length char-stream) length)))))

(def get-text (comp to-strings get-char-stream))

;Okay, so ideally you want to provide a layer of abstraction where you can open
; a book-stream with one function. The layers involved Book -> Resources
; -> Readers -> XMLThingy (lazy) -> book contents (lazy, not 1-1 with XMLThingy)
;
