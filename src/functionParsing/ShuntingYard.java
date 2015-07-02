package functionParsing;

import java.util.Stack;
 
public class ShuntingYard {
 
    public static String infixToPostfix(String infix) throws Exception {
        if(infix == null || infix.isEmpty()) throw new Exception();
        
        
        final String ops = "-+/*^";
        StringBuilder sb = new StringBuilder();
        Stack<Integer> s = new Stack<>();
 
        for (String token : infix.split("\\s")) {
            if (token.isEmpty()) continue;
            
            int idx = -1;
            
            char c = token.charAt(0);
            if(token.length() == 1) idx = ops.indexOf(c);
 
            // check for operator
            if (idx != -1) {
                if (s.isEmpty()) s.push(idx);
 
                else {
                    while (!s.isEmpty()) {
                        int prec2 = s.peek() / 2;
                        int prec1 = idx / 2;
                        if (prec2 > prec1 || (prec2 == prec1 && c != '^')) sb.append(ops.charAt(s.pop())).append(' ');
                        else break;
                    }
                    s.push(idx);
                }
            } 
            else if (c == '(') {
                s.push(-2); // -2 stands for '('
            } 
            else if (c == ')') {
                // until '(' on stack, pop operators.
                while (s.peek() != -2)
                    sb.append(ops.charAt(s.pop())).append(' ');
                s.pop();
            }
            else {
                sb.append(token).append(' ');
            }
        }
        while (!s.isEmpty())
            sb.append(ops.charAt(s.pop())).append(' ');
        return sb.toString();
    }
}