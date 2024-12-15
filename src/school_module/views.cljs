(ns school-module.views
  (:require [school-module.views.attending :as views.attending]
            [school-module.views.courses :as views.courses]
            [school-module.views.main :as views.main]
            [school-module.views.login :as views.login]
            [school-module.views.students :as views.students]
            [school-module.views.subjects :as views.subjects]
            [helix.core :refer [$ <> defnc]]
            [helix.dom :as d]
            [refx.alpha :as refx]))

(defn redirect! [loc]
  (set! (.-location js/window) loc))

(defnc view-home [{:keys [_]}]
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
        (redirect! "/")
        ($ views.courses/main {:session session}))))))

(defnc view-students [{:keys [_]}]
  (let [session (refx/use-sub [:session])]
    (<>
     (d/main
      (if (empty? (:token session))
        (redirect! "/")
        ($ views.students/main {:session session}))))))

(defnc view-subjects [{:keys [_]}]
  (let [session (refx/use-sub [:session])]
    (<>
     (d/main
      (if (empty? (:token session))
        (redirect! "/")
        ($ views.subjects/main {:session session}))))))

(defnc view-attending [{:keys [_]}]
  (let [session (refx/use-sub [:session])]
    (<>
     (d/main
      (if (empty? (:token session))
        (redirect! "/")
        ($ views.attending/main {:session session}))))))
