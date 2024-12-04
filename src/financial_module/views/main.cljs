(ns financial-module.views.main
  (:require [financial-module.sections.header :refer [header]]
            [financial-module.sections.footer :refer [footer]]
            [financial-module.infra.http :as http]
            [helix.core :refer [$ defnc]]
            [helix.dom :as d]
            [helix.hooks :as hh]
            [refx.alpha :as refx]))

(defnc main [{:keys [session]}]
  (let [accounts-payable (refx/use-sub [:accounts-payable])]

    (d/div {:class "mx-auto"}
           ($ header {})
           (d/div {:className "md:container mx-auto"}
                  (d/div
                   "Main page"))

           ($ footer {}))))
