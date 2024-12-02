(ns financial-module.views.login
  (:require
   [financial-module.infra.session :as session]
   [helix.core :refer [defnc]]
   [helix.dom :as d]))

(defnc login [{:keys [_]}]
  (d/div {:class "flex h-screen"}
         (d/div {:className "m-auto md:container md:mx-auto py-10 flex items-center justify-center"}
                (d/div {:className "w-1/4 bg-neutral rounded-lg py-10 align-center"}
                       (d/div {:class "md:flex md:flex-col md:items-center md:gap-4"}
                              (d/h1 {:className "text-2xl font-bold text-center"}
                                    "Login")
                              (d/label {:className "input input-bordered flex items-center gap-2"}
                                       (d/svg {:xmlns "http://www.w3.org/2000/svg"
                                               :viewBox "0 0 16 16"
                                               :fill "currentColor"
                                               :className "h-4 w-4 opacity-70"}
                                              (d/path {:d "M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6ZM12.735 14c.618 0 1.093-.561.872-1.139a6.002 6.002 0 0 0-11.215 0c-.22.578.254 1.139.872 1.139h9.47Z"}))
                                       (d/input {:name "username"
                                                 :type "text"
                                                 :className "grow"
                                                 :placeholder "Username"}))

                              (d/label {:className "input input-bordered flex items-center gap-2"}
                                       (d/svg {:xmlns "http://www.w3.org/2000/svg"
                                               :viewBox "0 0 16 16"
                                               :fill "currentColor"
                                               :className "h-4 w-4 opacity-70"}
                                              (d/path {:fillRule "evenodd"
                                                       :d "M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
                                                       :clipRule "evenodd"}))
                                       (d/input {:name "password"
                                                 :type "password"
                                                 :className "grow"
                                                 :placeholder "Password"}))

                              (d/button {:className "btn btn-primary flex items-center gap-2"
                                         :onClick #(session/update-session {:username (.-value (js/document.querySelector "input[name=username]"))
                                                                            :password (.-value (js/document.querySelector "input[name=password]"))})}
                                        "Login"))))))
