(ns financial-module.infra.http
  (:require [financial-module.infra.http.component :as http.component]
            [financial-module.config :as config]))

(defn request! [request-input]
  (http.component/request
   (http.component/new-http {:base-url config/api-base-url})
   request-input))

(comment
  (-> (http.component/request
       (http.component/new-http {:base-url "http://localhost:3001/"})
       {:path "login"
        :method :post
        :body {:username "jacare"
               :password "dunha"}})
      (.then #(prn "then" (:body %)))
      (.catch  #(prn "catch" %)))

  (-> (request! {:path "login"
                 :method :post
                 :body {:username "jacare"
                        :password "dunha"}})
      (.then #(prn "request! then: " (:body %)))
      (.catch #(prn "request! catch: " %))))
