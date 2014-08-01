/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ning.http.client;

import static com.ning.http.client.AsyncHttpClientConfigDefaults.*;

import com.ning.http.client.date.TimeConverter;
import com.ning.http.client.filter.IOExceptionFilter;
import com.ning.http.client.filter.RequestFilter;
import com.ning.http.client.filter.ResponseFilter;
import com.ning.http.util.ProxyUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Configuration class to use with a {@link AsyncHttpClient}. System property can be also used to configure this
 * object default behavior by doing:
 * <p/>
 * -Dcom.ning.http.client.AsyncHttpClientConfig.nameOfTheProperty
 * ex:
 * <p/>
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultMaxTotalConnections
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultMaxTotalConnections
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultMaxConnectionsPerHost
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultConnectionTimeoutInMS
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultIdleConnectionInPoolTimeoutInMS
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultRequestTimeoutInMS
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultRedirectsEnabled
 * -Dcom.ning.http.client.AsyncHttpClientConfig.defaultMaxRedirects
 */
public class AsyncHttpClientConfig {

    protected int maxTotalConnections;
    protected int maxConnectionPerHost;
    protected int connectionTimeOutInMs;
    protected int webSocketIdleTimeoutInMs;
    protected int idleConnectionInPoolTimeoutInMs;
    protected int idleConnectionTimeoutInMs;
    protected int requestTimeoutInMs;
    protected boolean redirectEnabled;
    protected int maxRedirects;
    protected boolean compressionEnabled;
    protected String userAgent;
    protected boolean allowPoolingConnection;
    protected ExecutorService applicationThreadPool;
    protected ProxyServerSelector proxyServerSelector;
    protected SSLContext sslContext;
    protected SSLEngineFactory sslEngineFactory;
    protected AsyncHttpProviderConfig<?, ?> providerConfig;
    protected ConnectionsPool<?, ?> connectionsPool;
    protected Realm realm;
    protected List<RequestFilter> requestFilters;
    protected List<ResponseFilter> responseFilters;
    protected List<IOExceptionFilter> ioExceptionFilters;
    protected int requestCompressionLevel;
    protected int maxRequestRetry;
    protected boolean allowSslConnectionPool;
    protected boolean useRawUrl;
    protected boolean removeQueryParamOnRedirect;
    protected HostnameVerifier hostnameVerifier;
    protected int ioThreadMultiplier;
    protected boolean strict302Handling;
    protected boolean useRelativeURIsWithConnectProxies;
    protected int maxConnectionLifeTimeInMs;
    protected TimeConverter timeConverter;

    protected AsyncHttpClientConfig() {
    }

    private AsyncHttpClientConfig(int maxTotalConnections,
                                  int maxConnectionPerHost,
                                  int connectionTimeOutInMs,
                                  int webSocketTimeoutInMs,
                                  int idleConnectionInPoolTimeoutInMs,
                                  int idleConnectionTimeoutInMs,
                                  int requestTimeoutInMs,
                                  int connectionMaxLifeTimeInMs,
                                  boolean redirectEnabled,
                                  int maxRedirects,
                                  boolean compressionEnabled,
                                  String userAgent,
                                  boolean keepAlive,
                                  ExecutorService applicationThreadPool,
                                  ProxyServerSelector proxyServerSelector,
                                  SSLContext sslContext,
                                  SSLEngineFactory sslEngineFactory,
                                  AsyncHttpProviderConfig<?, ?> providerConfig,
                                  ConnectionsPool<?, ?> connectionsPool, Realm realm,
                                  List<RequestFilter> requestFilters,
                                  List<ResponseFilter> responseFilters,
                                  List<IOExceptionFilter> ioExceptionFilters,
                                  int requestCompressionLevel,
                                  int maxRequestRetry,
                                  boolean allowSslConnectionCaching,
                                  boolean useRawUrl,
                                  boolean removeQueryParamOnRedirect,
                                  HostnameVerifier hostnameVerifier,
                                  int ioThreadMultiplier,
                                  boolean strict302Handling,
                                  boolean useRelativeURIsWithConnectProxies,
                                  TimeConverter timeConverter) {

        this.maxTotalConnections = maxTotalConnections;
        this.maxConnectionPerHost = maxConnectionPerHost;
        this.connectionTimeOutInMs = connectionTimeOutInMs;
        this.webSocketIdleTimeoutInMs = webSocketTimeoutInMs;
        this.idleConnectionInPoolTimeoutInMs = idleConnectionInPoolTimeoutInMs;
        this.idleConnectionTimeoutInMs = idleConnectionTimeoutInMs;
        this.requestTimeoutInMs = requestTimeoutInMs;
        this.maxConnectionLifeTimeInMs = connectionMaxLifeTimeInMs;
        this.redirectEnabled = redirectEnabled;
        this.maxRedirects = maxRedirects;
        this.compressionEnabled = compressionEnabled;
        this.userAgent = userAgent;
        this.allowPoolingConnection = keepAlive;
        this.sslContext = sslContext;
        this.sslEngineFactory = sslEngineFactory;
        this.providerConfig = providerConfig;
        this.connectionsPool = connectionsPool;
        this.realm = realm;
        this.requestFilters = requestFilters;
        this.responseFilters = responseFilters;
        this.ioExceptionFilters = ioExceptionFilters;
        this.requestCompressionLevel = requestCompressionLevel;
        this.maxRequestRetry = maxRequestRetry;
        this.allowSslConnectionPool = allowSslConnectionCaching;
        this.removeQueryParamOnRedirect = removeQueryParamOnRedirect;
        this.hostnameVerifier = hostnameVerifier;
        this.ioThreadMultiplier = ioThreadMultiplier;
        this.strict302Handling = strict302Handling;
        this.useRelativeURIsWithConnectProxies = useRelativeURIsWithConnectProxies;

        if (applicationThreadPool == null) {
            this.applicationThreadPool = Executors.newCachedThreadPool();
        } else {
            this.applicationThreadPool = applicationThreadPool;
        }
        this.proxyServerSelector = proxyServerSelector;
        this.useRawUrl = useRawUrl;
        this.timeConverter = timeConverter;
    }

