(ns snowscraper.scraper
  (:use [clojure.data.zip.xml]
        [clojurewerkz.quartzite.jobs :only [defjob]]
        [clojurewerkz.quartzite.schedule.simple :only [schedule repeat-forever with-interval-in-milliseconds]])
  (:require [clojure.contrib.string :as string]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]))

(def resort-list (ref {}))

;(def feed-url "http://www.onthesnow.com/new-york/snow.rss")
(def feed-url "http://www.onthesnow.com/vermont/snow.rss")

(defn feed [] (zip/xml-zip (xml/parse feed-url)))

(defn open-resorts [resorts]
  (filter #(-> % val :status (string/substring? "Open")) resorts))

(defn resorts []
  (into {}
        (let [text-node (fn [item name] (xml1-> item name text))]
          (map #(vector
                  (xml1-> % :title text)
                  {:title (text-node % :title)
                   :description (text-node % :description)
                   :status (text-node % :ots:open_staus)
                   :base_depth (text-node % :ots:base_depth)
                   :snowfall_48_hour (text-node % :ots:snowfall_48hr)
                   :region_name (text-node % :ots:region_name)
                   :surface_conditions (text-node % :ots:surface_conditions)
                   :base_depth_metric (text-node % :ots:base_depth_metric)
                   :snowfall_48_hours_metric (text-node % :ots:snowfall_48hr_metric)
                   :resort_feed (text-node % :ots:resort_css_link)})
             (xml-> (feed) :channel :item)))))

(defjob scrape-sites [context]
  (dosync (ref-set resort-list (open-resorts (resorts)))))

(def polling-interval 36000000)

(defn start []
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
