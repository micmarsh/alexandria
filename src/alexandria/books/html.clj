(ns alexandria.books.html
    (:use [bookworm.core :only
        [get-title open-book get-html]]
          [hiccup.core :only [html]]))

(defn list-files []
    (let [path "samples/"
          dir (java.io.File. path)]
          (.listFiles dir)))

(defn file-to-id [file]
    (let [full-name (.getName file)
          length (count full-name)]
        (apply str
          (take (- length 5) full-name))))


(defn to-books [files]
    (map open-book files))

(defn book-list []
    (->> (list-files)
        to-books
        (map get-title)))

(defn lookup-book [id]
    "This is very high level and will be moved
    to some database namespace once such a thing exists"
    (let [match-id #(= (str id ".epub") (.getName %))
          files (list-files)
          i (println id)
          n (println (map #(.getName %) files))]
          (first (filter match-id files))))


(def PAGE_TITLE "Alexandria")
(def PAGE_DESCRIPTION "Share and read books on any device")

(defn add-header [body]
    (str (html [:head
        [:title PAGE_TITLE]])
            body))

(defn list-template [book-files]
    (html [:body
        [:h1 PAGE_TITLE]
        [:p PAGE_DESCRIPTION]
        [:ul
            (for [book-file book-files]
                (let [book (open-book book-file)
                      title (get-title book)
                      id (file-to-id book-file)]
                [:li
                    [:a
                        {:href (str "/books/" id)}
                        title]]))]]))

(defn book-text [id]
    (let [book-file (lookup-book id)
          book (open-book book-file)
          ;need to save this stream in a session or something
          text-stream (get-html book)]
          (apply str (take 100 text-stream))))

(defn book-page [id]
    (-> id book-text add-header))


(def books-page
    (comp
        add-header
        list-template
        list-files))






