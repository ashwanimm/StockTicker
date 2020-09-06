package com.proximity.stockticker.utils;

import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/*
*   Stock Socket class to connect to Web socket
 */

@WebSocket(maxTextMessageSize = 64 * 1024)
public class StockSocket
{
    private final CountDownLatch closeLatch;
    private static final String TAG = "StockSocket";

    private StockSubscriber stockSubscriber;

    @SuppressWarnings("unused")
    private Session session;

    public StockSocket(StockSubscriber stockSubscriber)
    {

        this.closeLatch = new CountDownLatch(1);
        this.stockSubscriber = stockSubscriber;
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
    {
        return this.closeLatch.await(duration, unit);
    }

   @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        Log.e(TAG, "**** Connection closed: " + statusCode + ". Reason : " + reason);

        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        //Log.e(TAG, "Got connect: " + session);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String msg)
    {
        //Log.e(TAG, "Got msg: " + msg);
        stockSubscriber.receivedStock(msg);
    }

    @OnWebSocketError
    public void onError(Throwable cause)
    {
        Log.e(TAG,"WebSocket Error: ");
        Log.e(TAG, "exception", cause);
    }

}