    /**
     * Return the maximum number of connections an {@link com.ning.http.client.AsyncHttpClient} can handle.
     *
     * @return the maximum number of connections an {@link com.ning.http.client.AsyncHttpClient} can handle.
     */
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    /**
     * Return the maximum number of connections per hosts an {@link com.ning.http.client.AsyncHttpClient} can handle.
     *
     * @return the maximum number of connections per host an {@link com.ning.http.client.AsyncHttpClient} can handle.
     */
    public int getMaxConnectionPerHost() {
        return maxConnectionPerHost;
    }

    /**
     * Return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can wait when connecting to a remote host
     *
     * @return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can wait when connecting to a remote host
     */
    public int getConnectionTimeoutInMs() {
        return connectionTimeOutInMs;
    }

    /**
     * Return the maximum time, in milliseconds, a {@link com.ning.http.client.websocket.WebSocket} may be idle before being timed out.
     * @return the maximum time, in milliseconds, a {@link com.ning.http.client.websocket.WebSocket} may be idle before being timed out.
     */
    public int getWebSocketIdleTimeoutInMs() {
        return webSocketIdleTimeoutInMs;
    }

    /**
     * Return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can stay idle.
     *
     * @return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can stay idle.
     */
    public int getIdleConnectionTimeoutInMs() {
        return idleConnectionTimeoutInMs;
    }

    /**
     * Return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} will keep connection
     * in pool.
     *
     * @return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} will keep connection
     *         in pool.
     */
    public int getIdleConnectionInPoolTimeoutInMs() {
        return idleConnectionInPoolTimeoutInMs;
    }

    /**
     * Return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} wait for a response
     *
     * @return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} wait for a response
     */
    public int getRequestTimeoutInMs() {
        return requestTimeoutInMs;
    }

    /**
     * Is HTTP redirect enabled
     *
     * @return true if enabled.
     */
    public boolean isRedirectEnabled() {
        return redirectEnabled;
    }

    /**
     * Get the maximum number of HTTP redirect
     *
     * @return the maximum number of HTTP redirect
     */
    public int getMaxRedirects() {
        return maxRedirects;
    }

    /**
     * Is the {@link ConnectionsPool} support enabled.
     *
     * @return true if keep-alive is enabled
     */
    public boolean getAllowPoolingConnection() {
        return allowPoolingConnection;
    }

    /**
     * Is the {@link ConnectionsPool} support enabled.
     *
     * @return true if keep-alive is enabled
     * @deprecated - Use {@link AsyncHttpClientConfig#getAllowPoolingConnection()}
     */
    public boolean getKeepAlive() {
        return allowPoolingConnection;
    }

    /**
     * Return the USER_AGENT header value
     *
     * @return the USER_AGENT header value
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Is HTTP compression enabled.
     *
     * @return true if compression is enabled
     */
    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    /**
     * Return the {@link java.util.concurrent.ExecutorService} an {@link AsyncHttpClient} use for handling
     * asynchronous response.
     *
     * @return the {@link java.util.concurrent.ExecutorService} an {@link AsyncHttpClient} use for handling
     *         asynchronous response.
     */
    public ExecutorService executorService() {
        return applicationThreadPool;
    }

    /**
     * An instance of {@link com.ning.http.client.ProxyServer} used by an {@link AsyncHttpClient}
     *
     * @return instance of {@link com.ning.http.client.ProxyServer}
     */
    public ProxyServerSelector getProxyServerSelector() {
        return proxyServerSelector;
    }

    /**
     * Return an instance of {@link SSLContext} used for SSL connection.
     *
     * @return an instance of {@link SSLContext} used for SSL connection.
     */
    public SSLContext getSSLContext() {
        return sslContext;
    }

