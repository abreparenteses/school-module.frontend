(ns financial-module.config)

(goog-define DEBUG true)
(goog-define PROTOCOL "http")
(goog-define HOST "localhost")
(goog-define PORT "8300")
(def base-url
  (str PROTOCOL "://" HOST
       (when PORT (str ":" PORT))))
(def api-base-url
  "http://localhost:3001/")
(goog-define EXTERNAL-RESOURCES-BASE-URL "")
(def resources-base-url
  (str (if (empty? EXTERNAL-RESOURCES-BASE-URL)
         base-url
         EXTERNAL-RESOURCES-BASE-URL)
       "/resources"))

(def config
  {:debug DEBUG
   :protocol PROTOCOL
   :host HOST
   :port PORT
   :base-url api-base-url
   :resources-base-url resources-base-url})
