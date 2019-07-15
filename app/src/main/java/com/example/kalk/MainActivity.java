package com.example.kalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.webkit.*;
import java.lang.String;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private TextView show, show2;
    private String input = "";

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

//    private void calculate() {
//        input = input.replaceAll("[+]+","+");
//        input = input.replaceAll("[-]+","-");
//        input = input.replaceAll("[*]+","*");
//        input = input.replaceAll("[/]+","/");
//        input = input.replaceAll("[.]+",".");
//
//        String tmp = input.toUpperCase().replaceAll("MOD","%");
//        tmp = tmp.replaceAll("E","Math.E");
//        tmp = tmp.replaceAll("LOG","Math.log");
//
//        if (tmp.charAt(0)>57 || tmp.charAt(0)<48)//if first char is between 0 ans 9 (ASCII code)
//            tmp = show2.getText()+tmp;
//
//        WebView webview = new WebView(getApplicationContext());
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.evaluateJavascript("(function() { return "+tmp+"; })();",new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String result) {
//                if (result.compareTo("null")==0) result = getString(R.string.error);
//                refresh(result);
//            }
//        });
//    }

    private void calculate() {
        if (input.charAt(0) > 57 || input.charAt(0) < 48)//if first char is between 0 ans 9 (ASCII code)
            input = show2.getText() + input;

        String parse = input.toLowerCase();
        String[] operators = {"\\^", "-", "+", "*", "/"};
        for (String tmp : operators)
            parse = parse.replaceAll("[" + tmp + "]+", " " + tmp + " ");
        parse = parse.replaceAll("[(]", " ( ");
        parse = parse.replaceAll("[)]", " ) ");
        parse = parse.replaceAll("mod", " % ");
        parse = parse.replaceAll("(\\W\\s- )", " -");
        if (parse.startsWith(" - ")) parse = parse.replaceFirst(" - ", "-");
        parse = parse.replaceAll(" [ ]+", " ");
        parse = parse.trim();

//        int open = StringUtils.countMatches;
//        int close = StringUtils.countMatches;
//        for (int i=open;i<=close;i++)
//            parse+=" )";

        refresh(RPN.calculate(RPN.toRPN(parse)));
    }

    private void refresh(String result) {
        show2.setText(input);
        show.setText(result);
        input = "";
    }

    public void onClick(View v) {
        if (!show2.getText().toString().isEmpty() && input.isEmpty())
            show2.setText(show.getText());
        if (show.getText().toString().compareTo("0") == 0)
            input = "";
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
            case R.id.button19: {//SPEC
                if (((Button) v).getText().toString().compareTo("SPEC") == 0) {
                    ((Button) v).setText("NORM");
                    findViewById(R.id.button18).setVisibility(View.VISIBLE);
                    findViewById(R.id.RowX).setVisibility(View.VISIBLE);
                } else {
                    ((Button) v).setText("SPEC");
                    findViewById(R.id.button18).setVisibility(View.INVISIBLE);
                    findViewById(R.id.RowX).setVisibility(View.INVISIBLE);
                }
                break;
            }
            case R.id.button17: {//backspace
                if (!input.isEmpty()) {
                    if (input.endsWith("d")) input = input.substring(0, input.length() - 3);
                    else input = input.substring(0, input.length() - 1);
                }
                if (input.isEmpty()) input = "0";
                show.setText(input);
                break;
            }
            default: {
                input += ((Button) v).getText().toString();
                show.setText(input);
                break;
            }
        }
    }
}
