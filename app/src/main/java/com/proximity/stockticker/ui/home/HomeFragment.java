package com.proximity.stockticker.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.proximity.stockticker.DataTypes.Stock;
import com.proximity.stockticker.R;
import com.proximity.stockticker.utils.StockAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/*
*   Home Screen. It shows the Live Stock Ticker.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private TableLayout mTableLayout;
    private Context mContext;
    private ListView listView;
    private ProgressBar spinner;

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm a");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = Objects.requireNonNull(getActivity()).getApplicationContext();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) root.findViewById(R.id.lvStocks);

        spinner = (ProgressBar) root.findViewById(R.id.progressBar1);

        spinner.setVisibility(View.VISIBLE);

        //Observe the the Stock feed and update the UI
        LiveData<String[][]> stockLiveListener = new StockLiveData();
        stockLiveListener.observe(getViewLifecycleOwner(), stocks -> {

            // Update the UI.
            Log.e(TAG, "****Updating the UI");
            //Log.e(TAG, "*** Printing matrix array: " + stocks);
             createStockLayout(stocks);
        });

        return root;
    }

    /*
    *   Create and Display the Stock screen
     */
    public void createStockLayout(String[][] data) {


        // Construct the data source
        ArrayList<Stock> arrayOfStocks = new ArrayList<Stock>();

        //Add the headings in the Stock data
        Stock stock = new Stock("Ticker", "Price", "Last Update", "Old Price");
        arrayOfStocks.add(stock);

        String currentDateandTime = sdf.format(new Date());

        //Loop through the Stock feed and create Stock Array List
        for (int i = 0; i < data.length; i++) {

            String price = data[i][1];
            int indexOfDecimal = price.indexOf(".");
            price = price.substring(0, (indexOfDecimal + 3));

            String oldPrice = data[i][2];
            indexOfDecimal = oldPrice.indexOf(".");
            oldPrice = oldPrice.substring(0, (indexOfDecimal + 3));

            String ticker = (data[i][0]).toUpperCase();
            String lastChange = currentDateandTime;
            stock = new Stock(ticker, price, lastChange, oldPrice);
            arrayOfStocks.add(stock);
        }

        // Create the adapter to convert the array to views
        StockAdapter adapter = new StockAdapter(mContext, arrayOfStocks);

        spinner.setVisibility(View.GONE);

        // Attach the adapter to a ListView
        listView.setAdapter(adapter);
    }
}