(ns financial-module.infra.session
  (:require [financial-module.infra.http :as http]
            [refx.alpha :as refx]))

(defn update-session [{:keys [username password]}]
  (-> (http/request! {:path "login"
                      :method :post
                      :body {:username username
                             :password password}})
      (.then (fn [{:keys [body]}]
               (refx/dispatch [:set-session body])
               (.reload js/location)))
      (.catch #(prn "request to set session! catch: " %))))
