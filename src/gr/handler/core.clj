(ns gr.handler.core
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [gr.boundary.users :as users]
            [gr.boundary.groups :as groups]
            [gr.view.page :as page]
            [integrant.core :as ig]))

(defmethod ig/init-key :gr.handler.core/groups [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "groups"]))

(defmethod ig/init-key :gr.handler.core/new [_ options]
  (fn [{[_] :ataraxy/result}]
    (page/new-group)))

(defn- validate
  [users]
  (doseq [u (str/split users #"\s+")]
    (when-not (users/find-user-by-login u)
      (throw (Exception. (str u " is not found"))))
    (when (groups/find-user u)
      (throw (Exception. (str u " already belong other group"))))))

(defn- create-group [users]
  (let [gid (groups/create users)]
    (doseq [u (str/split users #"\s")]
      (users/update-gid u gid))))

(defmethod ig/init-key :gr.handler.core/create [_ options]
  (fn [{[_ {:strs [users]}] :ataraxy/result}]
    (try
      (validate users)
      (create-group users)
      [::response/ok "/groups"] ;; no hstore extension
      (catch Exception e
        (page/error (str e))))))

(defmethod ig/init-key :gr.handler.core/group [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "group"]))

(defmethod ig/init-key :gr.handler.core/update [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "update"]))

(defmethod ig/init-key :gr.handler.core/delete [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "delete"]))