    /**
     * Return an instance of {@link ConnectionsPool}
     *
     * @return an instance of {@link ConnectionsPool}
     */
    public ConnectionsPool<?, ?> getConnectionsPool() {
        return connectionsPool;
    }

    /**
     * Return an instance of {@link SSLEngineFactory} used for SSL connection.
     *
     * @return an instance of {@link SSLEngineFactory} used for SSL connection.
     */
    public SSLEngineFactory getSSLEngineFactory() {
        if (sslEngineFactory == null) {
            return new SSLEngineFactory() {
                public SSLEngine newSSLEngine() {
                    if (sslContext != null) {
                        SSLEngine sslEngine = sslContext.createSSLEngine();
                        sslEngine.setUseClientMode(true);
                        return sslEngine;
                    } else {
                        return null;
                    }
                }
            };
        }
        return sslEngineFactory;
    }

    /**
     * Return the {@link com.ning.http.client.AsyncHttpProviderConfig}
     *
     * @return the {@link com.ning.http.client.AsyncHttpProviderConfig}
     */
    public AsyncHttpProviderConfig<?, ?> getAsyncHttpProviderConfig() {
        return providerConfig;
    }

    /**
     * Return the current {@link Realm}}
     *
     * @return the current {@link Realm}}
     */
    public Realm getRealm() {
        return realm;
    }

    /**
     * Return the list of {@link RequestFilter}
     *
     * @return Unmodifiable list of {@link ResponseFilter}
     */
    public List<RequestFilter> getRequestFilters() {
        return Collections.unmodifiableList(requestFilters);
    }

    /**
     * Return the list of {@link ResponseFilter}
     *
     * @return Unmodifiable list of {@link ResponseFilter}
     */
    public List<ResponseFilter> getResponseFilters() {
        return Collections.unmodifiableList(responseFilters);
    }

    /**
     * Return the list of {@link java.io.IOException}
     *
     * @return Unmodifiable list of {@link java.io.IOException}
     */
    public List<IOExceptionFilter> getIOExceptionFilters() {
        return Collections.unmodifiableList(ioExceptionFilters);
    }

    /**
     * Return the compression level, or -1 if no compression is used.
     *
     * @return the compression level, or -1 if no compression is use
     */
    public int getRequestCompressionLevel() {
        return requestCompressionLevel;
    }

    /**
     * Return the number of time the library will retry when an {@link java.io.IOException} is throw by the remote server
     *
     * @return the number of time the library will retry when an {@link java.io.IOException} is throw by the remote server
     */
    public int getMaxRequestRetry() {
        return maxRequestRetry;
    }

    /**
     * Return true is SSL connection polling is enabled. Default is true.
     *
     * @return true is enabled.
     */
    public boolean isSslConnectionPoolEnabled() {
        return allowSslConnectionPool;
    }


    /**
     * @return the useRawUrl
     */
    public boolean isUseRawUrl() {
        return useRawUrl;
    }

    /**
     * Return true if the query parameters will be stripped from the request when a redirect is requested.
     *
     * @return true if the query parameters will be stripped from the request when a redirect is requested.
     */
    public boolean isRemoveQueryParamOnRedirect() {
        return removeQueryParamOnRedirect;
    }

    /**
     * Return true if one of the {@link java.util.concurrent.ExecutorService} has been shutdown.
     *
     * @return true if one of the {@link java.util.concurrent.ExecutorService} has been shutdown.
     *
     * @deprecated use #isValid
     */
    public boolean isClosed() {
        return !isValid();
    }

    /**
     * @return <code>true</code> if both the application and reaper thread pools
     *  haven't yet been shutdown.
     *
     * @since 1.7.21
     */
    public boolean isValid() {
        boolean atpRunning = true;
        try {
            atpRunning = applicationThreadPool.isShutdown();
        } catch (Exception ignore) {
            // isShutdown() will thrown an exception in an EE7 environment
            // when using a ManagedExecutorService.
            // When this is the case, we assume it's running.
        }
        return atpRunning;
    }

    /**
     * Return the {@link HostnameVerifier}
     *
     * @return the {@link HostnameVerifier}
     */
    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    /**
     * @return number to multiply by availableProcessors() that will determine # of NioWorkers to use
     */
    public int getIoThreadMultiplier() {
        return ioThreadMultiplier;
    }

    /**
     * <p>
     * In the case of a POST/Redirect/Get scenario where the server uses a 302
     * for the redirect, should AHC respond to the redirect with a GET or
     * whatever the original method was.  Unless configured otherwise,
     * for a 302, AHC, will use a GET for this case.
     * </p>
     *
     * @return <code>true</code> if string 302 handling is to be used,
     *  otherwise <code>false</code>.
     *
     * @since 1.7.2
     */
    public boolean isStrict302Handling() {
        return strict302Handling;
    }

