package com.example.kalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.View;
import java.lang.String;

public class MainActivity extends AppCompatActivity {

    private TextView show, show2;
    private String input = "";
    private int Lbracket = 0, Rbracket = 0;
    private boolean dot = true, operator = false;

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
        Lbracket = Rbracket = 0;
        dot = operator = true;
    }

    private void calculate() {
        if (input.isEmpty()) return;

        if (!((input.charAt(0) >= '0' && input.charAt(0) <= '9') || input.charAt(0) == '.' || input.charAt(0) == '('))
            //jeśli pierwszym znakiem jest operator (nie liczba i nie kropka lub nawias)
            //wstaw liczbę z poprzedniego wyniku na początek
            input = show2.getText() + input;

        input = input.toLowerCase().replaceAll("mod", " % ");
        String result;
        try {
            result = RPN.calculate(RPN.toRPN(input));
        } catch (Exception e) {
            Log.e("NumberFormatException", e.getMessage());
            result = "E ERROR";
        }

        //w klasie RPN nie mogłem uzyskać dostępu do zasobów więc mamy obejście
        //można by tu ustawić try{}catch(){} ale chciałem by obsługa błędów była tam
        if (result.startsWith("E"))
            switch (result) {
                case "E NumberFormatException": {
                    result = getString(R.string.NumberFormatException);
                    break;
                }
                case "E ArithmeticException": {
                    result = getString(R.string.ArithmeticException);
                    break;
                }
                case "E OVERFLOW": {
                    result = getString(R.string.OVERFLOW);
                    break;
                }
                case "E ERROR": {
                    result = getString(R.string.error);
                }
            }
        refresh(result);
    }

    private void refresh(String result) {
        show2.setText(input);
        show.setText(result);
        input = "";
    }

    public void onClick(View v) {
        input = input.replaceFirst(" [ ]+", " ");
        if (!show2.getText().toString().isEmpty() && input.isEmpty())
            show2.setText(show.getText());
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
                input = input.trim();
                if (!input.isEmpty()) {
                    if (input.endsWith(".")) dot = true;
                    else if (RPN.precedence_tab.containsKey(Character.toString(input.charAt(input.length() - 1))))
                        operator = true;
                    else if (input.endsWith("(")) Lbracket--;
                    else if (input.endsWith(")")) Rbracket--;

                    if (input.endsWith("d")) {
                        input = input.substring(0, input.length() - 3);
                        operator = true;
                    } else input = input.substring(0, input.length() - 1);
                }
                if (input.length() > 2) {
                    char a = input.charAt(input.length() - 2);
                    if (a >= '0' && a <= '9')
                        input = input.trim();
                }
                if (input.isEmpty()) {
                    input = "";
                    clear();
                }
                show.setText(input);
                break;
            }
            default: {
                String key = ((Button) v).getText().toString();
                switch (key) {
                    case "(": {
                        Lbracket++;
                        input += "( ";
                        break;
                    }
                    case ")": {
                        if (Lbracket > Rbracket) {
                            Rbracket++;
                            input += " )";
                        }
                        break;
                    }
                    case ".": {
                        if (dot) {
                            operator = true;
                            dot = false;
                            input += ".";
                        }
                        break;
                    }
                    case "mod":
                    case "^":
                    case "%":
                    case "*":
                    case "/":
                    case "+": {
                        if (operator) {
                            input += " " + key + " ";
                            dot = true;
                            operator = false;
                        }
                        break;
                    }
                    case "-": {
                        if (operator) {
                            operator = false;
                            dot = true;
                            if (input.isEmpty()) {
                                if (show2.getText().length() == 0)
                                    input += "-";
                                else
                                    input += " - ";
                            } else if (input.charAt(input.length() - 1) > '0'
                                    && input.charAt(input.length() - 1) < '9'
                                    || input.charAt(input.length() - 1) == ')'
                                    || input.charAt(input.length() - 1) == 'E')
                                input += " - ";
                            else
                                input += " -";
                        }
                        break;
                    }
                    case "E": {
                        if (!input.isEmpty())
                            if (input.charAt(input.length() - 1) > '0'
                                    && input.charAt(input.length() - 1) < '9'
                                    || input.charAt(input.length() - 1) == '.'
                                    || input.charAt(input.length() - 1) == 'E')
                                return;
                        dot = false;
                    }
                    default: {
                        if (input.compareTo("0") == 0) input = key;
                        else input += key;
                        operator = true;
                    }
                }
                show.setText(input);
            }
        }
    }
}