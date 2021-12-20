(ns gr.handler.example
  (:require #_[ataraxy.core :as ataraxy]
            [ataraxy.response :as response] 
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defmethod ig/init-key :gr.handler/example [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok (io/resource "gr/handler/example/example.html")]))
