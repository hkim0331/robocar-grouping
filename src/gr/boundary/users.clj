(ns gr.boundary.users
  (:require
   [environ.core :refer [env]]
   [next.jdbc :refer [get-connection]]
   [next.jdbc.result-set :as rs]
   [next.jdbc.sql :refer [query update!]]
   [taoensso.timbre :refer [debug]]))

(def db {:dbtype   "postgresql"
         :host     (env :r99c-host)
         :dbname   (env :r99c-db)
         :port     (env :r99c-port)
         :user     (env :r99c-user)
         :password (env :r99c-password)})

(def ds (get-connection db))

(defn find-user-by-login [login]
  (let [ret (query
             ds
             ["select * from users where login=?" login]
             {:builder-fn rs/as-unqualified-lower-maps})]
    (-> ret first)))

(defn update-gid [user gid]
  (update! ds :users {:gid gid} ["login=?" user]))