    /**
     * @return<code>true</code> if AHC should use relative URIs instead of absolute ones when talking with a SSL proxy
     * or WebSocket proxy, otherwise <code>false</code>.
     *  
     *  @since 1.7.12
     *  @deprecated Use isUseRelativeURIsWithConnectProxies instead.
     */
    @Deprecated
    public boolean isUseRelativeURIsWithSSLProxies() {
        return useRelativeURIsWithConnectProxies;
    }

    /**
     * @return<code>true</code> if AHC should use relative URIs instead of absolute ones when talking with a proxy
     * using the CONNECT method, for example when using SSL or WebSockets.
     *
     *  @since 1.8.13
     */
    public boolean isUseRelativeURIsWithConnectProxies() {
        return useRelativeURIsWithConnectProxies;
    }

    /**
     * Return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} will keep connection in the pool, or -1 to keep connection while possible.
     *
     * @return the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} will keep connection in the pool, or -1 to keep connection while possible.
     */
    public int getMaxConnectionLifeTimeInMs() {
        return maxConnectionLifeTimeInMs;
    }

    /**
     * @return 1.8.2
     */
    public TimeConverter getTimeConverter() {
        return timeConverter;
    }

    /**
     * Builder for an {@link AsyncHttpClient}
     */
    public static class Builder {
        private int maxTotalConnections = defaultMaxTotalConnections();
        private int maxConnectionPerHost = defaultMaxConnectionPerHost();
        private int connectionTimeOutInMs = defaultConnectionTimeOutInMs();
        private int webSocketIdleTimeoutInMs = defaultWebSocketIdleTimeoutInMs();
        private int idleConnectionInPoolTimeoutInMs = defaultIdleConnectionInPoolTimeoutInMs();
        private int idleConnectionTimeoutInMs = defaultIdleConnectionTimeoutInMs();
        private int requestTimeoutInMs = defaultRequestTimeoutInMs();
        private int maxConnectionLifeTimeInMs = defaultMaxConnectionLifeTimeInMs();
        private boolean redirectEnabled = defaultRedirectEnabled();
        private int maxDefaultRedirects = defaultMaxRedirects();
        private boolean compressionEnabled = defaultCompressionEnabled();
        private String userAgent = defaultUserAgent();
        private boolean useProxyProperties = defaultUseProxyProperties();
        private boolean useProxySelector = defaultUseProxySelector();
        private boolean allowPoolingConnection = defaultAllowPoolingConnection();
        private boolean useRelativeURIsWithConnectProxies = defaultUseRelativeURIsWithConnectProxies();
        private int requestCompressionLevel = defaultRequestCompressionLevel();
        private int maxRequestRetry = defaultMaxRequestRetry();
        private int ioThreadMultiplier = defaultIoThreadMultiplier();
        private boolean allowSslConnectionPool = defaultAllowSslConnectionPool();
        private boolean useRawUrl = defaultUseRawUrl();
        private boolean removeQueryParamOnRedirect = defaultRemoveQueryParamOnRedirect();
        private boolean strict302Handling = defaultStrict302Handling();
        private HostnameVerifier hostnameVerifier = defaultHostnameVerifier();

        private ExecutorService applicationThreadPool;
        private ProxyServerSelector proxyServerSelector = null;
        private SSLContext sslContext;
        private SSLEngineFactory sslEngineFactory;
        private AsyncHttpProviderConfig<?, ?> providerConfig;
        private ConnectionsPool<?, ?> connectionsPool;
        private Realm realm;
        private final List<RequestFilter> requestFilters = new LinkedList<RequestFilter>();
        private final List<ResponseFilter> responseFilters = new LinkedList<ResponseFilter>();
        private final List<IOExceptionFilter> ioExceptionFilters = new LinkedList<IOExceptionFilter>();
        private TimeConverter timeConverter;

        public Builder() {
        }

        /**
         * Set the maximum number of connections an {@link com.ning.http.client.AsyncHttpClient} can handle.
         *
         * @param maxTotalConnections the maximum number of connections an {@link com.ning.http.client.AsyncHttpClient} can handle.
         * @return a {@link Builder}
         */
        public Builder setMaximumConnectionsTotal(int maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
            return this;
        }

        /**
         * Set the maximum number of connections per hosts an {@link com.ning.http.client.AsyncHttpClient} can handle.
         *
         * @param maxConnectionPerHost the maximum number of connections per host an {@link com.ning.http.client.AsyncHttpClient} can handle.
         * @return a {@link Builder}
         */
        public Builder setMaximumConnectionsPerHost(int maxConnectionPerHost) {
            this.maxConnectionPerHost = maxConnectionPerHost;
            return this;
        }

        /**
         * Set the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can wait when connecting to a remote host
         *
         * @param connectionTimeOutInMs the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can wait when connecting to a remote host
         * @return a {@link Builder}
         */
        public Builder setConnectionTimeoutInMs(int connectionTimeOutInMs) {
            this.connectionTimeOutInMs = connectionTimeOutInMs;
            return this;
        }

