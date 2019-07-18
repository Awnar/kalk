package com.example.kalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.*;
import android.view.View;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView show, show2;
    private String input = "";
    private int Lbracket = 0, Rbracket = 0;
    private boolean dot = true, operator = false, minus = true;

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
        dot = minus = true;
        operator = false;
    }

    private void calculate() {
        if (input.isEmpty()) return;

        if (!((input.charAt(0) >= '0' && input.charAt(0) <= '9') || input.charAt(0) == '.'
                || input.charAt(0) == '(' || input.charAt(0) == 'E'))
            //jeśli pierwszym znakiem jest operator (nie liczba i nie kropka i nie nawias i nie E)
            //wstaw liczbę z poprzedniego wyniku na początek
            input = show2.getText() + " " + input;
        input = input.replaceFirst(" [ ]+", " ");
        input = input.toLowerCase().replaceAll("mod", "%");

        //łatanie problemów w formatowaniu wykrytych przy testach
        input = input.toLowerCase().replaceAll("-\\(", "- (");
        Matcher matcher = Pattern.compile("(\\)|\\d)(\\s)*\\(").matcher(input);
        while (matcher.find())
            input = input.replace(matcher.group(1) + "(", matcher.group(1) + " * (");
        matcher = Pattern.compile("(^\\)\\w) - (\\d)").matcher(input);
        while (matcher.find())
            input = input.replace(matcher.group(1) + " - " + matcher.group(2),
                    matcher.group(1) + " -" + matcher.group(2));

        String result;
        try {
            result = RPN.calculate(RPN.toRPN(input));
        } catch (NumberFormatException e) {
            if (e.getMessage().contains("Infinity"))
                result = getString(R.string.OVERFLOW);
            else
                result = getString(R.string.NumberFormatException);
        } catch (ArithmeticException e) {
            result = getString(R.string.ArithmeticException);
        } catch (Exception e) {
            result = getString(R.string.error);
        }

        //usuwanie nadmiarowych 0, pustych wyników itd.
        while (result.endsWith("0") && result.contains(".")) result = result.substring(0, result.length() - 1);
        if (result.startsWith("0E") || result.isEmpty()) result = "0";
        if (result.endsWith(".")) result = result.substring(0, result.length() - 1);
        refresh(result);

        Lbracket = Rbracket = 0;
        dot = operator = minus = true;
    }

    private void refresh(String result) {
        show2.setText(input);
        show.setText(result);
        input = "";
    }

    public void onClick(View v) {
        input = input.replaceFirst(" [ ]+", " ");

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
                    input = input.trim();
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
                    if (a >= '0' && a <= '9' || a == ')')
                        input = input.trim();
                }
                if (input.isEmpty())
                    clear();
                else
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
                        if (minus) {
                            operator = false;
                            dot = true;
                            minus = false;
                            if (input.isEmpty()) {
                                if (show2.getText().length() == 0) {
                                    input += "-";
                                    minus = true;
                                } else
                                    input += " - ";
                            } else if (input.charAt(input.length() - 1) > '0'
                                    && input.charAt(input.length() - 1) < '9'
                                    || input.charAt(input.length() - 1) == ')'
                                    || input.charAt(input.length() - 1) == 'E')
                                input += " - ";
                            else {
                                input += " -";
                                minus = true;
                            }
                        }
                        break;
                    }
                    case "E": {
                        if (!input.isEmpty()) {
                            if (input.charAt(input.length() - 1) > '0'
                                    && input.charAt(input.length() - 1) < '9'
                                    || input.charAt(input.length() - 1) == '.'
                                    || input.charAt(input.length() - 1) == 'E')
                                return;
                            if (input.charAt(input.length() - 1) == ')') return;
                        }
                        if (input.compareTo("0") == 0) input = key;
                        else input += key;
                        operator = true;
                        dot = false;
                        break;
                    }
                    default: {
                        if (!input.isEmpty())
                            if (input.charAt(input.length() - 1) == ')'
                                    || input.charAt(input.length() - 1) == 'E')
                                return;
                        if (input.compareTo("0") == 0) input = key;
                        else input += key;
                        operator = minus = true;
                    }
                }
                if (input.isEmpty())
                    show.setText("0");
                else
                    show.setText(input);
            }
        }
    }
}