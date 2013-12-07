(defproject cascajal "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                [nl.siegmann.epublib/epublib-core "3.1"]
                [org.clojure/core.typed "0.2.19"]
                [com.github.kyleburton/clj-xpath "1.4.3"]]
  :plugins [[lein-localrepo "0.4.0"]]
  :main cascajal.core
)

