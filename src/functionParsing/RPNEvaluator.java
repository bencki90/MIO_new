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
			
	private static double getOperand(Stack<String> operandsStack, int operandNumber) throws Exception{
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
		
		if(operandNumber == 1){
			while(!operandsStack.isEmpty()){
				switch(operandsStack.peek().toLowerCase()){
					case "sin":
						operandsStack.pop();
						valueToReturn = Math.sin(valueToReturn);
						break;
					case "cos":
						operandsStack.pop();
						valueToReturn = Math.cos(valueToReturn);
						break;
					case "abs":
						operandsStack.pop();
						valueToReturn = Math.abs(valueToReturn);
						break;
					default:
						return valueToReturn;
				};
			}
		}
		return valueToReturn;
	}
	
	public static double evalRPN(String expr) throws Exception{

		Stack<String> operandsStack = new Stack<String>();
		String[] equationElements = expr.split("\\s");
		
		for(int i = 0; i < equationElements.length; ++i){
			
			String currentElement = equationElements[i].toLowerCase();
			boolean isOperand = false;
			
			switch(currentElement){
				case "e":
				case "pi":
				case "sin":
				case "cos":
				case "abs":
					operandsStack.push(currentElement);
					isOperand = true;
					break;
				default:
					if(NumberUtils.isParsable(currentElement)){
						operandsStack.push(currentElement);
						isOperand = true;
					}
					break;
			};
			
			if(!isOperand){
				
				//Order is fine
				double secondOperand = getOperand(operandsStack, 1);
				double firstOperand = getOperand(operandsStack, 1);
				
				switch(currentElement){
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
		}
		return getOperand(operandsStack, 1);
	}
}