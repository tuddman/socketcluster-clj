(defproject socketcluster-clj "0.1.0-SNAPSHOT"
  :description "socketcluster-clj : a SocketCluster Client library for Clojure"
  :url "https://socketcluster.io"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
   :repositories [["bintray" {:url "https://dl.bintray.com/sacoo7/Maven"
                              :snapshots true}]]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.github.sac/SocketclusterClientJava "2.0.0-beta"] ;; sacoo7 on Github, bintray
                 [com.taoensso/timbre "4.10.0"]]
  :repl-options {:init-ns socketcluster-clj.client})
