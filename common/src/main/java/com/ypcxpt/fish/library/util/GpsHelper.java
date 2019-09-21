package com.ypcxpt.fish.library.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class GpsHelper {

    @SuppressLint("MissingPermission")
    public static Location getGpsLocation(Context context) {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled)
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null && net_loc != null) {
            //smaller the number more accurate result will
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                finalLoc = net_loc;
            else
                finalLoc = gps_loc;
            // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)
        } else {
            if (gps_loc != null) {
                finalLoc = gps_loc;
            } else if (net_loc != null) {
                finalLoc = net_loc;
            } else {
//                Intent i = new Intent();
//                i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                context.startActivity(i);
//                return finalLoc;
//                Toaster.showShort("未开启位置权限");
            }
        }
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new LocationListener() {
//            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//            @Override
//            public void onLocationChanged(Location location) {
////            mWeatherPresenter.getWeatherInfo(location);
//            }
//
//            // Provider被disable时触发此函数，比如GPS被关闭
//            @Override
//            public void onProviderDisabled(String provider) {
////            mWeatherPresenter.getWeatherInfo(null);
//            }
//
//            // Provider被enable时触发此函数，比如GPS被打开
//            @Override
//            public void onProviderEnabled(String provider) {}
//
//            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//        });
        return finalLoc;
    }

    @SuppressLint("MissingPermission")
    public static Location getGpsLocation2(Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
