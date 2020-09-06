package com.proximity.stockticker.DataTypes;

import java.util.Date;

/*
*   Stock Object for storing the Stock properties
 */
public class Stock {
    public String ticker, price, lastChange, oldPrice;

    public Stock(String ticker, String price, String lastChange, String oldPrice) {
        this.ticker = ticker;
        this.price = price;
        this.lastChange = lastChange;
        this.oldPrice = oldPrice;
    }
}
