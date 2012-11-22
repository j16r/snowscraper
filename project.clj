(defproject snowscraper "1.0.0-SNAPSHOT"
  :main snowscraper.core
  :description "Scrape snow sites for relevant data"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/data.zip "0.1.1"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [compojure "1.1.1"]
                 [hiccup "1.0.1"]
                 [clojurewerkz/quartzite "1.0.1"]]
  :plugins [[lein-ring "0.7.1"]]
  :ring {:handler photo-wall.core/application})
