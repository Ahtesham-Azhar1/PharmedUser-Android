package com.zealsoft.pharmed.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ahtesham on 3/2/2020.
 */

public class Internet {

    public static boolean isAvailable(Context context) {
//        final String command = "ping -c 1 google.com";
//        try {
//            return Runtime.getRuntime().exec(command).waitFor() == 0;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }

//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 8)
//        {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                    .permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            //your codes here
//
//        }

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        try {
//                            InetAddress address = InetAddress.getByName("www.google.com");
//                            return !address.equals("");
//                        } catch (UnknownHostException e) {
//                            // Log error
//                        }
                        return true;
                    }
        }

        return false;
    }
}
