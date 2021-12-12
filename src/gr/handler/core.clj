(ns gr.handler.core
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defmethod ig/init-key :gr.handler.core/groups [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "groups"]))

(defmethod ig/init-key :gr.handler.core/new [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "new"]))

(defmethod ig/init-key :gr.handler.core/create [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "create"]))

(defmethod ig/init-key :gr.handler.core/group [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "group"]))

(defmethod ig/init-key :gr.handler.core/update [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "update"]))

(defmethod ig/init-key :gr.handler.core/delete [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "delete"]))