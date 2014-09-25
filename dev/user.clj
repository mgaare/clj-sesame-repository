(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [clj-sesame-repository.core :refer :all])
  (:import org.openrdf.repository.Repository
           org.openrdf.repository.sail.SailRepository
           org.openrdf.sail.nativerdf.NativeStore
           org.openrdf.sail.memory.MemoryStore
           org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer))