        /**
         * Set the maximum time in millisecond an {@link com.ning.http.client.websocket.WebSocket} can stay idle.
         *
         * @param webSocketIdleTimeoutInMs
         *         the maximum time in millisecond an {@link com.ning.http.client.websocket.WebSocket} can stay idle.
         * @return a {@link Builder}
         */
        public Builder setWebSocketIdleTimeoutInMs(int webSocketIdleTimeoutInMs) {
            this.webSocketIdleTimeoutInMs = webSocketIdleTimeoutInMs;
            return this;
        }

        /**
         * Set the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can stay idle.
         *
         * @param idleConnectionTimeoutInMs
         *         the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} can stay idle.
         * @return a {@link Builder}
         */
        public Builder setIdleConnectionTimeoutInMs(int idleConnectionTimeoutInMs) {
            this.idleConnectionTimeoutInMs = idleConnectionTimeoutInMs;
            return this;
        }

        /**
         * Set the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} will keep connection
         * idle in pool.
         *
         * @param idleConnectionInPoolTimeoutInMs
         *         the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} will keep connection
         *         idle in pool.
         * @return a {@link Builder}
         */
        public Builder setIdleConnectionInPoolTimeoutInMs(int idleConnectionInPoolTimeoutInMs) {
            this.idleConnectionInPoolTimeoutInMs = idleConnectionInPoolTimeoutInMs;
            return this;
        }

        /**
         * Set the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} wait for a response
         *
         * @param requestTimeoutInMs the maximum time in millisecond an {@link com.ning.http.client.AsyncHttpClient} wait for a response
         * @return a {@link Builder}
         */
        public Builder setRequestTimeoutInMs(int requestTimeoutInMs) {
            this.requestTimeoutInMs = requestTimeoutInMs;
            return this;
        }

        /**
         * Set to true to enable HTTP redirect
         *
         * @param redirectEnabled true if enabled.
         * @return a {@link Builder}
         */
        public Builder setFollowRedirects(boolean redirectEnabled) {
            this.redirectEnabled = redirectEnabled;
            return this;
        }

        /**
         * Set the maximum number of HTTP redirect
         *
         * @param maxDefaultRedirects the maximum number of HTTP redirect
         * @return a {@link Builder}
         */
        public Builder setMaximumNumberOfRedirects(int maxDefaultRedirects) {
            this.maxDefaultRedirects = maxDefaultRedirects;
            return this;
        }

        /**
         * Enable HTTP compression.
         *
         * @param compressionEnabled true if compression is enabled
         * @return a {@link Builder}
         */
        public Builder setCompressionEnabled(boolean compressionEnabled) {
            this.compressionEnabled = compressionEnabled;
            return this;
        }

        /**
         * Set the USER_AGENT header value
         *
         * @param userAgent the USER_AGENT header value
         * @return a {@link Builder}
         */
        public Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        /**
         * Set true if connection can be pooled by a {@link ConnectionsPool}. Default is true.
         *
         * @param allowPoolingConnection true if connection can be pooled by a {@link ConnectionsPool}
         * @return a {@link Builder}
         */
        public Builder setAllowPoolingConnection(boolean allowPoolingConnection) {
            this.allowPoolingConnection = allowPoolingConnection;
            return this;
        }

        /**
         * Set true if connection can be pooled by a {@link ConnectionsPool}. Default is true.
         *
         * @param allowPoolingConnection true if connection can be pooled by a {@link ConnectionsPool}
         * @return a {@link Builder}
         * @deprecated - Use {@link com.ning.http.client.AsyncHttpClientConfig.Builder#setAllowPoolingConnection(boolean)}
         */
        public Builder setKeepAlive(boolean allowPoolingConnection) {
            this.allowPoolingConnection = allowPoolingConnection;
            return this;
        }

        /**
         * Set the {@link java.util.concurrent.ExecutorService} an {@link AsyncHttpClient} use for handling
         * asynchronous response.
         *
         * @param applicationThreadPool the {@link java.util.concurrent.ExecutorService} an {@link AsyncHttpClient} use for handling
         *                              asynchronous response.
         * @return a {@link Builder}
         */
        public Builder setExecutorService(ExecutorService applicationThreadPool) {
            this.applicationThreadPool = applicationThreadPool;
            return this;
        }

        /**
         * Set an instance of {@link ProxyServerSelector} used by an {@link AsyncHttpClient}
         *
         * @param proxyServerSelector instance of {@link ProxyServerSelector}
         * @return a {@link Builder}
         */
        public Builder setProxyServerSelector(ProxyServerSelector proxyServerSelector) {
            this.proxyServerSelector = proxyServerSelector;
            return this;
        }

