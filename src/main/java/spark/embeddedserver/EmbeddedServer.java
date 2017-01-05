/*
 * Copyright 2016 - Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spark.embeddedserver;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;
import spark.ssl.SslStores;

/**
 * Represents an embedded server that can be used in Spark. (this is currently Jetty by default).
 */
public interface EmbeddedServer {

    /**
     * Ignites the embedded server, listening on the specified port, running SSL secured with the specified keystore
     * and truststore.  If truststore is null, keystore is reused.
     *
     * @param host                    The address to listen on
     * @param port                    - the port
     * @param sslStores               - The SSL sslStores.
     * @param latch                   - the countdown latch
     * @param maxThreads              - max nbr of threads.
     * @param minThreads              - min nbr of threads.
     * @param threadIdleTimeoutMillis - idle timeout (ms).
     * @param requestLogFileName      - Set the output file name of the request log. The file name may be in
     *                                  include the pattern 'yyyy_mm_dd' to enable rollover.
     * @param requestLogRetainDays    - The number of days to retain files before deleting them. 0 to retain forever.
     * @return The port number the server was launched on.
     */
    int ignite(String host,
               int port,
               SslStores sslStores,
               CountDownLatch latch,
               int maxThreads,
               int minThreads,
               int threadIdleTimeoutMillis,
               String requestLogFileName,
               int requestLogRetainDays);

    /**
     * Configures the web sockets for the embedded server.
     *
     * @param webSocketHandlers          - web socket handlers.
     * @param webSocketIdleTimeoutMillis - Optional WebSocket idle timeout (ms).
     */
    default void configureWebSockets(Map<String, WebSocketHandlerWrapper> webSocketHandlers,
                                     Optional<Integer> webSocketIdleTimeoutMillis) {

        NotSupportedException.raise(getClass().getSimpleName(), "Web Sockets");
    }

    /**
     * Configure the session storage directory for the embedded server.
     *
     * @param sessionsDir - session directory
     */
    void configureSessionsDirectory(File sessionsDir);

    /**
     * Extinguish the embedded server.
     */
    void extinguish();
}
