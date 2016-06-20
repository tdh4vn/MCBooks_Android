package vn.mcbooks.mcbooks.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.mcbooks.mcbooks.R;

/**
 * Created by hungtran on 6/11/16.
 */
public class SaleOffsDialog extends DialogFragment {
    int mNum = 4;
    private List<String> saleOffs;

    public List<String> getSaleOffs() {
        return saleOffs;
    }

    public void setSaleOffs(List<String> saleOffs) {
        this.saleOffs = saleOffs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_sale_offs, container, false);
        getDialog().setTitle("Khuyến mại");
        TextView textView = (TextView)v.findViewById(R.id.txtSaleOffs);
        String str = "";
        for (String s: saleOffs){
            str = str + "&#9679; " + s +"<br><br>";
        }
        textView.setText(Html.fromHtml(str));
        return v;
    }
}
