(ns gr.handler.auth
  (:require
   [ataraxy.response :as response]
   [buddy.hashers :as hashers]
   [integrant.core :as ig]
   [gr.boundary.users :refer [find-user-by-login]]
   [gr.view.page :refer [login-page]]
   [ring.util.response :refer [redirect]]
   [taoensso.timbre :as timbre]))

(defmethod ig/init-key :gr.handler.auth/login [_ _]
  (fn [_]
    (login-page)))

(defn auth? [login password]
  (let [user (find-user-by-login login)]
    (timbre/debug "user" user)
    (and (some? user) (hashers/check password (:password user)))))

(defmethod ig/init-key :gr.handler.auth/login-post [_ _]
  (fn [{[_ {:strs [login password]}] :ataraxy/result}]
    (timbre/debug "login" login "password" password)
    (if (and (seq login) (auth? login password))
      (-> (redirect "/groups")
          (assoc-in [:session :identity] (keyword login)))
      [::response/found "/login"])))

(defmethod ig/init-key :gr.handler.auth/logout [_ _]
  (fn [_]
    (-> (redirect "/login")
        (assoc :session {}))))
