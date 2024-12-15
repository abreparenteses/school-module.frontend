(ns school-module.views.main
  (:require [school-module.sections.header :refer [header]]
            [school-module.sections.footer :refer [footer]]
            [helix.core :refer [$ defnc]]
            [helix.dom :as d]))

(defnc main [{:keys [_]}]
  (d/div {:class "mx-auto"}
         ($ header {})
         (d/div {:className "md:container mx-auto"}
                (d/div {:className "hero bg-base-200 min-h-screen"}
                       (d/div {:className "hero-content flex-col lg:flex-row-reverse"}
                              (d/img {:src "https://media.istockphoto.com/id/171306436/pt/foto/vermelho-tijolo-edif%C3%ADcio-de-escola-secund%C3%A1ria-exterior.jpg?s=612x612&w=0&k=20&c=W-yKSXGKUSg0oF1Grd2E5HaqzN1WAWzjtKSE9oMAZ34="
                                      :className "max-w-sm rounded-lg shadow-2xl"})
                              (d/div
                               (d/h1 {:className "text-5xl font-bold"}
                                     "School module!")
                               (d/p {:className "py-6"}
                                    "Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem quasi. In deleniti eaque aut repudiandae et a id nisi.")
                               (d/button {:className "btn btn-primary"}
                                         "Get Started")))))

         ($ footer {})))
