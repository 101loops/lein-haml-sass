(ns leiningen.integration-spec
  (:use [speclj.core]
        [clojure.java.shell :only [sh]])
  (:require [leiningen.utils :as futils]
            [clojure.java.io :as io]))

(describe "integration tests on tasks"
  ;; This is not ideal but provides some way of testing the tasks (given
  ;; that I have figured out how to include leiningen dependencoes in
  ;; the tests) especially: we are relying on the project.clj file
  ;; (which can't be changed from here)
  
  (before (with-out-str (futils/delete-directory-recursively! "spec/out")))

  (defn sass [sub-task] (sh "lein" "with-profile" "plugin-example" "sass" sub-task))

  (context "once"
    (it "compiles the files in the correct directory"
      (sass "once")

      (let [out-files (file-seq (io/file "spec/out"))]
        (should= 3 (count out-files)))

      (let [file-content (slurp "spec/out/foo.css")
            expected-content ".wide {\n  width: 100%; }\n\n.foo {\n  display: block; }\n"]
        (should= expected-content file-content))

      (let [file-content (slurp "spec/out/bar.css")
            expected-content ".bar {\n  display: none; }\n"]
        (should= expected-content file-content))))

  (context "auto")

  (context "clean"
    (it "removes all artifacts that were created by sass task"
      (sass "once")
      (should (.exists (io/file "spec/out")))

      (sass "clean")
      (should-not (.exists (io/file "spec/out"))))

    (it "only deletes the artifacts that were created by sass task"
      (sass "once")
      (should (.exists (io/file "spec/out")))

      (spit "spec/out/not-generated" "a non generated content")

      (sass "clean")
      (should (.exists (io/file "spec/out/not-generated")))
      (should-not (.exists (io/file "spec/out/bar.css")))
      (should-not (.exists (io/file "spec/out/foo.css"))))))