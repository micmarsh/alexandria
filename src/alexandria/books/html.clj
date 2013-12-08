(ns alexandria.books.html
    (:use [bookworm.core :only
        [get-title open-book get-text]]
          [hiccup.core :only
        [html]]))

(defn list-files []
    (let [path "samples/"
          dir (java.io.File. path)]
          (.listFiles dir)))

(defn to-books [files]
    (map open-book files))

(defn book-list []
    (->> (list-files)
        to-books
        (map get-title)))

(defn list-template [books]
    (html [:body
        [:ul
            (for [title books]
                [:li title])]]))

(def books-page (comp list-template book-list))






