(ns clj-sesame-repository.core
  (:require [clojure.java.io :as io])
  (:import org.openrdf.repository.Repository
           org.openrdf.repository.sail.SailRepository
           org.openrdf.sail.nativerdf.NativeStore
           org.openrdf.sail.memory.MemoryStore
           org.openrdf.sail.inferencer.fc.ForwardChainingRDFSInferencer))

(defn- kwargs-or-map->map
  "Shamelessly stolen from immutant.internal.util... although they probably
   stole it from someone else and then went fishing."
  [vals]
  (if (= 1 (count vals))
    (let [m (first vals)]
      (if (map? m) m (apply hash-map m)))
    (apply hash-map vals)))

(defmacro doto-pred
  "If predicate p is true, then applies doto on x with the supplied forms,
   otherwise evalutes and returns x."
  [p x & forms]
  `(if ~p
     (doto ~x ~@forms)
     ~x))

(defn repository
  "Creates SailRepository from a given Store."
  [s]
  (SailRepository. s))

(defn set-indexes
  [store indexes]
  (.setTripleIndexes store indexes))

(defn make-native
  "Creates a Native (on-disk) Sesame repository.

   data-dir is something Fileable pointing to the directory for the repository
   files.

   opts is an optional map or kwargs (if you're into that) that accepts:

     * :inferencer?  If truthy, the repository will include the (useful?)
                     ForwardChainingRDFSInferencer.
     * :indexes      String of comma-or-space delimited index configs, each of
                     which is some permutation of the characters 's' 'p' 'o' 'c'.
                     Defaults to 'spoc,posc'. Indexes can speed up querying, and
                     slow down writing, obviously. According to an unconfirmed
                     report, they can also sap and impurify your precious bodily
                     fluids."
  ([data-dir]
     (make-native data-dir {}))
  ([data-dir & opts]
     (let [opts (kwargs-or-map->map opts)
           store (cond-> (doto-pred (:indexes opts)
                                    (NativeStore. (io/as-file data-dir))
                                    (set-indexes (:indexes opts)))
                         (:inferencer? opts)
                         (ForwardChainingRDFSInferencer.))]
       (repository store))))

(defn make-memory
  "Creates an in-memory Sesame repository.

  opts is an optional map or kwargs (if that butters your toast) that accepts:

    * :inferencer?  If truthy, the repository will include the (useful?)
                    ForwardChainingRDFSInferencer.
    * :data-dir     If supplied a Fileable directory, creates a Persistent
                    repository that will load up files in this directory on
                    initialization, and persists to this directory."
  [& opts]
  (let [opts (kwargs-or-map->map opts)
        store (cond-> (if-let [data-dir (:data-dir opts)]
                        (MemoryStore. (io/as-file data-dir))
                        (MemoryStore.))
                      (:inferencer? opts)
                      (ForwardChainingRDFSInferencer.))]
    (repository store)))

(defn initialized?
  "Predicate to test repositories for initialization."
  [repository]
  (.isInitialized repository))

(defn initialize!
  "Idempotently initializes repository."
  [repository]
  (doto-pred (not (initialized? repository))
             repository .initialize))

(defn shut-down!
  "Idempotently shuts down repository."
  [repository]
  (doto-pred (initialized? repository)
             repository .shutDown))

(def close
  "Convenience alias for shut-down."
  shut-down!)

(defn connection
  "Gets a connection from the repository. This should probably be used in
   with-open, since the resulting connection is a resource that needs to be
   closed."
  [repository]
  (.getConnection repository))
