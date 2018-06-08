package com.iustu.vertxagent;

import com.iustu.vertxagent.register.Endpoint;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

/**
 * Author : Alex
 * Date : 2018/6/6 10:13
 * Description :
 */
public class ConsumerInBoundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private static Logger logger = LoggerFactory.getLogger(ConsumerInBoundHandler.class);


    private Random random = new Random();
    private List<Endpoint> endpoints = null;

    public ConsumerInBoundHandler(List<Endpoint> endpoints) throws IOReactorException {
        super();
        this.endpoints = endpoints;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws IOException {
//        if (msg instanceof HttpRequest) {
//            FullHttpRequest httpRequest = (FullHttpRequest) msg;
        Map<String, String> paramMap = getParamMap(msg);
        String interfaceName = paramMap.get("interface");
        String method = paramMap.get("method");
        String parameterTypesString = paramMap.get("parameterTypesString");
        String parameter = paramMap.get("parameter");
        try {
            consumer(ctx.channel(), interfaceName, method, parameterTypesString, parameter);
        } catch (Exception e) {
            ctx.channel().close();
            e.printStackTrace();
        }
//            ctx.channel().writeAndFlush(httpResponse)
//                    .addListener((ChannelFutureListener) future1 -> {
//                        if (future1.isSuccess()) {
//                            logger.info("provider write done");
//                        } else {
//                            logger.info("provider write error", future1.cause());
//                        }
//                    });

//        } else {
//            logger.info(msg.toString());
//            ctx.channel().close();
//            logger.error("unknown requset");
//        }
    }

    public void consumer(Channel channel, String interfaceName, String method, String parameterTypesString, String parameter) throws Exception {


        // 简单的负载均衡，随机取一个
        // TODO: 2018/5/31
        Endpoint endpoint = endpoints.get(random.nextInt(endpoints.size()));

        final String url = "http://" + endpoint.getHost() + ":" + endpoint.getPort();

//        RequestBody requestBody = new FormBody.Builder()
//                .add("interface", interfaceName)
//                .add("method", method)
//                .add("parameterTypesString", parameterTypesString)
//                .add("parameter", parameter)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//        Call call = httpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                logger.info("resp from provider error: ", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                ResponseBody body = response.body();
//                if (body != null) {
//                    try {
//                        byte[] bytes = body.bytes();
//                        logger.info("resp from provider: " + bytes.length + " | " + bytes);
//
////                        String string = new String(bytes);
//                        ByteBuf buffer = channel.alloc().ioBuffer(bytes.length).writeBytes(bytes);
//                        DefaultFullHttpResponse newResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
//                        HttpHeaders headers = newResponse.headers();
//                        headers.set(CONTENT_TYPE, "text/plain; charset=UTF-8");
//                        headers.set(CONTENT_LENGTH, String.valueOf(bytes.length));
//                        channel.writeAndFlush(newResponse)
//                                .addListener(future -> {
//                                    if(future.isSuccess()){
//                                        logger.error("success");
//                                        logger.error("failed");
//                                    }
//                                })
//                                .addListener(ChannelFutureListener.CLOSE)
//                        ;
////                        logger.info("resp from provider: " + string);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    logger.info("resp from provider: body == null");
//                }
//            }
//        });

//        final HttpPost post = new HttpPost(url);
//
//        List<BasicNameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("interface", interfaceName));
//        params.add(new BasicNameValuePair("method", method));
//        params.add(new BasicNameValuePair("parameterTypesString", parameterTypesString));
//        params.add(new BasicNameValuePair("parameter", parameter));
//        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf-8");
//        post.setEntity(formEntity);
//        httpAsyncClient.execute(post, new FutureCallback<HttpResponse>() {
//            @Override
//            public void completed(HttpResponse httpResponse) {
//                try {
//                    byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
//                    ByteBuf buffer = channel.alloc().ioBuffer(bytes.length).writeBytes(bytes);
//                    DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
//                    HttpHeaders headers = response.headers();
//                    headers.set(CONTENT_TYPE, "text/plain; charset=UTF-8");
//                    headers.set(CONTENT_LENGTH, String.valueOf(bytes.length));
//                    channel.writeAndFlush(response)
//                            .addListener(ChannelFutureListener.CLOSE)
//                    ;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failed(Exception e) {
//                logger.error("consumer response failed " + e.getLocalizedMessage(), e);
//            }
//
//            @Override
//            public void cancelled() {
//                logger.debug("consumer request cancelled");
//            }
//        });
//        try (Response response = call.execute()) {
//            if (response.isSuccessful()) {
//                channel.writeAndFlush(response.body().bytes());
//            }
//        }

//        HttpResponse response = future.get(6000, TimeUnit.MILLISECONDS);
//        channel.writeAndFlush(EntityUtils.toByteArray(response.getEntity()));
//                new FutureCallback<org.apache.http.HttpResponse>() {
//            @Override
//            public void completed(HttpResponse httpResponse) {
//                try {
//                    HttpEntity entity = httpResponse.getEntity();
//                    byte[] bytes = EntityUtils.toByteArray(entity);
//                    channel.writeAndFlush(bytes);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failed(Exception e) {
//                logger.error("consumer response failed " + e.getLocalizedMessage(), e);
//            }
//
//            @Override
//            public void cancelled() {
//                logger.debug("consumer request cancelled");
//            }
//        });

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(channel.eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(4096))
                                .addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {

                                    @Override
                                    public void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
                                        ByteBuf buffer = msg.content();
//                                        ByteBuf buffer = channel.alloc().buffer(bytes.length).writeBytes(bytes);
                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer.retain());
                                        HttpHeaders headers = response.headers();
                                        headers.set(CONTENT_TYPE, "text/plain; charset=UTF-8");
                                        headers.set(CONTENT_LENGTH, String.valueOf(buffer.readableBytes()));
                                        channel.writeAndFlush(response)
//                                                .addListener(future -> {
//                                                    if (future.isSuccess()) {
//                                                        logger.error("return consumer success");
//                                                    } else {
//                                                        logger.error("return consumer failed",future.cause());
//                                                    }
//                                                })
                                        ;
                                    }

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        super.channelActive(ctx);
                                        HttpRequest httpRequest = new DefaultFullHttpRequest(
                                                HttpVersion.HTTP_1_1,
                                                HttpMethod.POST,
                                                url
                                        );
                                        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
                                        HttpPostRequestEncoder bodyRequestEncoder = new HttpPostRequestEncoder(factory, httpRequest, false);
                                        bodyRequestEncoder.addBodyAttribute("interface", interfaceName);
                                        bodyRequestEncoder.addBodyAttribute("method", method);
                                        bodyRequestEncoder.addBodyAttribute("parameterTypesString", parameterTypesString);
                                        bodyRequestEncoder.addBodyAttribute("parameter", parameter);
                                        httpRequest = bodyRequestEncoder.finalizeRequest();
                                        ctx.channel().writeAndFlush(httpRequest);
                                    }

                                });
                    }
                })
                .connect(endpoint.getHost(), endpoint.getPort());

    }


    private Map<String, String> getParamMap(FullHttpRequest httpRequest) throws IOException {
        Map<String, String> paramMap = new HashMap<>();
        if (httpRequest.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
            decoder.parameters().forEach((key, value) -> paramMap.put(key, value.get(0)));
        } else if (httpRequest.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(httpRequest);
            decoder.offer(httpRequest);
            List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
            for (InterfaceHttpData param : paramList) {
                Attribute data = (Attribute) param;
                paramMap.put(data.getName(), data.getValue());
            }
        } else {
            logger.error("not support method", httpRequest.uri());
        }
        return paramMap;
    }


}