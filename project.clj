(defproject alexandria "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                [org.clojure/core.typed "0.2.19"]
                [bookworm "0.2.2"]
                [liberator "0.10.0"]
                [compojure "1.1.5"]
                [http-kit "2.1.13"]
                [ring/ring-devel "1.1.8"]
                [ring-cors "0.1.0"]
                [hiccup "1.0.4"]
                [garden "1.1.3"]]
  :plugins [[lein-localrepo "0.4.0"]]
  :min-lein-version "2.0"
  :main alexandria.core
)

