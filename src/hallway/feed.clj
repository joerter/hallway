(ns hallway.feed
  (:require
    [biff.util :as bu]
    [hallway.util :as u]))

(def xml-doc
  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")

(defn entry [{:keys [title url created author source]}]
  [:entry
   [:title title]
   [:link {:href url}]
   [:id url]
   [:updated (u/format-date created)]
   [:author [:name author]]
   [:category
    {:term source}]
   [:summary title]])

(defn feed [{:keys [params/url biff/base-url]} docs]
  (let [sorted-docs (sort-by :created #(compare %2 %1) docs)
        self (str base-url "/feed?url=" url)]
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:title (str "Discussions for " url)]
     [:link {:href self :rel "self"}]
     [:id self]
     (when (not-empty docs)
       [:updated (u/format-date (:created (first sorted-docs)))])
     (map entry sorted-docs)]))
