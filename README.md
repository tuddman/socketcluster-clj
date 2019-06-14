# socketcluster-clj

A [SocketCluster](https://socketcluster.io/) Client library for Clojure. This client wraps [socketcluster-client-java](https://github.com/sacOO7/socketcluster-client-java), but where appropriate aims to be more idiomatic and succinct.

## Usage

```clojure
lein repl

=> (require '[socketcluster-clj.core :as sc])
=> (def sc-server-url "ws://localhost:8000/socketcluster/")
=> (def my-socket (sc/new-socket sc-server-url)
=> (sc/set-listener my-socket)
=> (sc/connect my-socket)
=> (sc/emit my-socket {"some" "data"})
```

have a look in `src/socketcluster_clj/core.clj` for every function you can call.


### NPE Issue

if you get the following error when trying to connect:

```bash
Exception in thread "OkHttp Dispatcher" java.lang.NullPointerException
	at io.github.sac.Socket$1.onFailure(Socket.java:185)
	at okhttp3.internal.ws.RealWebSocket.failWebSocket(RealWebSocket.java:546)
	at okhttp3.internal.ws.RealWebSocket$2.onResponse(RealWebSocket.java:206)
```

you need to `(sc/set-listener <socket>)` **before** connecting.

## License

Copyright © 2019 tuddman

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
