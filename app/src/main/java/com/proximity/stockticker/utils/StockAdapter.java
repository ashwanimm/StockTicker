package com.proximity.stockticker.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proximity.stockticker.DataTypes.Stock;
import com.proximity.stockticker.R;

import java.util.ArrayList;

/*
*   Stock Adapter Class to dynamically generate the Stock screen.
*   It takes the Stock ArrayList and generate the Stock view
 */
public class StockAdapter extends ArrayAdapter<Stock> {
    public StockAdapter(Context context, ArrayList<Stock> stocks) {
        super(context, 0, stocks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Stock stock = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_stock, parent, false);
        }

        // Lookup view for data population
        TextView ticker = (TextView) convertView.findViewById(R.id.ticker);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView lastChange = (TextView) convertView.findViewById(R.id.lastChange);

        //For the first position only set the Titles
        if(position == 0) {
            ticker.setTextSize(15);
            ticker.setTextColor(Color.parseColor("#000000"));
            price.setTextSize(15);
            price.setTextColor(Color.parseColor("#000000"));
            price.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lastChange.setTextSize(15);
            lastChange.setTextColor(Color.parseColor("#000000"));
        } else {
            //Compare the old and current price
            Float oldPriceFloat=Float.parseFloat(stock.oldPrice);
            Float currentPriceFloat=Float.parseFloat(stock.price);

            Float diff = currentPriceFloat - oldPriceFloat;

            //If Price increased show color green
            if(diff > 0) {
                price.setBackgroundColor(Color.parseColor("#008000"));
            } else if(diff < 0){
                price.setBackgroundColor(Color.parseColor("#FF0000"));
            }

        }
        // Populate the data into the template view using the data object
        ticker.setText(stock.ticker);
        price.setText(stock.price);
        lastChange.setText(stock.lastChange);

        // Return the completed view to render on screen
        return convertView;
    }
}