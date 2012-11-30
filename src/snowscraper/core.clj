(ns snowscraper.core
  (:require [clojure.contrib.string :as string]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j])
  (:use [hiccup core page]
        [compojure.core :only [defroutes ANY]]
        [compojure.route :as route]
        [compojure.handler :as handler]
        [ring.adapter.jetty :as ring]
        [clojurewerkz.quartzite.jobs :only [defjob]]
        [clojure.data.zip.xml]
        [clojurewerkz.quartzite.schedule.simple :only [schedule repeat-forever with-interval-in-milliseconds]]))

(def resort-list (ref {}))

;(def feed-url "http://www.onthesnow.com/new-york/snow.rss")
(def feed-url "http://www.onthesnow.com/vermont/snow.rss")

(defn feed [] (zip/xml-zip (xml/parse feed-url)))

(defn open-resorts [resorts]
  (filter #(-> % val :status (string/substring? "Open")) resorts))

(defn resorts []
  (into {}
        (map #(vector
                (xml1-> % :title text)
                {:title (xml1-> % :title text)
                 :description (xml1-> % :description text)
                 :status (xml1-> % :ots:open_staus text)})
             (xml-> (feed) :channel :item))))

(defjob scrape-sites [context]
  (dosync (ref-set resort-list (open-resorts (resorts)))))

(def polling-interval 36000000)

(defn start-scraper []
  (qs/initialize)
  (qs/start)
  (let [job (j/build
              (j/of-type scrape-sites)
              (j/with-identity (j/key "jobs.snowscraper.1")))
        trigger (t/build
                  (t/with-identity (t/key "triggers.1"))
                  (t/start-now)
                  (t/with-schedule (schedule
                                     (repeat-forever)
                                     (with-interval-in-milliseconds polling-interval))))]
  (qs/schedule job trigger)))

(defn index []
  (println "Rendering index...")
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:title "Can I board?"]
    (include-css "/stylesheets/snowscraper.css")
    [:body
     [:ol
      (map #(let [value (val %)
                  title (:title value)]
              [:li
               [:h3 title]
               [:p (:status value)]
               [:p (:description value)]])
           @resort-list)]]]))

(defroutes routes
  (route/resources "/")
  (ANY "/" [] (index)))

(def application (handler/site routes))

(defn start [port]
  (ring/run-jetty #'application {:port port :join? false}))

(defn -main [& args]
  (start-scraper)
  (let [port (-> (first args) (or 8080) (Integer.))]
    (start port)))
