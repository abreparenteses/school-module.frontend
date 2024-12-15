(ns school-module.views.subjects
  (:require [school-module.sections.header :refer [header]]
            [school-module.sections.footer :refer [footer]]
            [school-module.infra.http :as http]
            [helix.core :refer [$ defnc]]
            [helix.dom :as d]
            [helix.hooks :as hh]
            [refx.alpha :as refx]))

(defn- get-entries [session]
  (-> (http/request! {:path "subjects"
                      :method :get
                      :headers {"authorization" (:token session)}})
      (.then (fn [e]
               (refx/dispatch [:set-subjects (:body e)])))
      (.catch #(prn "request to get entries! catch: " %))))

(defn- get-courses [session]
  (-> (http/request! {:path "courses"
                      :method :get
                      :headers {"authorization" (:token session)}})
      (.then (fn [e]
               (refx/dispatch [:set-courses (:body e)])))
      (.catch #(prn "request to get courses list! catch: " %))))

(defn- create-entry [name description courses-id session]
  (-> (http/request! {:path "subjects"
                      :method :post
                      :headers {"authorization" (:token session)}
                      :body {:name name
                             :description description
                             :courses-id (parse-uuid courses-id)}})
      (.then #(get-entries session))
      (.catch #(prn "request to create entry! catch: " %))))

(defn- update-entry [id name description courses-id session]
  (-> (http/request! {:path (str "subjects/" id)
                      :method :put
                      :headers {"authorization" (:token session)}
                      :body {:name name
                             :description description
                             :courses-id (parse-uuid courses-id)}})
      (.then #(get-entries session))
      (.catch #(prn "request to update entry! catch: " %))))

(defn- delete-entry [id session]
  (-> (http/request! {:path (str "subjects/" id)
                      :method :delete
                      :headers {"authorization" (:token session)}})
      (.then #(get-entries session))
      (.catch #(prn "request to delete entry! catch: " %))))

(defn- find-course-by-id [id courses]
  (first (filter #(= id (:id %)) (:entries courses))))

(defnc main [{:keys [session]}]
  (let [subjects (refx/use-sub [:subjects])
        courses (refx/use-sub [:courses])
        [name set-name!] (hh/use-state "")
        [description set-description!] (hh/use-state "")
        [courses-id set-courses-id!] (hh/use-state "")
        [update-id set-update-id!] (hh/use-state "")
        [update-name set-update-name!] (hh/use-state "")
        [update-description set-update-description!] (hh/use-state "")
        [update-courses-id set-update-courses-id!] (hh/use-state "")
        ;; [update-courses set-update-courses!] (hh/use-state [])
        _ (get-entries session)
        _ (get-courses session)]

    (d/div {:class "mx-auto"}
           ($ header {})
           (d/div {:className "md:container mx-auto"}
                  (d/div
                   (d/form {:className "flex flex-col items-center justify-center"
                            :onSubmit (fn [e]
                                        (.preventDefault e)
                                        (create-entry name description courses-id session))}
                           (d/h1 {:className "font-bold text-4xl my-4"}
                                 "Subjects")
                           (d/input {:name "name"
                                     :type "text"
                                     :placeholder "Name"
                                     :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                     :on-change #(set-name! (.. % -target -value))})
                           (d/input {:name "description"
                                     :type "text"
                                     :placeholder "Description"
                                     :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                     :on-change #(set-description! (.. % -target -value))})
                           (d/select
                            {:name "courses-id"
                             :className "select select-primary m-2 w-full max-w-xs"
                             :on-change #(set-courses-id! (.. % -target -value))}

                            (for [course (:entries courses)]
                              (d/option {:key (:id course)
                                         :value (:id course)}
                                        ;; validate if courses-id is empty
                                        ;; to select the first one
                                        (when (empty? courses-id)
                                          (set-courses-id! (str (:id (first (:entries courses))))))
                                        (:name course))))

                           (d/button {:className "btn btn-primary m-2 w-full max-w-xs"}
                                     "Create Entry")))

                  (d/div {:className "overflow-x-auto mt-4"}
                         (d/table {:className "table table-pin-cols"}
                                  (d/thead
                                   (d/tr
                                    (d/th "Id")
                                    (d/th "Name")
                                    (d/th "Description")
                                    (d/th "Courses")
                                    (d/th "Created At")
                                    (d/th "Actions")))
                                  (d/tbody
                                   (for [entry (:entries subjects)]
                                     (d/tr {:key (str (:id entry))}
                                           (d/td (str (:id entry)))
                                           (d/td (str (:name entry)))
                                           (d/td (str (:description entry)))
                                           (d/td (str (:name (find-course-by-id (:courses-id entry) courses))))
                                           (d/td (str (.toLocaleDateString (new js/Date (:created-at entry)) "pt-BR")))
                                           (d/td (d/div
                                                  (d/button {:className "btn btn-primary mx-2"
                                                             :onClick (fn []
                                                                        (set-update-id! (str (:id entry)))
                                                                        (set-update-name! (str (:name entry)))
                                                                        (set-update-description! (str (:description entry)))
                                                                        (set-update-courses-id! (str (:courses-id entry)))
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
                                                 (update-entry update-id update-name update-description update-courses-id session)
                                                 (.close (js/document.getElementById "update-modal")))}
                                    (d/h3 {:className "font-bold text-lg my-2"}
                                          "Update Entry")
                                    (d/input {:name "name"
                                              :type "text"
                                              :placeholder "Name"
                                              :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                              :value update-name
                                              :on-change #(set-update-name! (.. % -target -value))})
                                    (d/input {:name "description"
                                              :type "text"
                                              :placeholder "Description"
                                              :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                              :value update-description
                                              :on-change #(set-update-description! (.. % -target -value))})
                                    (d/select {:name "courses-id"
                                               :className "select select-primary m-2 w-full max-w-xs"
                                               :on-change #(set-update-courses-id! (.. % -target -value))}
                                              (for [course (:entries courses)]
                                                (if (= update-courses-id (str (:id course)))
                                                  (d/option {:key (:id course)
                                                             :value (:id course)
                                                             :selected true}
                                                            (:name course))
                                                  (d/option {:key (:id course)
                                                             :value (:id course)}
                                                            (:name course)))))

                                    (d/button {:className "btn btn-primary m-2 w-full max-w-xs"}
                                              "Update Entry"))
                            (d/form {:method "dialog"
                                     :className "modal-backdrop"}
                                    (d/button "close"))))

           #_($ footer {}))))
