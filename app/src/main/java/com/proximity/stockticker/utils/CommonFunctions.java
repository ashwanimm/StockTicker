package com.proximity.stockticker.utils;

import java.util.Arrays;
import java.util.Comparator;

/*  Common Functions Class. It contains reusable functions
*
 */
public class CommonFunctions {

    //Set the string 'place' in each ticker feed.
    public static String setPricePlaceHolder(String msg) {

        msg = msg.replaceAll("],\\[", ",place],[")
                .replaceAll("]]", ",place]]");

        return msg;
    }

    //Convert the Stock feed to Array
    public static String[][] convertStockFeedToArray(String msg) {

        // Split on this delimiter
        String[] rows = msg.split("],\\[");
        for (int i = 0; i < rows.length; i++) {

            // Remove any beginning and ending braces and any white spaces
            rows[i] = rows[i].replace("[[", "")
                    .replace("]]", "")
                    .replaceAll(" ", "")
                    .replaceAll("\"", "");
        }

        // Get the number of columns in a row
        int numberOfColumns = rows[0].split(",").length;

        // Setup your matrix
        String[][] matrix = new String[rows.length][numberOfColumns];

        // Populate your matrix
        for (int i = 0; i < rows.length; i++) {
            matrix[i] = rows[i].split(",");
            //Log.e(TAG, "****Matrix : " + matrix[i][0]  + " Price: " + matrix[i][1]);
        }

        return matrix;
    }

    // Function to sort by column
    public static void sortbyColumn(String arr[][], int col)
    {
        // Using built-in sort function Arrays.sort
        Arrays.sort(arr, new Comparator<String[]>() {

            @Override
            // Compare values according to columns
            public int compare(final String[] entry1,
                               final String[] entry2) {

                // To sort in descending order revert
                // the '>' Operator
                if (entry1[col].compareTo(entry2[col]) > 0)
                    return 1;
                else
                    return -1;
            }
        });
    }

    //Update the Stock feed Array with old price
    public static String[][] updateOldPrice(String[][] oldMatrix, String[][] matrix ) {

        String oldTicker, newTicker;
        String oldPrice;

        if(oldMatrix != null) {
            oldPrice ="place";

            for (int i = 0; i < matrix.length; i++) {
                newTicker = matrix[i][0];

                for(int j=0; j < oldMatrix.length; j++) {
                    oldTicker = oldMatrix[j][0];
                    if(oldTicker.compareTo(newTicker) == 0) {
                        oldPrice = oldMatrix[j][1];
                    }
                }

//                    Log.e(TAG, "***old price: " + oldPrice);

                if(oldPrice != "place") {
                    matrix[i][2] = oldPrice;
                } else {
                    matrix[i][2] = matrix[i][1];
                }

            }
        } else {
            for (int i = 0; i < matrix.length; i++) {

//                    Log.e(TAG, "***old price: " + oldPrice);
                matrix[i][2] = matrix[i][1];
            }

        }

        return matrix;
    }

}
