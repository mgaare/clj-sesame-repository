(defproject clj-sesame-repository "0.1.1"
  :description "Manage Sesame repositories"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.openrdf.sesame/sesame-runtime "2.6.10"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.7"]]
                   :source-paths ["dev"]}})
