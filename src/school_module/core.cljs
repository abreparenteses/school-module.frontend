(ns school-module.core
  (:require ["react-dom/client" :as react-dom-client]
            [helix.core :refer [$ defnc]]
            [refx.alpha :as refx]
            [reitit.coercion.spec :as rss]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [school-module.events]
            [school-module.subs]
            [school-module.routes :as routes]))

(defn on-navigate [new-match]
  (when new-match
    (refx/dispatch [:navigated new-match])))

(def router
  (rf/router
   routes/routes
   {:data {:coercion rss/coercion}}))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment true}))

(defnc router-component [{:keys [_]}]
  (let [current-route (refx/use-sub [:current-route])]
    (when current-route
      (-> current-route :data :view $))))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:export init []
  (refx/clear-subscription-cache!)
  (refx/dispatch-sync [:initialize-db])
  ;; (dev-setup)
  (init-routes!)
  (doto (react-dom-client/createRoot (js/document.getElementById "app"))
    (.render ($ router-component {:router router}))))
