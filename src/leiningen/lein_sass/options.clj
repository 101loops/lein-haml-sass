(ns leiningen.lein-sass.options)

(def ^:private default-options {:src "resources"
                                :output-extension "css"
                                :delete-output-dir true
                                :auto-compile-delay 250
                                })

(defn- normalize-hooks [options]
  (let [hooks (into #{} (:ignore-hooks options))
        normalized-hooks (if (:compile hooks)
                           (disj (conj hooks :once) :compile)
                           hooks)]
    (assoc options :ignore-hooks normalized-hooks)))

(defn- default-gem [src-type]
  (case src-type
    :sass {:gem-name "sass" :gem-version "3.3.0.rc.2"}
    :scss {:gem-name "sass" :gem-version "3.3.0.rc.2"}))

(defn- compression-style [src-type]
  (when (or (= src-type :sass) (= src-type :scss))
    {:style :nested}))

(defn- normalize-options [src-type options]
  (->> (src-type options)
    normalize-hooks
    (merge (default-gem src-type))
    (merge default-options)
    (merge (compression-style src-type))
    (merge {:src-type src-type})))

(defn extract-options [src-type project]
  (if (src-type project)
    (normalize-options src-type project)
    (println "WARNING: no" src-type "entry found in project definition.")))
