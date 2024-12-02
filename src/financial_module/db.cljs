(ns financial-module.db
  (:require [cljs.reader]
            [refx.alpha :as refx]))

(def default-db
  {:session {}
   :showing :all})

(def ls-key "session")

(defn session->local-store
  "Puts session into localStorage"
  [session]
  (.setItem js/localStorage ls-key (:session session)))

(refx/reg-cofx
 :local-store-session
 (fn [cofx _]
   (assoc cofx :local-store-session
          (into (sorted-map)
                (some->> (.getItem js/localStorage ls-key)
                         (cljs.reader/read-string))))))
