(ns financial-module.sections.header
  (:require
   [helix.core :refer [defnc]]
   [helix.dom :as d]
   [refx.alpha :as refx]))

(defnc header [{:keys [_]}]
  (d/div {:className "navbar bg-base-100"}
         (d/div {:className "navbar-start"}
                (d/a {:className "btn btn-ghost text-xl"
                      :href "/#/"}
                     (d/img {:src "/img/financial.png"
                             :width "20"
                             :alt "Financial Module logo"})
                     "Financial Module"))

         (d/div {:className "navbar-end"}
                (d/button {:className "btn text-xl"
                           :onClick (fn []
                                      (refx/dispatch [:set-session {}])
                                      (.reload js/location))}
                          "Logout"))))