        /**
         * Set an instance of {@link ProxyServer} used by an {@link AsyncHttpClient}
         *
         * @param proxyServer instance of {@link com.ning.http.client.ProxyServer}
         * @return a {@link Builder}
         */
        public Builder setProxyServer(ProxyServer proxyServer) {
            this.proxyServerSelector = ProxyUtils.createProxyServerSelector(proxyServer);
            return this;
        }

        /**
         * Set the {@link SSLEngineFactory} for secure connection.
         *
         * @param sslEngineFactory the {@link SSLEngineFactory} for secure connection
         * @return a {@link Builder}
         */
        public Builder setSSLEngineFactory(SSLEngineFactory sslEngineFactory) {
            this.sslEngineFactory = sslEngineFactory;
            return this;
        }

        /**
         * Set the {@link SSLContext} for secure connection.
         *
         * @param sslContext the {@link SSLContext} for secure connection
         * @return a {@link Builder}
         */
        public Builder setSSLContext(final SSLContext sslContext) {
            this.sslEngineFactory = new SSLEngineFactory() {
                public SSLEngine newSSLEngine() throws GeneralSecurityException {
                    SSLEngine sslEngine = sslContext.createSSLEngine();
                    sslEngine.setUseClientMode(true);
                    return sslEngine;
                }
            };
            this.sslContext = sslContext;
            return this;
        }

        /**
         * Set the {@link com.ning.http.client.AsyncHttpProviderConfig}
         *
         * @param providerConfig the {@link com.ning.http.client.AsyncHttpProviderConfig}
         * @return a {@link Builder}
         */
        public Builder setAsyncHttpClientProviderConfig(AsyncHttpProviderConfig<?, ?> providerConfig) {
            this.providerConfig = providerConfig;
            return this;
        }

        /**
         * Set the {@link ConnectionsPool}
         *
         * @param connectionsPool the {@link ConnectionsPool}
         * @return a {@link Builder}
         */
        public Builder setConnectionsPool(ConnectionsPool<?, ?> connectionsPool) {
            this.connectionsPool = connectionsPool;
            return this;
        }

        /**
         * Set the {@link Realm}  that will be used for all requests.
         *
         * @param realm the {@link Realm}
         * @return a {@link Builder}
         */
        public Builder setRealm(Realm realm) {
            this.realm = realm;
            return this;
        }

        /**
         * Add an {@link com.ning.http.client.filter.RequestFilter} that will be invoked before {@link com.ning.http.client.AsyncHttpClient#executeRequest(Request)}
         *
         * @param requestFilter {@link com.ning.http.client.filter.RequestFilter}
         * @return this
         */
        public Builder addRequestFilter(RequestFilter requestFilter) {
            requestFilters.add(requestFilter);
            return this;
        }

        /**
         * Remove an {@link com.ning.http.client.filter.RequestFilter} that will be invoked before {@link com.ning.http.client.AsyncHttpClient#executeRequest(Request)}
         *
         * @param requestFilter {@link com.ning.http.client.filter.RequestFilter}
         * @return this
         */
        public Builder removeRequestFilter(RequestFilter requestFilter) {
            requestFilters.remove(requestFilter);
            return this;
        }

        /**
         * Add an {@link com.ning.http.client.filter.ResponseFilter} that will be invoked as soon as the response is
         * received, and before {@link AsyncHandler#onStatusReceived(HttpResponseStatus)}.
         *
         * @param responseFilter an {@link com.ning.http.client.filter.ResponseFilter}
         * @return this
         */
        public Builder addResponseFilter(ResponseFilter responseFilter) {
            responseFilters.add(responseFilter);
            return this;
        }

        /**
         * Remove an {@link com.ning.http.client.filter.ResponseFilter} that will be invoked as soon as the response is
         * received, and before {@link AsyncHandler#onStatusReceived(HttpResponseStatus)}.
         *
         * @param responseFilter an {@link com.ning.http.client.filter.ResponseFilter}
         * @return this
         */
        public Builder removeResponseFilter(ResponseFilter responseFilter) {
            responseFilters.remove(responseFilter);
            return this;
        }

        /**
         * Add an {@link com.ning.http.client.filter.IOExceptionFilter} that will be invoked when an {@link java.io.IOException}
         * occurs during the download/upload operations.
         *
         * @param ioExceptionFilter an {@link com.ning.http.client.filter.ResponseFilter}
         * @return this
         */
        public Builder addIOExceptionFilter(IOExceptionFilter ioExceptionFilter) {
            ioExceptionFilters.add(ioExceptionFilter);
            return this;
        }

        /**
         * Remove an {@link com.ning.http.client.filter.IOExceptionFilter} tthat will be invoked when an {@link java.io.IOException}
         * occurs during the download/upload operations.
         *
         * @param ioExceptionFilter an {@link com.ning.http.client.filter.ResponseFilter}
         * @return this
         */
        public Builder removeIOExceptionFilter(IOExceptionFilter ioExceptionFilter) {
            ioExceptionFilters.remove(ioExceptionFilter);
            return this;
        }

