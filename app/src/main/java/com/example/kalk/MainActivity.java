package com.example.kalk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import java.lang.String;
import android.webkit.*;

public class MainActivity extends AppCompatActivity {

    private TextView show,show2;
    private String input="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = findViewById(R.id.textView0);
        show2 = findViewById(R.id.textView1);
    }

    private void clear() {
        input = "";
        show.setText("0");
        show2.setText("");
    }

    private void calculate() {
        WebView webview = new WebView(getApplicationContext());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.evaluateJavascript("(function() { return "+input+"; })();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                if(result.compareTo("null")==0) result=getString(R.string.error);
                refresh(result);
            }
        });
    }

    private void refresh(String result){
        show2.setText(input);
        show.setText(result);
        input="";
    }

    public void onClick(View v){
        int id = v.getId();
        switch (id) {
            case R.id.button15: { //=
                calculate();
                break;
            }
            case R.id.button16: { //C
                clear();
                break;
            }
            default:{
                if(input=="" && show2.getText().toString().compareTo("")!=0)
                    show2.setText(show.getText());
                input += ((Button)v).getText().toString();
                show.setText(input);
                break;
            }
        }
    }
}
