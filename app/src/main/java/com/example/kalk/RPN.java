package com.example.kalk;

import java.math.*;
import java.util.*;

class RPN {
    private static Map<String,Integer> precedence_tab = new HashMap<String,Integer>(){{
        put("%",7);
        put("^",6);
        put("*",5);
        put("/",4);
        put("+",2);
        put("-",1);
    }};

    private static boolean isHigerPrec(String op, String sub) {
        return (precedence_tab.containsKey(sub) && precedence_tab.get(sub) >= precedence_tab.get(op));
    }

    public static String[] toRPN(String in) {
        LinkedList<String> output = new LinkedList<>();
        LinkedList<String> stack = new LinkedList<>();
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

    public static String calculate(String[] in) {
        LinkedList<String> list = new LinkedList<String>(Arrays.asList(in));
        while (list.size() > 1) {
            for (int i = 2; i < list.size(); i++) {
                String token = list.get(i);
                if (precedence_tab.containsKey(token)) {
                    try {
                        token = calc(token, list.get(i - 2), list.get(i - 1));
                        list.remove(i);
                        list.remove(i - 1);
                        list.remove(i - 2);
                        if (list.isEmpty())
                            list.add(token);
                        else
                            list.add(i-2, token);
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
            }
        }
        return list.getFirst();
    }

    private static String calc(String op, String L1, String L2){
        MathContext mc = new MathContext(15, RoundingMode.HALF_UP);
        BigDecimal big;
        if (L1.equals("e")) big = new BigDecimal(Math.E);
        else big = new BigDecimal(L1);
        BigDecimal big2;
        if (L2.equals("e")) big2 = new BigDecimal(Math.E);
        else big2 = new BigDecimal(L2);
        switch (op){
            case "+":{
                big = big.add(big2, mc);
                break;
            }
            case "-":{
                big = big.subtract(big2, mc);
                break;
            }
            case "*":{
                big = big.multiply(big2, mc);
                break;
            }
            case "/":{
                big = big.divide(big2, mc);
                break;
            }
            case "^":{
                big = big.pow(new Integer(L2), mc);
                break;
            }
            case "%":{
                big = big.remainder(big2, mc);
                break;
            }
        }
        return  big.toString();
    }
}
