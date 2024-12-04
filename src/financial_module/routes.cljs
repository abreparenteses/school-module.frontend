(ns financial-module.routes
  (:require [financial-module.views.login :refer [login]]
            [financial-module.views :as views]))

(defn- set-title! [title]
  (set! (.-title js/document) title))

(def routes
  ["/"
   [""
    {:name      ::home
     :view      views/view-home
     :link-text "Home"
     :controllers
     [{:start (fn [& _params]
                (set-title! "home"))}]}]

   ["login"
    {:name      ::login
     :view      login
     :link-text "Login"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       :start (fn [& _params]
                (set-title! "login"))}]}]

   ["courses"
    {:name      ::courses
     :view      views/view-courses
     :link-text "Courses"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       :start (fn [& _params]
                (set-title! "courses"))}]}]

   ["students"
    {:name      ::students
     :view      views/view-students
     :link-text "Students"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       :start (fn [& _params]
                (set-title! "students"))}]}]

   ["subjects"
    {:name      ::subjects
     :view      views/view-subjects
     :link-text "Subjects"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       :start (fn [& _params]
                (set-title! "subjects"))}]}]

   ["attending"
    {:name      ::attending
     :view      views/view-attending
     :link-text "Attending"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (refx/dispatch [::events/load-something-with-ajax])
       ;; Teardown can be done here.
       :start (fn [& _params]
                (set-title! "attending"))}]}]])
