(ns school-module.views.attending
  (:require [school-module.sections.header :refer [header]]
            [school-module.sections.footer :refer [footer]]
            [school-module.infra.http :as http]
            [helix.core :refer [$ defnc]]
            [helix.dom :as d]
            [helix.hooks :as hh]
            [refx.alpha :as refx]))

(defn- get-entries [session]
  (-> (http/request! {:path "attending"
                      :method :get
                      :headers {"authorization" (:token session)}})
      (.then (fn [e]
               (refx/dispatch [:set-attending (:body e)])))
      (.catch #(prn "request to get entries! catch: " %))))

(defn- get-students [session]
  (-> (http/request! {:path "students"
                      :method :get
                      :headers {"authorization" (:token session)}})
      (.then (fn [e]
               (refx/dispatch [:set-students (:body e)])))
      (.catch #(prn "request to get students list! catch: " %))))

(defn- get-subjects [session]
  (-> (http/request! {:path "subjects"
                      :method :get
                      :headers {"authorization" (:token session)}})
      (.then (fn [e]
               (refx/dispatch [:set-subjects (:body e)])))
      (.catch #(prn "request to get subjects list! catch: " %))))

(defn- create-entry [students-id subjects-id session]
  (-> (http/request! {:path "attending"
                      :method :post
                      :headers {"authorization" (:token session)}
                      :body {:students-id (parse-uuid students-id)
                             :subjects-id (parse-uuid subjects-id)}})
      (.then #(get-entries session))
      (.catch #(prn "request to create entry! catch: " %))))

(defn- update-entry [id students-id subjects-id session]
  (-> (http/request! {:path (str "attending/" id)
                      :method :put
                      :headers {"authorization" (:token session)}
                      :body {:students-id (parse-uuid students-id)
                             :subjects-id (parse-uuid subjects-id)}})
      (.then #(get-entries session))
      (.catch #(prn "request to update entry! catch: " %))))

(defn- delete-entry [id session]
  (-> (http/request! {:path (str "attending/" id)
                      :method :delete
                      :headers {"authorization" (:token session)}})
      (.then #(get-entries session))
      (.catch #(prn "request to delete entry! catch: " %))))

(defn- find-entity-by-id [id entities]
  (first (filter #(= id (:id %)) (:entries entities))))

(defnc main [{:keys [session]}]
  (let [attending (refx/use-sub [:attending])
        students (refx/use-sub [:students])
        subjects (refx/use-sub [:subjects])
        [students-id set-students-id!] (hh/use-state "")
        [subjects-id set-subjects-id!] (hh/use-state "")
        [update-id set-update-id!] (hh/use-state "")
        [update-students-id set-update-students-id!] (hh/use-state "")
        [update-subjects-id set-update-subjects-id!] (hh/use-state "")
        _ (get-entries session)
        _ (get-students session)
        _ (get-subjects session)]

    (d/div {:class "mx-auto"}
           ($ header {})
           (d/div {:className "md:container mx-auto"}
                  (d/div
                   (d/form {:className "flex flex-col items-center justify-center"
                            :onSubmit (fn [e]
                                        (.preventDefault e)
                                        (create-entry students-id subjects-id session))}
                           (d/h1 {:className "font-bold text-4xl my-4"}
                                 "Attending")
                           (d/select
                            {:name "students-id"
                             :className "select select-primary m-2 w-full max-w-xs"
                             :on-change #(set-students-id! (.. % -target -value))}

                            (for [student (:entries students)]
                              (d/option {:key (:id student)
                                         :value (:id student)}
                                        ;; validate if courses-id is empty
                                        ;; to select the first one
                                        (when (empty? students-id)
                                          (set-students-id! (str (:id (first (:entries students))))))
                                        (:name student))))
                           (d/select
                            {:name "subjects-id"
                             :className "select select-primary m-2 w-full max-w-xs"
                             :on-change #(set-subjects-id! (.. % -target -value))}

                            (for [subject (:entries subjects)]
                              (d/option {:key (:id subject)
                                         :value (:id subject)}
                                        ;; validate if courses-id is empty
                                        ;; to select the first one
                                        (when (empty? subjects-id)
                                          (set-subjects-id! (str (:id (first (:entries subjects))))))
                                        (:name subject))))

                           (d/button {:className "btn btn-primary m-2 w-full max-w-xs"}
                                     "Create Entry")))

                  (d/div {:className "overflow-x-auto mt-4"}
                         (d/table {:className "table table-pin-cols"}
                                  (d/thead
                                   (d/tr
                                    (d/th "Id")
                                    (d/th "Student")
                                    (d/th "Subject")
                                    (d/th "Created At")
                                    (d/th "Actions")))
                                  (d/tbody
                                   (for [entry (:entries attending)]
                                     (d/tr {:key (str (:id entry))}
                                           (d/td (str (:id entry)))
                                           (d/td (str (:name (find-entity-by-id (:students-id entry) students))))
                                           (d/td (str (:name (find-entity-by-id (:subjects-id entry) subjects))))
                                           (d/td (str (.toLocaleDateString (new js/Date (:created-at entry)) "pt-BR")))
                                           (d/td (d/div
                                                  (d/button {:className "btn btn-primary mx-2"
                                                             :onClick (fn []
                                                                        (set-update-id! (str (:id entry)))
                                                                        (set-update-students-id! (str (:students-id entry)))
                                                                        (set-update-subjects-id! (str (:subjects-id entry)))
                                                                        (.showModal (js/document.getElementById "update-modal")))}
                                                            "Update")
                                                  (d/button {:className "btn btn-error mx-2"
                                                             :onClick #(delete-entry (str (:id entry)) session)}
                                                            "Delete")))))))))

           (d/dialog {:id "update-modal"
                      :className "modal"}
                     (d/div {:className "modal-box"}
                            (d/form {:className "flex flex-col items-center justify-center"
                                     :onSubmit (fn [e]
                                                 (.preventDefault e)
                                                 (update-entry update-id update-students-id update-subjects-id session)
                                                 (.close (js/document.getElementById "update-modal")))}
                                    (d/h3 {:className "font-bold text-lg my-2"}
                                          "Update Entry")
                                    (d/select {:name "students-id"
                                               :className "select select-primary m-2 w-full max-w-xs"
                                               :on-change #(set-update-students-id! (.. % -target -value))}
                                              (for [student (:entries students)]
                                                (if (= update-students-id (str (:id student)))
                                                  (d/option {:key (:id student)
                                                             :value (:id student)
                                                             :selected true}
                                                            (:name student))
                                                  (d/option {:key (:id student)
                                                             :value (:id student)}
                                                            (:name student)))))
                                    (d/select {:name "subjects-id"
                                               :className "select select-primary m-2 w-full max-w-xs"
                                               :on-change #(set-update-subjects-id! (.. % -target -value))}
                                              (for [subject (:entries subjects)]
                                                (if (= update-subjects-id (str (:id subject)))
                                                  (d/option {:key (:id subject)
                                                             :value (:id subject)
                                                             :selected true}
                                                            (:name subject))
                                                  (d/option {:key (:id subject)
                                                             :value (:id subject)}
                                                            (:name subject)))))

                                    (d/button {:className "btn btn-primary m-2 w-full max-w-xs"}
                                              "Update Entry"))
                            (d/form {:method "dialog"
                                     :className "modal-backdrop"}
                                    (d/button "close"))))

           #_($ footer {}))))
