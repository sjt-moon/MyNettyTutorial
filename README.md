# MyNettyTutorial
Netty based projects collection

> A ServerSocketChannelFactory which creates a server-side NIO-based ServerSocketChannel. It utilizes the non-blocking I/O mode which was introduced with NIO to serve many number of concurrent connections efficiently.
> 
> How threads work
> 
> There are two types of threads in a NioServerSocketChannelFactory; one is boss thread and the other is worker thread.
> 
> Boss threads
> 
> Each bound ServerSocketChannel has its own boss thread. For example, if you opened two server ports such as 80 and 443, you will have two boss threads. A boss thread accepts incoming connections until the port is unbound. Once a connection is accepted successfully, the boss thread passes the accepted Channel to one of the worker threads that the NioServerSocketChannelFactory manages.
> 
> Worker threads
> 
> One NioServerSocketChannelFactory can have one or more worker threads. A worker thread performs non-blocking read and write for one or more Channels in a non-blocking mode.

## DiscardServer
Server receives data and never responds. 

## EchoServer - Telnet
Server receives data from a client and echos back. Same to my HTTP server implemented in C, I add a message framer in which received messages are stored and parsed into lines of characters.

To test, open a telnet window and try ``telnet localhost 8080``.

## EchoII - TwoSidesCommunication
It's kind of embarrassing that I got stuck on replacing Telnet with a java client. I resort to [Echo!](https://stackoverflow.com/questions/47675650/java-netty-client-cannot-send-message-to-server-but-telnet-to-sever-ok) from StackOverflow to get my program running. 

*Echo!* provides some new interfaces comparing to *Netty User Guide*, namely *channelRead0*, *StringEncoder* and *StringDecoder*.

### Difference between *channelRead0* and *channelRead*
[StackOverflow](https://stackoverflow.com/questions/31631725/netty-channelread0-not-firing) gives some guidance on the difference between *channelRead0* and *channelRead*. Basically, *channelRead* reads an Object and tries to call *channelRead0*. Like this:

```
@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    boolean release = true;
    try {
        if (acceptInboundMessage(msg)) {
            @SuppressWarnings("unchecked")
            I imsg = (I) msg;
            channelRead0(ctx, imsg);
        } else {
            release = false;
            ctx.fireChannelRead(msg);
        }
    } finally {
        if (autoRelease && release) {
            ReferenceCountUtil.release(msg);
        }
    }
}
```
To test this idea, you could also override *channelRead* with this:
```
@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    String message = (String) msg;
    channelRead0(ctx, message);
}
```

### *StringEncoder* and *StringDecoder*
[Netty Doc](https://netty.io/4.0/api/io/netty/handler/codec/string/StringEncoder.html) gives the detailed explanation. Namely, *StringEncoder* will encode the requested string into a *ByteBuf*, while *StringDecoder* decodes the received *ByteBuf* into string. Thus, you could use **channelRead(ChannelHandlerContext ctx, String msg)** instead of **channelRead(ChannelHandlerContext ctx, Object msg)** (well, msg is often transformed into a *ByteBuf* first).

### DelimiterBasedFrameDecoder
I add this as a child handler on the server side. As I add it before *StringEncoder* and *StringDecoder*,

> Hence, if the transferred message is without expected delimiter (e.g. new line in this case), both client/server will continue waiting and think that message is still transferring.

To make it right, be sure to add a ``\n`` for each message sent.