        /**
         * Return the compression level, or -1 if no compression is used.
         *
         * @return the compression level, or -1 if no compression is use
         */
        public int getRequestCompressionLevel() {
            return requestCompressionLevel;
        }

        /**
         * Set the compression level, or -1 if no compression is used.
         *
         * @param requestCompressionLevel compression level, or -1 if no compression is use
         * @return this
         */
        public Builder setRequestCompressionLevel(int requestCompressionLevel) {
            this.requestCompressionLevel = requestCompressionLevel;
            return this;
        }

        /**
         * Set the number of time a request will be retried when an {@link java.io.IOException} occurs because of a Network exception.
         *
         * @param maxRequestRetry the number of time a request will be retried
         * @return this
         */
        public Builder setMaxRequestRetry(int maxRequestRetry) {
            this.maxRequestRetry = maxRequestRetry;
            return this;
        }

        /**
         * Return true is if connections pooling is enabled.
         *
         * @param allowSslConnectionPool true if enabled
         * @return this
         */
        public Builder setAllowSslConnectionPool(boolean allowSslConnectionPool) {
            this.allowSslConnectionPool = allowSslConnectionPool;
            return this;
        }

        /**
         * Allows use unescaped URLs in requests
         * useful for retrieving data from broken sites
         *
         * @param useRawUrl
         * @return this
         */
        public Builder setUseRawUrl(boolean useRawUrl) {
            this.useRawUrl = useRawUrl;
            return this;
        }

        /**
         * Set to false if you don't want the query parameters removed when a redirect occurs.
         *
         * @param removeQueryParamOnRedirect
         * @return this
         */
        public Builder setRemoveQueryParamsOnRedirect(boolean removeQueryParamOnRedirect) {
            this.removeQueryParamOnRedirect = removeQueryParamOnRedirect;
            return this;
        }

        /**
         * Sets whether AHC should use the default JDK ProxySelector to select a proxy server.
         * <p/>
         * If useProxySelector is set to <code>true</code> but {@link #setProxyServer(ProxyServer)}
         * was used to explicitly set a proxy server, the latter is preferred.
         * <p/>
         * See http://docs.oracle.com/javase/7/docs/api/java/net/ProxySelector.html
         */
        public Builder setUseProxySelector(boolean useProxySelector) {
            this.useProxySelector = useProxySelector;
            return this;
        }

        /**
         * Sets whether AHC should use the default http.proxy* system properties
         * to obtain proxy information.  This differs from {@link #setUseProxySelector(boolean)}
         * in that AsyncHttpClient will use its own logic to handle the system properties,
         * potentially supporting other protocols that the the JDK ProxySelector doesn't.
         * <p/>
         * If useProxyProperties is set to <code>true</code> but {@link #setUseProxySelector(boolean)}
         * was also set to true, the latter is preferred.
         * <p/>
         * See http://download.oracle.com/javase/1.4.2/docs/guide/net/properties.html
         */
        public Builder setUseProxyProperties(boolean useProxyProperties) {
            this.useProxyProperties = useProxyProperties;
            return this;
        }

        public Builder setIOThreadMultiplier(int multiplier) {
            this.ioThreadMultiplier = multiplier;
            return this;
        }

        /**
         * Set the {@link HostnameVerifier}
         *
         * @param hostnameVerifier {@link HostnameVerifier}
         * @return this
         */
        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * Configures this AHC instance to be strict in it's handling of 302 redirects
         * in a POST/Redirect/GET situation.
         *
         * @param strict302Handling strict handling
         *
         * @return this
         *
         * @since 1.7.2
         */
        public Builder setStrict302Handling(final boolean strict302Handling) {
            this.strict302Handling = strict302Handling;
            return this;
        }
      
        /**
         * Configures this AHC instance to use relative URIs instead of absolute ones when talking with a SSL proxy or
         * WebSocket proxy.
         *
         * @param useRelativeURIsWithSSLProxies
         * @return this
         *
         * @since 1.7.2
         * @deprecated Use setUseRelativeURIsWithConnectProxies instead.
         */
        public Builder setUseRelativeURIsWithSSLProxies(boolean useRelativeURIsWithSSLProxies) {
            this.useRelativeURIsWithConnectProxies = useRelativeURIsWithSSLProxies;
            return this;
        }

        /**
         * Configures this AHC instance to use relative URIs instead of absolute ones when making requests through
         * proxies using the CONNECT method, such as when making SSL requests or WebSocket requests.
         *
         * @param useRelativeURIsWithConnectProxies
         * @return this
         *
         * @since 1.8.13
         */
        public Builder setUseRelativeURIsWithConnectProxies(boolean useRelativeURIsWithConnectProxies) {
            this.useRelativeURIsWithConnectProxies = useRelativeURIsWithConnectProxies;
            return this;
        }

