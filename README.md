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

## EchoServer
Server receives data from a client and echos back. Same to my HTTP server implemented in C, I add a message framer in which received messages are stored and parsed into lines of characters.
