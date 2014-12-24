(ns leiningen.sass
  (:use [leiningen.lein-common.lein-utils :only [lein2?]])
  (:require [leiningen.tasks :as tasks]
            [leiningen.help :as lhelp]
            [leiningen.clean :as lclean]
            [leiningen.compile :as lcompile]
            [robert.hooke :as hooke]))

(tasks/def-lein-task sass)

(defn activate
  "Set up hooks for the plugin.  Eventually, this can be changed to just hooks,
   and people won't have to specify :hooks in their project.clj files anymore."
  []
  (hooke/add-hook #'lcompile/compile (tasks/standard-hook :sass :once))
  (hooke/add-hook #'lclean/clean (tasks/standard-hook :sass :clean)))
