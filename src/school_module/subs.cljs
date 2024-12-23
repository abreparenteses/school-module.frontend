(ns school-module.subs
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

(refx/reg-sub
 :courses
 (fn [db]
   (:courses db)))

(refx/reg-sub
 :students
 (fn [db]
   (:students db)))

(refx/reg-sub
 :subjects
 (fn [db]
   (:subjects db)))

(refx/reg-sub
 :attending
 (fn [db]
   (:attending db)))
