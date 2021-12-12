(ns gr.boundary.groups
  (:require
   [environ.core :refer [env]]
   [next.jdbc :refer [get-connection]]
   [next.jdbc.sql :as sql]
   [next.jdbc.result-set :as rs]
   [taoensso.timbre :refer [debug]]))

(def db {:dbtype   "postgresql"
         :host     (env :r99c-host)
         :dbname   (env :r99c-db)
         :port     (env :r99c-port)
         :user     (env :r99c-user)
         :password (env :r99c-password)})

(def ds (get-connection db))

(defn find-user [user]
  (let [ret (sql/query ds ["select * from groups where members like ?"
                            (str "%" user "%")])]
    (seq ret)))

(defn create [users]
  (let [ret (sql/insert! ds :groups {:members users}
                         {:builder-fn rs/as-unqualified-lower-maps})]
    (:id ret)))