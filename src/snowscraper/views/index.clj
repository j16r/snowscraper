(ns snowscraper.views.index
  (:use [hiccup core page]))

(defn- map-url [resort_name]
  (str "https://maps.google.com/maps?q=" resort_name))

(defn- draw-resorts [resorts]
  (map #(let [value (val %)
              title (:title value)]
          [:li
           [:a {:href (map-url title)} [:h3 title]]
           [:p {:class "depth"} (:base_depth value)]
           [:p {:class "snowfall"} (:snowfall_48_hour value)]
           [:div {:class "clear"}]])
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
     [:h1 "Can I board?"]
     [:header "Yes! Here are some resorts near you:"]
     [:ol
      (draw-resorts resorts)]]]))
