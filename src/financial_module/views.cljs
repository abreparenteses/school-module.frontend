(ns financial-module.views
  (:require [financial-module.views.courses :as views.courses]
            [financial-module.views.main :as views.main]
            [financial-module.views.login :as views.login]
            [financial-module.views.subjects :as views.subjects]
            [helix.core :refer [$ <> defnc]]
            [helix.dom :as d]
            [refx.alpha :as refx]))

(defn redirect! [loc]
  (set! (.-location js/window) loc))

(defnc home [{:keys [_]}]
  (let [session (refx/use-sub [:session])]
    (<>
     (d/main
      (if (empty? (:token session))
        ($ views.login/login)
        ($ views.main/main {:session session}))))))

(defnc view-courses [{:keys [_]}]
  (let [session (refx/use-sub [:session])]
    (<>
     (d/main
      (if (empty? (:token session))
        #_(redirect! "/")
        ($ views.login/login)
        ($ views.courses/main {:session session}))))))

(defnc view-subjects [{:keys [_]}]
  (let [session (refx/use-sub [:session])]
    (<>
     (d/main
      (if (empty? (:token session))
        #_(redirect! "/")
        ($ views.login/login)
        ($ views.subjects/main {:session session}))))))
