package com.example.kalk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import java.lang.String;

public class MainActivity extends AppCompatActivity {

    private TextView show,show2;
    private double wynik,last;
    private String input="";
    private int dot=0;
    private char operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wynik = 0;
        show = findViewById(R.id.textView0);
        show2 = findViewById(R.id.textView1);
    }

    private void number0(double nr){
        try {
            if (wynik == 0 || operation == ' ')
                wynik=nr;
            else{
                double tmp = wynik;
                if(dot>0){
                    tmp+=nr/(10^dot++);
                }else{
                    tmp*=10;
                    tmp+=nr;
                }
                wynik=tmp;
            }
            refresh();
        }catch (Exception e){}
    }

    private void number(int nr){
        if(nr>0)
            input += Integer.toString(nr);
        else if(dot==0) {
            if(input.compareTo("")==0)
                input ="0.";
            else
                input += '.';
            dot = 1;
        }
        //show.setText(input);
        wynik = Double.parseDouble(input);
        refresh();
    }

    private void refresh(){
        if(last>0) show2.setText(Double.toString(last)+"  "+operation);
        else show2.setText("");
        show.setText(Double.toString(wynik));
    }

    private void setOperation(char op){
        calc(false);
        last = wynik;
        wynik = 0;
        operation = op;
        refresh();
    }

    private void calc(Boolean f){
        input="";
        dot=0;
        switch (operation) {
            case '+': {
                wynik += last;
                break;
            }
            case '-': {
                wynik = last - wynik;
                break;
            }
            case '*': {
                wynik *= last;
                break;
            }
            case '/': {
                wynik = last / wynik;
                break;
            }
        }
        if(f) {
            operation = ' ';
            last=0;
            refresh();
        }
    }

    public void onClick(View v){
        int id = v.getId();
        switch (id) {
            case R.id.button0: {
                number(0);
                break;
            }
            case R.id.button1: {
                number(1);
                break;
            }
            case R.id.button2: {
                number(2);
                break;
            }
            case R.id.button3: {
                number(3);
                break;
            }
            case R.id.button4: {
                number(4);
                break;
            }
            case R.id.button5: {
                number(5);
                break;
            }
            case R.id.button6: {
                number(6);
                break;
            }
            case R.id.button7: {
                number(7);
                break;
            }
            case R.id.button8: {
                number(8);
                break;
            }
            case R.id.button9: {
                number(9);
                break;
            }
            case R.id.button10: {
                //if(dot==0)dot=1;
                number(-1);
                break;
            }
            case R.id.button11: {
                setOperation('/');
                break;
            }
            case R.id.button12: {
                setOperation('*');
                break;
            }
            case R.id.button13: {
                setOperation('-');
                break;
            }
            case R.id.button14: {//+
                setOperation('+');
                break;
            }
            case R.id.button15: {
                calc(true);
                break;
            }
            case R.id.button16: {
                wynik=0;
                last=0;
                operation=' ';
                dot=0;
                refresh();
                break;
            }
        }
    }
}
