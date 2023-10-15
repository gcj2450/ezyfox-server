package com.tvd12.ezyfoxserver.nio;

import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketSecureServerCreator;
import com.tvd12.ezyfoxserver.nio.builder.impl.EzyWebSocketServerCreator;
import com.tvd12.ezyfoxserver.nio.websocket.EzyWsWritingLoopHandler;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSetting;
import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;
import com.tvd12.ezyfoxserver.socket.EzySocketWriter;
import org.eclipse.jetty.server.Server;

import javax.net.ssl.SSLContext;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyWebSocketServerBootstrap extends EzyAbstractSocketServerBootstrap {

    private Server websocketServer;
    private final SSLContext sslContext;
    private final EzyWebSocketSetting webSocketSetting;

    public EzyWebSocketServerBootstrap(Builder builder) {
        super(builder);
        this.sslContext = builder.sslContext;
        this.webSocketSetting = serverSettings.getWebsocket();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void start() throws Exception {
        websocketServer = newWebsocketServer();
        websocketServer.start();
        writingLoopHandler = newWritingLoopHandler();
        writingLoopHandler.start();
    }

    @Override
    public void destroy() {
        processWithLogException(() -> writingLoopHandler.destroy());
        processWithLogException(() -> websocketServer.stop());
    }

    private Server newWebsocketServer() {
        return newSocketServerCreator()
            .setting(webSocketSetting)
            .sessionManagementSetting(serverSettings.getSessionManagement())
            .sessionManager((EzyNioSessionManager) server.getSessionManager())
            .handlerGroupManager(handlerGroupManager)
            .socketDataReceiver(socketDataReceiver)
            .create();
    }

    private EzySocketEventLoopHandler newWritingLoopHandler() {
        EzyWsWritingLoopHandler loopHandler = new EzyWsWritingLoopHandler();
        loopHandler.setThreadPoolSize(webSocketSetting.getWriterThreadPoolSize());
        loopHandler.setEventHandlerSupplier(() -> {
            EzySocketWriter eventHandler = new EzySocketWriter();
            eventHandler.setWriterGroupFetcher(handlerGroupManager);
            eventHandler.setSessionTicketsQueue(sessionTicketsQueue);
            return eventHandler;
        });
        return loopHandler;
    }

    private EzyWebSocketServerCreator newSocketServerCreator() {
        if (webSocketSetting.isSslActive()) {
            return new EzyWebSocketSecureServerCreator(sslContext);
        }
        return new EzyWebSocketServerCreator();
    }

    public static class Builder
        extends EzyAbstractSocketServerBootstrap.Builder<Builder, EzyWebSocketServerBootstrap> {

        private SSLContext sslContext;

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        @Override
        public EzyWebSocketServerBootstrap build() {
            return new EzyWebSocketServerBootstrap(this);
        }
    }
}
