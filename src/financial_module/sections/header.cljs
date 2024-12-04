(ns financial-module.sections.header
  (:require
   [helix.core :refer [defnc]]
   [helix.dom :as d]
   [refx.alpha :as refx]))

(defnc header [{:keys [_]}]
  (d/div {:className "navbar bg-base-100"}
         (d/div {:className "navbar-start"}
                (d/div {:className "dropdown"}
                       (d/div {:tabIndex 0
                               :role "button"
                               :className "btn btn-ghost lg:hidden"}
                              (d/svg {:xmlns "http://www.w3.org/2000/svg"
                                      :className "h-5 w-5"
                                      :fill "none"
                                      :viewBox "0 0 24 24"
                                      :stroke "currentColor"}
                                     (d/path {:strokeLinecap "round"
                                              :strokeLinejoin "round"
                                              :strokeWidth "2"
                                              :d "M4 6h16M4 12h8m-8 6h16"})))
                       (d/ul {:tabIndex 0
                              :className "menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow"}
                             (d/li (d/a {:href "/#/courses"} "Courses"))
                             (d/li (d/a {:href "/#/students"} "Students"))
                             (d/li (d/a {:href "/#/subjects"} "Subjects"))
                             (d/li (d/a {:href "/#/attending"} "Attending"))))
                (d/a {:className "btn btn-ghost text-xl"
                      :href "/#/"}
                     (d/img {:src "/img/school.png"
                             :width "20"
                             :alt "School Module logo"})
                     "School Module"))

         (d/div {:className "navbar-center hidden lg:flex"}
                (d/ul {:className "menu menu-horizontal px-1"}
                      (d/li (d/a {:href "/#/courses"} "Courses"))
                      (d/li (d/a {:href "/#/students"} "Students"))
                      (d/li (d/a {:href "/#/subjects"} "Subjects"))
                      (d/li (d/a {:href "/#/attending"} "Attending"))))

         (d/div {:className "navbar-end"}
                (d/button {:className "btn text-xl"
                           :onClick (fn []
                                      (refx/dispatch [:set-session {}])
                                      (.reload js/location))}
                          "Logout"))))
