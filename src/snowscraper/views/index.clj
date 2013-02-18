(ns snowscraper.views.index
  (:use [hiccup core page]))

(defn- map-url [resort_name]
  (str "https://maps.google.com/maps?q=" resort_name))

(defn- draw-resorts [resorts]
  (map #(let [value (val %)
              title (:title value)]
          [:li
           [:a {:href (map-url title)} title]
           [:div
            [:span {:class "depth"} (str (:base_depth value) "\"")]
            [:span {:class "snowfall"} (str (:snowfall_48_hour value) "\"")]]])
       resorts))

(defn index [resorts]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:title "Can I board?"]
    (include-css "/stylesheets/snowscraper.css")
    [:body
     [:ol
      (draw-resorts resorts)]]]))
