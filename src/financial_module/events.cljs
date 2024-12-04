(ns financial-module.events
  (:require [financial-module.db :refer [default-db session->local-store]]
            [refx.alpha :as refx]
            [refx.interceptors :refer [path after]]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]))

(def ->local-store (after session->local-store))

(def session-interceptors [(path :session)
                           ->local-store])

(refx/reg-fx
 :push-state
 (fn [route]
   (apply rfe/push-state route)))

(refx/reg-event-fx
 :initialize-db

 [(refx/inject-cofx :local-store-session)]

 (fn [{:keys [_ local-store-session]} _]
   {:db (assoc default-db :session local-store-session)}))

(refx/reg-event-fx
 :push-state
 (fn [_ [_ & route]]
   {:push-state route}))

(refx/reg-event-db
 :navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (rfc/apply-controllers (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))

(refx/reg-event-db
 :set-session

 session-interceptors

 (fn [db [_ session]]
   (let [session (merge (:session db) session)]
     (assoc db :session session))))

(refx/reg-event-db
 :set-content
 (fn [db [_ content]]
   (assoc db :content content)))

(refx/reg-event-db
 :set-content-list
 (fn [db [_ content-list]]
   (assoc db :content-list content-list)))

(refx/reg-event-db
 :set-accounts-payable
 (fn [db [_ accounts-payable]]
   (assoc db :accounts-payable accounts-payable)))

(refx/reg-event-db
 :set-courses
 (fn [db [_ courses]]
   (assoc db :courses courses)))

(refx/reg-event-db
 :set-students
 (fn [db [_ students]]
   (assoc db :students students)))

(refx/reg-event-db
 :set-subjects
 (fn [db [_ subjects]]
   (assoc db :subjects subjects)))

(refx/reg-event-db
 :set-attending
 (fn [db [_ attending]]
   (assoc db :attending attending)))
