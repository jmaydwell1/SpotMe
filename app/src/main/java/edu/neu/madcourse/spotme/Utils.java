package edu.neu.madcourse.spotme;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message,
                Toast.LENGTH_SHORT).show();
    }


    /**
     * This routine calculates the distance between two points (given the
     * latitude/longitude of those points). It is being used to calculate
     * the distance between two locations using GeoDataSource (TM) products
     * @param latitude1 latitude of userA
     * @param longitude1 longitude of userA
     * @param latitude2 latitude of userB
     * @param longitude2 of userB
     * @param unit unit could be 'M' (default) for statute miles, 'K' for km, and 'N' for nautical miles
     * @return distance between the two users
     * Taken from: https://www.geodatasource.com/developers/java
     */
    public static double distance(String latitude1, String longitude1, String latitude2, String longitude2, String unit) {
        double lat1 = Double.valueOf(latitude1);
        double lon1 = Double.valueOf(longitude1);
        double lat2 = Double.valueOf(latitude2);
        double lon2 = Double.valueOf(longitude2);

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}