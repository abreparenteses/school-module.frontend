(ns financial-module.routes
  (:require [financial-module.views.login :refer [login]]
            [financial-module.views :as views]))

(def routes
  ["/"
   [""
    {:name      ::home
     :view      views/view-subjects
     :link-text "Home"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       }]}]

   ["login"
    {:name      ::login
     :view      login
     :link-text "Login"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       }]}]

   ["courses"
    {:name      ::courses
     :view      views/view-courses
     :link-text "Courses"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       }]}]])
