package com.proximity.stockticker.ui.home;

import android.util.Log;
import androidx.lifecycle.LiveData;

import androidx.annotation.MainThread;

import com.proximity.stockticker.utils.CommonFunctions;
import com.proximity.stockticker.utils.StockSocketClient;
import com.proximity.stockticker.utils.StockSubscriber;

/*
*   Stock live data class to listen for Stock feed.
*
*   The Stock feed msg string is coming from the Web socket. It is in the
 *  following format:
 *  [["aks",162.72047143583777],["lnkd",103.14996527503347],["aks",51.588167071340884],
 *  ["lnkd",107.005453158691],["aks",70.49797653507991],["aapl",31.94773288602456],
 *  ["aapl",142.2111679914566],["eva",104.73337930424617]]
 *
 */
public class StockLiveData extends LiveData<String[][]> {
    private static StockLiveData sInstance;
    private StockSocketClient stockManager;
    private static final String TAG = "StockLiveData";

    private String[][] oldMatrix;
    private StockSubscriber subscriber = new StockSubscriber() {

        @Override
        public void receivedStock(String msg)
        {


            //Add a place holder for old price. When we split using comma then
            // we will get 'place' at place holder
            msg = CommonFunctions.setPricePlaceHolder(msg);

            //Convert String to Array
            String[][] matrix = CommonFunctions.convertStockFeedToArray(msg);

            //Sort the Array on Ticker name
            CommonFunctions.sortbyColumn(matrix, 0);

            // Log matrix
            //Log.e(TAG, "*** Printing old matrix array: " + Arrays.deepToString(oldMatrix));
            //Log.e(TAG, "*** Printing new matrix array: " + Arrays.deepToString(matrix));

            //Set Tickers Old price
            matrix = CommonFunctions.updateOldPrice(oldMatrix, matrix);

            //Log.e(TAG, "*** Printing new matrix array with old price: " + Arrays.deepToString(matrix));

            //Save the matrix as oldMatrix so that on next Stock feed we can compare the prices
            oldMatrix = matrix;
            postValue(matrix);
        }

    };


    @MainThread
    public static StockLiveData get() {
        if (sInstance == null) {
            sInstance = new StockLiveData();
        }
        return sInstance;
    }

    /*
    *   Create a Web Socket Client. It will start listening on the Stock feed
     */
    protected StockLiveData() {
        stockManager = new StockSocketClient();
    }

    /*
    *   Start the Stock manager when the UI is Active
     */
    @Override
    protected void onActive() {
        stockManager.start(subscriber);
    }

    /*
     *   Stop the Stock manager (Close Web socket) when the UI is InActive
     */

    @Override
    protected void onInactive() {
        stockManager.stop();
    }
}