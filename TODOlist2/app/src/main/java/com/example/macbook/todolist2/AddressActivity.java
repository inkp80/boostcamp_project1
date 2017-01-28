package com.example.macbook.todolist2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kyu on 2017-01-28.
 */

public class AddressActivity extends AppCompatActivity {
    public String loc_str;

    private WebView webView;
    private TextView result;
    private Button button;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        result = (TextView) findViewById(R.id.result);
        button = (Button) findViewById(R.id.add_location);



        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                String CallerIs = getIntent().getStringExtra("caller");
                switch (CallerIs){
                    case "Detail" :
                        DetailActivity.settingLocation(loc_str);
                        break;
                    case "Add" :
                        AddTodoTaskActivity.settingLocation(loc_str);
                        break;
                    default:
                        break;
                }
                finish();
            }
        });
    }

    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
        webView.loadUrl("http://codeman77.ivyro.net/getAddress.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loc_str = String.format("%s %s", arg2, arg3);
                    result.setText(String.format("%s %s", arg2, arg3));
                    init_webView();
                }
            });
        }
    }
}
