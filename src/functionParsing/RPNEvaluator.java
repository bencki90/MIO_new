package functionParsing;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Stack;

import org.apache.commons.lang3.math.NumberUtils;
 
public class RPNEvaluator{
    public static DecimalFormat df = new DecimalFormat("0");
    public static DecimalFormatSymbols dfs = new DecimalFormatSymbols();

    public static void initDecimalformat(){
            df.setMaximumFractionDigits(340);
            dfs.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(dfs);
    }

    private static double getOperand(Stack<String> operandsStack) throws Exception{
        String operand = operandsStack.pop().toLowerCase();

        double valueToReturn = Double.NaN;

        switch(operand){
        case "e":
            valueToReturn = Math.E;
            break;
        case "pi":
            valueToReturn = Math.PI;
            break;
        default:
            valueToReturn = Double.valueOf(operand);
            break;
        };
        return valueToReturn;
    }

    public static double evalRPN(String expr) throws Exception{
        
        Stack<String> operandsStack = new Stack<String>();
        String[] equationElements = expr.split("\\s");

        for(int i = 0; i < equationElements.length; ++i){

            String currentElement = equationElements[i].toLowerCase().replace(',', '.');

            if(isOperandOrFunction(currentElement)){
                operandsStack.push(currentElement);
            }
            else {
                if(currentElement.equals("endfun")) makeFunction(operandsStack);
                else makeArithmeticOperation(operandsStack, currentElement);
            }
        }
        return getOperand(operandsStack);
    }

    private static boolean isOperandOrFunction(String equationElement){
        switch(equationElement){
            case "e":
            case "pi":
            case "sin":
            case "cos":
            case "abs":
                return true;
            case "endfun":
                return false;
            default:
                if(NumberUtils.isParsable(equationElement)) return true;
                else return false;
        }
    }
    
    private static void makeArithmeticOperation(Stack<String> operandsStack, String operationSymbol) throws Exception{
        double secondOperand = getOperand(operandsStack);
        double firstOperand = getOperand(operandsStack);

        switch(operationSymbol){
            case "+":
                operandsStack.push(df.format(firstOperand + secondOperand));
                break;
            case "-":
                operandsStack.push(df.format(firstOperand - secondOperand));
                break;
            case "*":
                operandsStack.push(df.format(firstOperand * secondOperand));
                break;
            case "/":
                operandsStack.push(df.format(firstOperand / secondOperand));
                break;
            case "^":
                operandsStack.push(df.format(Math.pow(firstOperand, secondOperand)));
                break;
        };
    }

    private static void makeFunction(Stack<String> operandsStack) throws Exception {
        double operand = getOperand(operandsStack);
        
        switch(operandsStack.pop()){
            case "sin":
                operandsStack.push(df.format(Math.sin(operand)));
                break;
            case "cos":
                operandsStack.push(df.format(Math.cos(operand)));
                break;
            case "abs":
                operandsStack.push(df.format(Math.abs(operand)));
                break;
            default:
                throw new Exception("Stack is broken! -> makeFunction");
        }
    }
}