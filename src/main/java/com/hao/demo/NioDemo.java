package com.hao.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NioDemo {

    @SneakyThrows
    private static void runJdkNioServer() {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        SelectionKey serverSocketChannelSelectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);
        log.info("Server socket channel selection key: {}", serverSocketChannelSelectionKey);
        serverSocketChannel.bind(new InetSocketAddress(8080));

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove(); // must remove manually
                log.info("Selected key: {}", selectionKey);
                if (selectionKey.isAcceptable()) { // server socket channel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel sc = channel.accept();
                    log.info("Socket channel: {}", sc);
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ, null);
                } else if (selectionKey.isReadable()) { // socket channel
                     SocketChannel sc = (SocketChannel) selectionKey.channel();
                     ByteBuffer byteBuffer = ByteBuffer.allocate(32);
                     try {
                         int c = sc.read(byteBuffer);
                         if (c == -1) {
                             doUnregister(selectionKey);
                         } else {
                             byteBuffer.flip();
                             doRead(byteBuffer);
                             byteBuffer.flip();
                             sc.write(byteBuffer); // just echo back
                         }
                     } catch (IOException e) { // socket channel is down
                         e.printStackTrace();
                         doUnregister(selectionKey);
                     }
                }
            }
        }




    }

    private static void doUnregister(SelectionKey selectionKey) {
        log.info("Unregister channel {}", selectionKey.channel());
        selectionKey.cancel();
    }

    @SneakyThrows
    private static void doRead(ByteBuffer byteBuffer) {
        String received = Charset.defaultCharset().decode(byteBuffer).toString();
        log.info("Read: {}", received);
    }

    public static void main(String[] args) {
        runJdkNioServer();

    }
}
