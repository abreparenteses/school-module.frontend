(ns financial-module.subs
  (:require [refx.alpha :as refx]))

(refx/reg-sub
 :db
 (fn [db _] db))

(refx/reg-sub
 :session
 (fn [db _]
   (:session db)))

(refx/reg-sub
 :current-route
 (fn [db]
   (:current-route db)))

(refx/reg-sub
 :content
 (fn [db]
   (:content db)))

(refx/reg-sub
 :content-list
 (fn [db]
   (:content-list db)))

(refx/reg-sub
 :accounts-payable
 (fn [db]
   (:accounts-payable db)))
