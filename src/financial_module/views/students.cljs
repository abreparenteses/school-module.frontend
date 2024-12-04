(ns financial-module.views.students
  (:require [financial-module.sections.header :refer [header]]
            [financial-module.sections.footer :refer [footer]]
            [financial-module.infra.http :as http]
            [helix.core :refer [$ defnc]]
            [helix.dom :as d]
            [helix.hooks :as hh]
            [refx.alpha :as refx]))

(defn get-entries [session]
  (-> (http/request! {:path "students"
                      :method :get
                      :headers {"authorization" (:token session)}})
      (.then (fn [e]
               (refx/dispatch [:set-students (:body e)])))
      (.catch #(prn "request to get entries! catch: " %))))

(defn create-entry [name email document phone session]
  (-> (http/request! {:path "students"
                      :method :post
                      :headers {"authorization" (:token session)}
                      :body {:name name
                             :email email
                             :document document
                             :phone phone}})
      (.then #(get-entries session))
      (.catch #(prn "request to create entry! catch: " %))))

(defn update-entry [id name email document phone session]
  (-> (http/request! {:path (str "students/" id)
                      :method :put
                      :headers {"authorization" (:token session)}
                      :body {:name name
                             :email email
                             :document document
                             :phone phone}})
      (.then #(get-entries session))
      (.catch #(prn "request to update entry! catch: " %))))

(defn delete-entry [id session]
  (-> (http/request! {:path (str "students/" id)
                      :method :delete
                      :headers {"authorization" (:token session)}})
      (.then #(get-entries session))
      (.catch #(prn "request to delete entry! catch: " %))))

(defnc main [{:keys [session]}]
  (let [students (refx/use-sub [:students])
        [name set-name!] (hh/use-state "")
        [email set-email!] (hh/use-state "")
        [document set-document!] (hh/use-state "")
        [phone set-phone!] (hh/use-state "")
        [update-id set-update-id!] (hh/use-state "")
        [update-name set-update-name!] (hh/use-state "")
        [update-email set-update-email!] (hh/use-state "")
        [update-document set-update-document!] (hh/use-state "")
        [update-phone set-update-phone!] (hh/use-state "")
        _ (get-entries session)]

    (d/div {:class "mx-auto"}
           ($ header {})
           (d/div {:className "md:container mx-auto"}
                  (d/div
                   (d/form {:className "flex flex-col items-center justify-center"
                            :onSubmit (fn [e]
                                        (.preventDefault e)
                                        (create-entry name email document phone session))}
                           (d/h1 {:className "font-bold text-4xl my-4"}
                                 "Students")
                           (d/input {:name "name"
                                     :type "text"
                                     :placeholder "Name"
                                     :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                     :on-change #(set-name! (.. % -target -value))})
                           (d/input {:name "email"
                                     :type "email"
                                     :placeholder "Email"
                                     :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                     :on-change #(set-email! (.. % -target -value))})
                           (d/input {:name "document"
                                     :type "text"
                                     :placeholder "Document"
                                     :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                     :on-change #(set-document! (.. % -target -value))})
                           (d/input {:name "phone"
                                     :type "text"
                                     :placeholder "Phone"
                                     :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                     :on-change #(set-phone! (.. % -target -value))})
                           (d/button {:className "btn btn-primary m-2 w-full max-w-xs"}
                                     "Create Entry")))

                  (d/div {:className "overflow-x-auto mt-4"}
                         (d/table {:className "table table-pin-cols"}
                                  (d/thead
                                   (d/tr
                                    (d/th "Id")
                                    (d/th "Name")
                                    (d/th "Email")
                                    (d/th "Document")
                                    (d/th "Phone")
                                    (d/th "Created At")
                                    (d/th "Actions")))
                                  (d/tbody
                                   (for [entry (:entries students)]
                                     (d/tr {:key (str (:id entry))}
                                           (d/td (str (:id entry)))
                                           (d/td (str (:name entry)))
                                           (d/td (str (:email entry)))
                                           (d/td (str (:document entry)))
                                           (d/td (str (:phone entry)))
                                           (d/td (str (.toLocaleDateString (new js/Date (:created-at entry)) "pt-BR")))
                                           (d/td (d/div
                                                  (d/button {:className "btn btn-primary mx-2"
                                                             :onClick (fn []
                                                                        (set-update-id! (str (:id entry)))
                                                                        (set-update-name! (str (:name entry)))
                                                                        (set-update-email! (str (:email entry)))
                                                                        (set-update-document! (str (:document entry)))
                                                                        (set-update-phone! (str (:phone entry)))
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
                                                 (update-entry update-id update-name update-email update-document update-phone session)
                                                 (.close (js/document.getElementById "update-modal")))}
                                    (d/h3 {:className "font-bold text-lg my-2"}
                                          "Update Entry")
                                    (d/input {:name "name"
                                              :type "text"
                                              :placeholder "Name"
                                              :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                              :value update-name
                                              :on-change #(set-update-name! (.. % -target -value))})
                                    (d/input {:name "email"
                                              :type "email"
                                              :placeholder "Email"
                                              :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                              :value update-email
                                              :on-change #(set-update-email! (.. % -target -value))})
                                    (d/input {:name "document"
                                              :type "document"
                                              :placeholder "Document"
                                              :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                              :value update-document
                                              :on-change #(set-update-document! (.. % -target -value))})
                                    (d/input {:name "phone"
                                              :type "phone"
                                              :placeholder "Phone"
                                              :className "input input-bordered input-primary m-2 w-full max-w-xs"
                                              :value update-phone
                                              :on-change #(set-update-phone! (.. % -target -value))})
                                    (d/button {:className "btn btn-primary m-2 w-full max-w-xs"}
                                              "Update Entry"))
                            (d/form {:method "dialog"
                                     :className "modal-backdrop"}
                                    (d/button "close"))))

           ($ footer {}))))
