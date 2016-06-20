package vn.mcbooks.mcbooks.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import vn.mcbooks.mcbooks.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMCBooksFragment extends BaseFragment {

    WebView webView;
    String content = "<div class=\"pad10\"><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align: center; background-color: white; background-position: initial initial; background-repeat: initial initial;\"><span style=\"font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t<img width=\"650\" border=\"0\" alt=\"\" src=\"http://mcbooks.vn/images/upload/MCBooks/10176023_305251589632939_3281354949020480752_n.jpg\"></span></p><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt;\"><span style=\"color: black; font-weight: bold; font-size: 12pt;\"><span style=\"font-size: 12pt;\">Công ty Cổ phần sách MCBooks</span></span><span style=\"color: black; font-size: 12pt;\"></span></span></p><span style=\"font-size: 12pt;\"><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"color: black; font-size: 12pt;\">Tên tiếng Anh: MCBooks Joint stock Company.</span></p><span style=\"font-size: 12pt;\"><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"color: black; font-size: 12pt;\">Tên viết tắt: MCBooks. JSC</span></p><span style=\"font-size: 12pt;\"><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\"><span style=\"font-weight: bold; font-size: 12pt;\">1.\n" +
            "Tên đầy đủ:</span><span class=\"apple-converted-space\" style=\"font-weight: bold; font-size: 12pt;\">&nbsp;</span>Công ty\n" +
            "Cổ phần Sách MCBooks \n" +
            "\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t<o:p></o:p></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\"><span style=\"font-weight: bold;\">2</span>. <span style=\"font-weight: bold; font-size: 12pt;\">Slogan</span>: Knowledge sharing - Chia sẻ tri thức <br>\n" +
            "\t\t</span></p>\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\">Đúng với slogan của mình, MCBooks - chuyên sách ngoại ngữ - luôn đem tới và sẻ chia những nguồn tri thức cho người đọc. Chúng tôi sẽ không ngừng nỗ lực để nguồn tri thức ngày càng lớn hơn và ngày càng lan toa r mạnh mẽ hơn.<br>\n" +
            "\t<span style=\"font-size: 12pt;\"><span style=\"color: rgb(52, 52, 52); font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\"><span style=\"font-weight: bold;\">3.</span><span style=\"font-size: 12pt;\" class=\"apple-converted-space\">&nbsp;</span><span style=\"font-weight: bold; font-size: 12pt;\">Trụ sở chính:</span><span class=\"apple-converted-space\" style=\"font-weight: bold; font-size: 12pt;\">&nbsp; Số 26/245 Phố Mai Dịch, Cầu Giấy, Hà Nội,\n" +
            "Việt Nam.</span><span style=\"color: rgb(52, 52, 52); font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\">Tel: (084-04) 37.921.466&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style=\"font-weight: bold; font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\">Email: <a href=\"mailto:contact@mcbooks.vn\">contact@mcbooks.vn</a><span style=\"font-weight: bold; font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\">Website: <a href=\"http://mcbooks.vn\">mcbooks.vn</a><span style=\"color: red; font-weight: bold; font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\"><span style=\"font-size: 12pt;\" class=\"st\">4.<span style=\"font-weight: bold; font-size: 12pt;\"> Chi nhánh: </span></span><span style=\"font-weight: bold; font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\"><span style=\"font-weight: bold;\">Địa chỉ: Số 45 đường số 8, khu phố 5,\n" +
            "Hiệp Bình Chánh, Quận Thủ Đức, HCM</span><span style=\"font-weight: bold; font-size: 12pt;\">\n" +
            "\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t\t<o:p></o:p></span></span></p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\">\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "</p><span style=\"font-size: 12pt;\">\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-top:3.0pt;margin-right:0in;margin-bottom:3.0pt;margin-left: 0in;text-align:justify;line-height:200%\"><span style=\"font-size: 12pt;\">Tel: (084- 08) 666.093.98&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mobile: 098.640.7923\n" +
            "\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t<o:p></o:p></span></p><span style=\"font-size: 12pt;\"><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"color: black; font-size: 12pt;\">Giấy chứng nhận đăng kí kinh doanh và đăng kí thuế số 0102883146 do Sở Kế Hoạch và Đầu tư thành phố Hà Nội cấp lần đầu vào ngày 25/08/2008 và đăng kí thay đổi vào ngày 23/08/2013.</span></p><span style=\"font-size: 12pt;\"><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "</span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt; color: black;\"><br>\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t</span></p><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt; color: black;\"><br>\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t</span></p><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt; color: black;\"><br>\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t</span></p><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt; color: black;\"><br>\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t</span></p><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt; color: black;\"><br>\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t</span></p><span style=\"font-size: 12pt;\"></span>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "<p style=\"text-align:justify;mso-line-height-alt:.5pt;background:white\"><span style=\"font-size: 12pt;\"><br>\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t</span></p><span style=\"font-size: 12pt;\"></span></div>";

    public AboutMCBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about_mcbooks, container, false);
        String mime = "text/html";
        String encoding = "utf-8";

        webView = (WebView)view.findViewById(R.id.aboutMCBooks);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, content, mime, encoding, null);
        return view;
    }

}
