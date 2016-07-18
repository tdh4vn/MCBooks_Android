package vn.mcbooks.mcbooks.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.dialog.SaleOffsDialog;
import vn.mcbooks.mcbooks.dialog.ShortReviewBookDialog;
import vn.mcbooks.mcbooks.eventbus.SetBottomBarPosition;
import vn.mcbooks.mcbooks.eventbus.SetIsReadyQRCodeEvent;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.model.GetBookByIDResult;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRCodeFragment extends BaseFragment {

    private boolean isResultBarcode = false;

    private SurfaceView cameraView;

    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;

    ProgressDialog progressDialogScaning;

    public QRCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        cameraView = (SurfaceView) view.findViewById(R.id.camera_view);
        progressDialogScaning = new ProgressDialog(getActivity());
        progressDialogScaning.setTitle("Đang quét thông tin sách");
        progressDialogScaning.setMessage("Đang quét thông tin sách");
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new SetBottomBarPosition(2, true));
//        IBottomNavigationController bottomNavigationController = (IBottomNavigationController) getActivity();
//        bottomNavigationController.setCurrentOfBottomNavigation(2);
        buildBarcodeDetector();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }

    private void buildBarcodeDetector(){
        barcodeDetector =
                new BarcodeDetector.Builder(getActivity())
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (!isResultBarcode){
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isResultBarcode = true;
                                getBookByBarcode(barcodes.valueAt(0).displayValue);
                                Log.d("QRCode", barcodes.valueAt(0).displayValue);
                            }
                        });
                    }
                }
            }
        });

        cameraSource = new CameraSource
                .Builder(getActivity(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

    }

    private void getBookByBarcode(String barcode){

        progressDialogScaning.show();
        progressDialogScaning.setCancelable(true);

        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
        Call<GetBookByIDResult> getBookByIDResultCall
                = getBookService.getBookByQRCode(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), barcode);
        getBookByIDResultCall.enqueue(new Callback<GetBookByIDResult>() {
            @Override
            public void onResponse(Call<GetBookByIDResult> call, Response<GetBookByIDResult> response) {
                if (response.body().getCode() == 1){
                    Log.d("Khong ro", "abcs2");
                    progressDialogScaning.dismiss();
                    ShortReviewBookDialog shortReviewBookDialog = ShortReviewBookDialog.create(response.body().getResult());
                    ((IOpenFragment)getActivity()).openDialogFragment(shortReviewBookDialog);
                } else {
                    Log.d("Khong ro", response.body().getCode() + "");
                    progressDialogScaning.dismiss();
                    isResultBarcode = false;
                    showToast(response.body().getMessage(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<GetBookByIDResult> call, Throwable t) {
                progressDialogScaning.setMessage("Không tìm thấy sách");
                isResultBarcode = false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.stop();
    }


    // This method will be called when a HelloWorldEvent is posted
    @Subscribe
    public void onEvent(SetIsReadyQRCodeEvent event){
        isResultBarcode = event.isReady();
    }
}
