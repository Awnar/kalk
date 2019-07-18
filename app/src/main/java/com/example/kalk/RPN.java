package com.example.kalk;

import java.math.*;
import java.lang.String;
import java.util.*;

class RPN {
    //tablica z priorytetami operatorów
    public static Map<String, Integer> precedence_tab = new HashMap<String, Integer>() {{
        put("%", 7);
        put("^", 6);
        put("*", 5);
        put("/", 4);
        put("-", 2);
        put("+", 1);
    }};

    //porównywanie operatorów
    private static boolean isHigerPrec(String op, String sub) {
        return (precedence_tab.containsKey(sub) && precedence_tab.get(sub) >= precedence_tab.get(op));
    }

    //konwersja na postać RPN
    public static String[] toRPN(String in) {
        LinkedList<String> output = new LinkedList<>();//stos wyjściowy
        LinkedList<String> stack = new LinkedList<>();//stos tymczasowy dla operatorów
        for (String token : in.split(" ")) {
            if (precedence_tab.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrec(token, stack.peek()))
                    output.add(stack.pop());
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.peek().equals("("))
                    output.add(stack.pop());
                stack.pop();
            } else {
                output.add(token);
            }
        }
        while (!stack.isEmpty())
            output.add(stack.pop());
        return output.toArray(new String[output.size()]);
    }

    //obliczenia na podstawie tablicy z RPN
    public static String calculate(String[] in) {
        LinkedList<String> list = new LinkedList<String>(Arrays.asList(in));
        while (list.size() > 2) {
            for (int i = 2; i < list.size(); i++) {
                String token = list.get(i);
                if (precedence_tab.containsKey(token)) {
                    token = calc(token, list.get(i - 2), list.get(i - 1));
                    list.remove(i);
                    list.remove(i - 1);
                    list.remove(i - 2);
                    if (list.isEmpty())
                        list.add(token);
                    else {
                        list.add(i - 2, token);
                        break;
                    }
                }
            }
        }
        if (list.getFirst().equals("e")) return Double.toString(Math.E);
        return list.getFirst();
    }

    //obliczenia na liczbach
    private static String calc(String op, String L1, String L2) {
        MathContext mc = new MathContext(18, RoundingMode.HALF_UP);
        BigDecimal big, big2;

        if (L1.equals("e")) big = new BigDecimal(Math.E);
        else big = new BigDecimal(L1);

        if (L2.equals("e")) big2 = new BigDecimal(Math.E);
        else big2 = new BigDecimal(L2);

        switch (op) {
            case "+": {
                big = big.add(big2, mc);
                break;
            }
            case "-": {
                big = big.subtract(big2, mc);
                break;
            }
            case "*": {
                big = big.multiply(big2, mc);
                break;
            }
            case "/": {
                big = big.divide(big2, mc);
                break;
            }
            case "^": {
                //BigDecimal nie można podnieść do potęgi zmiennoprzecinkowej
                big = new BigDecimal(Math.pow(big.doubleValue(), big2.doubleValue())).round(mc);
                //big = big.pow(new Integer(L2), mc);
                break;
            }
            case "%": {
                big = big.remainder(big2, mc);
                break;
            }
        }
        return big.toString();
    }
}