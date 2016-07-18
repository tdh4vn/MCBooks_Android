package vn.mcbooks.mcbooks.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hungtran on 7/4/16.
 */
public class CheckConnection {

    private Context _context;

    //Hàm dựng khởi tạo đối tượng
    public CheckConnection(Context context) {
        this._context = context;
    }

    public boolean checkMobileInternetConn() {
        //Tạo đối tương ConnectivityManager để trả về thông tin mạng
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //Nếu đối tượng khác null
        if (connectivity != null) {
            //Nhận thông tin mạng
            NetworkInfo infoMobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (infoMobile != null) {
                //Tìm kiếm thiết bị đã kết nối được internet chưa
                if (infoMobile.isConnected()) {
                    return true;
                }
            }
            NetworkInfo infoWifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (infoWifi != null) {
                //Tìm kiếm thiết bị đã kết nối được internet chưa
                if (infoWifi.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


}