        /**
         * Set the maximum time in millisecond connection can be added to the pool for further reuse
         *
         * @param maxConnectionLifeTimeInMs the maximum time in millisecond connection can be added to the pool for further reuse
         * @return a {@link Builder}
         */
        public Builder setMaxConnectionLifeTimeInMs(int maxConnectionLifeTimeInMs) {
           this.maxConnectionLifeTimeInMs = maxConnectionLifeTimeInMs;
           return this;
        }

        public Builder setTimeConverter(TimeConverter timeConverter) {
            this.timeConverter = timeConverter;
            return this;
        }

        /**
         * Create a config builder with values taken from the given prototype configuration.
         *
         * @param prototype the configuration to use as a prototype.
         */
        public Builder(AsyncHttpClientConfig prototype) {
            allowPoolingConnection = prototype.getAllowPoolingConnection();
            providerConfig = prototype.getAsyncHttpProviderConfig();
            connectionsPool = prototype.getConnectionsPool();
            connectionTimeOutInMs = prototype.getConnectionTimeoutInMs();
            idleConnectionInPoolTimeoutInMs = prototype.getIdleConnectionInPoolTimeoutInMs();
            idleConnectionTimeoutInMs = prototype.getIdleConnectionTimeoutInMs();
            maxConnectionPerHost = prototype.getMaxConnectionPerHost();
            maxConnectionLifeTimeInMs = prototype.getMaxConnectionLifeTimeInMs();
            maxDefaultRedirects = prototype.getMaxRedirects();
            maxTotalConnections = prototype.getMaxTotalConnections();
            proxyServerSelector = prototype.getProxyServerSelector();
            realm = prototype.getRealm();
            requestTimeoutInMs = prototype.getRequestTimeoutInMs();
            sslContext = prototype.getSSLContext();
            sslEngineFactory = prototype.getSSLEngineFactory();
            userAgent = prototype.getUserAgent();
            redirectEnabled = prototype.isRedirectEnabled();
            compressionEnabled = prototype.isCompressionEnabled();
            applicationThreadPool = prototype.executorService();

            requestFilters.clear();
            responseFilters.clear();
            ioExceptionFilters.clear();

            requestFilters.addAll(prototype.getRequestFilters());
            responseFilters.addAll(prototype.getResponseFilters());
            ioExceptionFilters.addAll(prototype.getIOExceptionFilters());

            requestCompressionLevel = prototype.getRequestCompressionLevel();
            useRawUrl = prototype.isUseRawUrl();
            ioThreadMultiplier = prototype.getIoThreadMultiplier();
            maxRequestRetry = prototype.getMaxRequestRetry();
            allowSslConnectionPool = prototype.getAllowPoolingConnection();
            removeQueryParamOnRedirect = prototype.isRemoveQueryParamOnRedirect();
            hostnameVerifier = prototype.getHostnameVerifier();
            strict302Handling = prototype.isStrict302Handling();
            timeConverter = prototype.timeConverter;
        }

        /**
         * Build an {@link AsyncHttpClientConfig}
         *
         * @return an {@link AsyncHttpClientConfig}
         */
        public AsyncHttpClientConfig build() {
            if (applicationThreadPool == null) {
                applicationThreadPool = Executors
                        .newCachedThreadPool(new ThreadFactory() {
                            public Thread newThread(Runnable r) {
                                Thread t = new Thread(r,
                                        "AsyncHttpClient-Callback");
                                t.setDaemon(true);
                                return t;
                            }
                        });
            }

            if (proxyServerSelector == null && useProxySelector) {
                proxyServerSelector = ProxyUtils.getJdkDefaultProxyServerSelector();
            }

            if (proxyServerSelector == null && useProxyProperties) {
                proxyServerSelector = ProxyUtils.createProxyServerSelector(System.getProperties());
            }

            if (proxyServerSelector == null) {
                proxyServerSelector = ProxyServerSelector.NO_PROXY_SELECTOR;
            }

            return new AsyncHttpClientConfig(maxTotalConnections,
                    maxConnectionPerHost,
                    connectionTimeOutInMs,
                    webSocketIdleTimeoutInMs,
                    idleConnectionInPoolTimeoutInMs,
                    idleConnectionTimeoutInMs,
                    requestTimeoutInMs,
                    maxConnectionLifeTimeInMs,
                    redirectEnabled,
                    maxDefaultRedirects,
                    compressionEnabled,
                    userAgent,
                    allowPoolingConnection,
                    applicationThreadPool,
                    proxyServerSelector,
                    sslContext,
                    sslEngineFactory,
                    providerConfig,
                    connectionsPool,
                    realm,
                    requestFilters,
                    responseFilters,
                    ioExceptionFilters,
                    requestCompressionLevel,
                    maxRequestRetry,
                    allowSslConnectionPool,
                    useRawUrl,
                    removeQueryParamOnRedirect,
                    hostnameVerifier,
                    ioThreadMultiplier,
                    strict302Handling,
                    useRelativeURIsWithConnectProxies,
                    timeConverter);
        }
    }
}
