(ns gr.handler.core
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [gr.boundary.users :as users]
            [gr.boundary.groups :as groups]
            [gr.view.page :as page]
            [integrant.core :as ig]
            [taoensso.timbre :as timbre]))

(defn- user [req]
 (get-in req [:session :identity]))

(defmethod ig/init-key :gr.handler.core/groups [_ options]
  (fn [{[_] :ataraxy/result :as req}]
    (let [admin? (= :hkimura (user req))]
      (page/list-groups (groups/list-groups) admin?))))

(defmethod ig/init-key :gr.handler.core/new [_ options]
  (fn [{[_] :ataraxy/result}]
    (page/new-group)))

(defn- validate
  [uhour users user]
  (let [members (str/split users #"\s+")]
    (when (empty? uhour)
      (throw (Exception. (str "empty class"))))
    (when-not (str/includes? user users)
      (throw (Exception. "are you a member of the group?")))
    (when (< 3 (count members))
      (throw (Exception. (str "too many members"))))
    (doseq [u members]
      (when-not (users/find-user-by-login u)
        (throw (Exception. (str u " is not found"))))
      (when (groups/find-user u)
        (throw (Exception. (str u " already belong to other group")))))))

(defn- create-group [uhour users]
  (let [gid (groups/create uhour users)]
    (doseq [u (str/split users #"\s")]
      (users/update-gid u gid))))

(defmethod ig/init-key :gr.handler.core/create [_ options]
  (fn [{[_ {:strs [uhour users]}] :ataraxy/result :as req}]
    (timbre/debug "uhour" uhour)
    (try
      (validate uhour users (user req))
      (create-group uhour users)
      [::response/found "/"]
      (catch Exception e
        (page/error (str e))))))

(defmethod ig/init-key :gr.handler.core/group [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "group"]))

#_(defmethod ig/init-key :gr.handler.core/update [_ options]
    (fn [{[_] :ataraxy/result}]
      [::response/ok "update"]))

(defmethod ig/init-key :gr.handler.core/delete [_ options]
  (fn [{[_ n] :ataraxy/result}]
    (groups/delete n)
    (users/delete n)
    [::response/found "/"]))
