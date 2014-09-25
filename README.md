# clj-sesame-repository

A clojure library to create and manage [Sesame] repositories.

Also, a boring name.

## Overview

This is a fairly thin wrapper around the Sesame Java API.

The two entry functions of interest are `make-native`, which creates a Native (which is Sesame's oddball terminology for "on-disk") Sail repository, and `make-memory`, which creates an in-memory Sail repository.

Once you have a repository, you can `initialize!` it, `shut-down!` the respository, and get a `connection`.

Originally this library was created to make it easier to work with [Sesame] repositories in the quite good [kr] library.

## Usage

[Leiningen] dep:

```clj
[clj-sesame-repository "0.1.1"]
```

In a REPL:

```clojure-repl
user=> (require '[clj-sesame.repository.core :as sesame])
user=> (def repo (sesame/make-memory))
user=> (sesame/initialize! repo)
```

Then you can pass the repo on to [kr]'s `kb/kb`, or whatever else you might want to do with an RDF repository.

## Slightly More Advanced Usage

`make-native` takes something Fileable that points to its storage directory, and a few options as an optional map or kwargs (if you're into that) that accepts:

* :inferencer?  If truthy, the repository will include the (useful?) ForwardChainingRDFSInferencer.
* :indexes      String of comma-or-space delimited index configs, each of which is some permutation of the characters 's' 'p' 'o' 'c'. Defaults to 'spoc,posc'. Indexes can speed up querying, and slow down writing, obviously. According to an unconfirmed report, they can also sap and impurify your precious bodily fluids.

`make-memory` takes a couple of options as an optional map or kwargs (if that butters your toast) that accepts:

* :inferencer?  If truthy, the repository will include the (useful?) ForwardChainingRDFSInferencer.
* :data-dir     If supplied a Fileable directory, creates a Persistent repository that will load up files in this directory on initialization, and persists to this directory.

## License

Copyright © 2014 Michael Gaare and Mphasis Corp.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


----

[Sesame]: http://www.openrdf.org/
[kr]: https://github.com/drlivingston/kr
[Leiningen]: http://leiningen.org/
