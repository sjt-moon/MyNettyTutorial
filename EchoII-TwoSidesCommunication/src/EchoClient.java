import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class EchoClient {
    static final String HOST = "localhost";
    static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //new LoggingHandler(LogLevel.TRACE),
                                    new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()),

                                    // if i remove string en/de-coder, no message appears
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    new EchoClientHandler());
                        }
                    });

            // Start the connection attempt.
            bootstrap.connect(HOST, PORT).sync().channel().closeFuture().sync();
            System.out.println("Message sent successfully.");
        } finally {
            group.shutdownGracefully();
        }
    }
}