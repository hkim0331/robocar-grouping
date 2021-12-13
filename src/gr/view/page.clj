(ns gr.view.page
  (:require
   [ataraxy.response :as response]
   #_[clojure.string :as str]
   [hiccup.page :refer [html5]]
   [hiccup.form :refer [form-to text-field password-field submit-button
                        label text-area file-upload hidden-field
                        radio-button]]
   #_[hiccup.util :refer [escape-html]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   #_[taoensso.timbre :as timbre :refer [debug]]))

(def version "0.4.0")

(defn page [& contents]
  [::response/ok
   (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
    [:link
     {:rel "stylesheet"
      :href "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
      :integrity "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
      :crossorigin "anonymous"}]
    [:link
     {:rel "stylesheet"
      :type "text/css"
      :href "/css/styles.css"}]
    [:script {:type "text/javascript"}
     "function ok() {return window.confirm('OK?');}"]
    [:title "gr"]
    [:body
     [:div {:class "container"}
      [:p {:class "develop"} "開発中。"
       "ユーザ名をテキトーに R99 から拾って作ったり消したりテストしてます。"
       "明日の授業開始前にクリアします。"]
      [:p {:class "develop"} "この赤メッセージ消えたら本番です。"]
      contents
      [:p]
      [:p [:a {:href "/logout" :class "btn btn-warning btn-sm"} "logout"]]
      [:hr]
      "hkimura, " version "."]])])

(defn error [msg]
 (page
  [:h2 {:class "error"} "gr: ERROR"]
  [:p msg]))

(defn login-page []
  (page
   [:h2 "gr: Login"]
   [:p "r99.melt と同じやつで。"]
   (form-to
    [:post "/login"]
    (anti-forgery-field)
    (text-field {:placeholder "ユーザ名"} "login")
    (password-field {:placeholder "パスワード"} "password")
    (submit-button "login"))))

(defn new-group []
  (page
   [:h2 "gr: New"]
   [:p "１グループは 3 人。複数のグループにはもちろん所属できない。"
       "グループ代表者ひとりがメンバのユーザ名
        （大文字・小文字・全角文字を正確に、区切りは半角スペース）を正確に入力後、"
       "create を押してください。追加、削除、修正はめんどくさいので、変更のないよう。"]
   (form-to
    [:post "/group"]
    (anti-forgery-field)
    [:p "class: " (radio-button "uhour" false "tue1") "tue1 "
        (radio-button "uhour" false "tue2") "tue2 "
        (radio-button "uhour" false "thr1") "thr1 "
        (radio-button "uhour" false "thr2") "thr2 "]
    (text-field {:placeholder "ユーザ名を半角スペースで区切って3人分。" :id "group"}
                "users")
    (submit-button {:class "btn btn-primary btn-sm"} "create"))))

(defn list-groups [groups admin?]
 (page
  [:h2 "gr: Groups"]
  (for [g groups]
    [:p
     (:id g) "&nbsp;"
     "(" (:uhour g) ") &nbsp;"
     (:members g)
     "&nbsp;"
     (when admin?
       [:a {:href (str "/delete/" (:id g))} "del"])])
  [:p [:a {:href "/group" :class "btn btn-primary btn-sm"}
       "new group"]]))