package com.proximity.stockticker.utils;

import android.util.Log;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class StockSocketClient
{
    private static final String TAG = "StockSocketClient";
    WebSocketClient client;

  /* public final StockSocket socket;

    public StockSocketClient(StockSocket stockSocket) {
        this.socket = stockSocket;
    }*/


    public void start(StockSubscriber stockSubscriber)
    {
        String destUri = "ws://stocks.mnet.website";

//        WebSocketClient client = new WebSocketClient();
        client = new WebSocketClient();
        StockSocket socket = new StockSocket(stockSubscriber);

        try
        {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            Log.e(TAG, "**** Connecting to : " + echoUri );

            // wait for closed socket connection.
            socket.awaitClose(5, TimeUnit.SECONDS);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
/*            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }*/
        }
    }

    public void stop() {
        try
        {
            if(client != null) {
                client.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}