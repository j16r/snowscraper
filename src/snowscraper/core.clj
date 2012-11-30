(ns snowscraper.core
  (:use [compojure.core :only [defroutes ANY]])
  (:require [snowscraper.scraper :as scraper]
            [snowscraper.views.index :as view]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :as ring]))

(defroutes routes
  (route/resources "/")
  (ANY "/" [] (view/index @scraper/resort-list)))

(def application (handler/site routes))

(defn start [port]
  (ring/run-jetty #'application {:port port :join? false}))

(defn -main [& args]
  (scraper/start)
  (let [port (-> (first args) (or 8080) (Integer.))]
    (start port)))
