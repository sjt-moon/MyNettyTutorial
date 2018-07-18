import io.netty.buffer.ByteBuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Handles a server-side channel.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter { // (1)

    Framer framer = new Framer();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String receivedMessage = ((ByteBuf) msg).toString(Charset.forName("UTF-8"));
        framer.addMessage(receivedMessage);
        if (framer.hasMessages()) {
            List<String> storedMessages = framer.popMessages();
            storedMessages.forEach(x -> {
                System.out.print("From client: " + x + "\n");
                ctx.write(Unpooled.copiedBuffer("From server: " + x + "\n", CharsetUtil.UTF_8));
                ctx.flush();
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}