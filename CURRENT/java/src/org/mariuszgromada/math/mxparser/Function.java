/*
 * @(#)Function.java        1.1.0    2015-12-24
 * 
 * You may use this software under the condition of "Simplified BSD License"
 * 
 * Copyright 2010 MARIUSZ GROMADA. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY <MARIUSZ GROMADA> ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of MARIUSZ GROMADA.
 * 
 * If you have any questions/bugs feel free to contact:
 * 
 *     Mariusz Gromada
 *     mariusz.gromada@wp.pl
 *     http://mathspace.plt/
 *     http://mxparser.sourceforge.net/
 * 
 *                              Asked if he believes in one God, a mathematician answered: 
 *                              "Yes, up to isomorphism."  
 */ 

package org.mariuszgromada.math.mxparser;

import java.util.ArrayList;



/**
 * Function class provides possibility to define user functions.
 * Functions can be used in further processing by any expression,
 * dependent or recursive argument, function, etc... For expamle:
 * 
 * <ul>
 * <li>'f(x) = sin(x)'
 * <li>'g(x,y) = sin(x)+cos(y)'
 * <li>'h(x,y = f(x)+g(y,x)'
 * <li>in general 'f(x1,x2,...,xn)' where x1,...,xn are arguments
 * </ul>
 * 
 * <p>
 * When creating a function you should avoid names reserved as
 * parser keywords, in general words known in mathematical language
 * as function names, operators (for example:
 * sin, cos, +, -, pi, e, etc...). Please be informed that after associating 
 * the constant with the expression, function or dependent/recursive argument
 * its name will be recognized by the parser as reserved key word.
 * It means that it could not be the same as any other key word known
 * by the parser for this particular expression.
 * 
 * @author         <b>Mariusz Gromada</b><br/>
 *                 <a href="mailto:"></a><br>
 *                 <a href="http://mathspace.plt/">http://mathspace.plt/</a><br>
 *                 <a href="http://mxparser.sourceforge.net/">http://mxparser.sourceforge.net/</a><br>
 *                         
 * @version        1.0.2
 * 
 * @see RecursiveArgument
 * @see Expression
 * @see Argument
 * @see Constant
 *
 */
public class Function {

	/**
	 * No syntax errors in the function.
	 */
	public static final boolean NO_SYNTAX_ERRORS = Expression.NO_SYNTAX_ERRORS;
	
	/**
	 * Syntax error in the function or syntax status unknown.
	 */
	public static final boolean SYNTAX_ERROR_OR_STATUS_UNKNOWN = Expression.SYNTAX_ERROR_OR_STATUS_UNKNOWN;
	
	/**
	 * When function was not found
	 */
	public static final int NOT_FOUND = Expression.NOT_FOUND;
	
	
	/**
	 * Function type id identifier
	 */
	static final int TYPE_ID	=	103;

	/**
	 * function expression
	 */
	Expression functionExpression;
	
	/**
	 * function name
	 */
	private String functionName;
	
	/**
	 * function description
	 */
	private String description;
	

	/**
	 * The number of function parameters
	 */
	private int parametersNumber;
	
	/*=================================================
	 * 
	 * Constructors
	 * 
	 *================================================= 
	 */	
	
	/**
	 * Constructor - creates function from function name
	 * and function expression string.
	 * 
	 * @param      functionName              the function name
	 * @param      functionExpressionString  the function expression string
	 * 
	 * @see        Expression
	 */
	public Function(String functionName
					,String  functionExpressionString ) {
		
		this.functionName = functionName;
		functionExpression = new Expression(functionExpressionString);
		functionExpression.setDescription(functionName);
		
		parametersNumber = 0;
		description = "";
		
	}

	/**
	 * Constructor - creates function from function name,
	 * function expression string and argument names.
	 * 
	 * @param      functionName              the function name
	 * @param      functionExpressionString  the function expression string
	 * @param      argumentsNames            the arguments names (variadic parameters)
	 *                                       comma separated list
	 *                                  
	 * @see        Expression
	 */
	public Function(String functionName
					,String  functionExpressionString
					,String... argumentsNames ) {
		
		this.functionName = functionName;
		functionExpression = new Expression(functionExpressionString);
		functionExpression.setDescription(functionName);
		
		for (String argName : argumentsNames)
			functionExpression.addArguments(new Argument(argName));
		
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
		description = "";
		
	}
	
		
	/**
	 * Constructor - creates function from function name,
	 * function expression string and arguments.
	 * 
	 * @param      functionName                  the function name
	 * @param      functionExpressionString      the function expression string
	 * @param      arguments                     the arguments (variadic parameters)
	 *                                           comma separated list
	 *                                  
	 * @see        Expression
	 */
	public Function(String functionName
					,String  functionExpressionString
					,Argument... arguments ) {
		
		this.functionName = functionName;
		functionExpression = new Expression(functionExpressionString);
		functionExpression.setDescription(functionName);
		
		for (Argument argument : arguments)
			functionExpression.addArguments(argument);
		
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
		
	}
	
	/**
	 * Constructor for function definition in natural math language,
	 * for instance providing on string "f(x,y) = sin(x) + cos(x)"
	 * is enough to define function "f" with parameters "x and y"
	 * and function body "sin(x) + cos(x)".
	 * 
	 * @param functionDefinitionString      Function definition in the form
	 *                                      of one String, ie "f(x,y) = sin(x) + cos(x)"
	 */
	public Function(String functionDefinitionString) {
		
		HeadEqBody headEqBody = new HeadEqBody(functionDefinitionString);
		parametersNumber = 0;
		boolean definitionError = true;
		
		
		if ( (headEqBody.definitionError == false) && (headEqBody.headTokens.size() > 1) ) {
				
			this.functionName = headEqBody.headTokens.get(0).tokenStr;
			functionExpression = new Expression(headEqBody.bodyStr);
			functionExpression.setDescription(headEqBody.headStr);
				
			if (headEqBody.headTokens.size() > 1) {
				Token t;
				for (int i = 1; i < headEqBody.headTokens.size(); i++) {
					t = headEqBody.headTokens.get(i);
					if (t.tokenTypeId != ParserSymbol.TYPE_ID)
						functionExpression.addArguments(new Argument(t.tokenStr));							
				}
				
			}
				
			parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
			description = "";
			if (parametersNumber > 0) definitionError = false;
				
								
		} else functionExpression = new Expression();
		
		if (definitionError == true) {
			functionExpression.setDescription(functionDefinitionString);
			functionExpression.setSyntaxStatus(Expression.SYNTAX_ERROR_OR_STATUS_UNKNOWN, "Definition error in user defined function (missing function name or missing parameters or error in function body).");
		}
		
	}
	
	/**
	 * Private constructor used for function cloning.
	 * 
	 * @param      function            the function, which is going
	 *                                 to be cloned.
	 */
	private Function(Function function) {
		
		functionName = function.functionName;
		description = function.description;
		parametersNumber = function.parametersNumber;
		functionExpression = function.functionExpression.clone();		
		
	}
	
	
	/**
	 * Sets function description.
	 * 
	 * @param      description         the function description
	 */
	public void setDescription(String description) {
		
		this.description = description;
		
	}
	
	/**
	 * Gets function description
	 * 
	 * @return     Function description as string.
	 */
	public String getDescription() {
		
		return description;
		
	}
	
	/**
	 * Gets function name.
	 * 
	 * @return     Function name as string.
	 */
	public String getFunctionName() {
		
		return functionName;
		
	}


	/**
	 * Gets function expression string
	 * 
	 * @return     Function expression as string.
	 */
	public String getFunctionExpressionString() {
		
		return functionExpression.getExpressionString();
		
	}

	
	/**
	 * Sets function name.
	 * 
	 * @param      functionName        the function name
	 */
	public void setFunctionName(String functionName) {
		
		this.functionName = functionName;
		setExpressionModifiedFlags();
		
	}	
	
	/**
	 * Sets value of function argument (function parameter).
	 * 
	 * @param      argumentIndex   the argument index (in accordance to
	 *                             arguments declaration sequence)
	 * @param      argumentValue   the argument value
	 */
	public void setArgumentValue(int argumentIndex, double argumentValue) {
		
		functionExpression.argumentsList.get(argumentIndex).argumentValue = argumentValue;
		
	}
	
	
	/**
	 * Checks function syntax
	 * 
	 * @return     syntax status: Function.NO_SYNTAX_ERRORS,
	 *                            Function.SYNTAX_ERROR_OR_STATUS_UNKNOWN
	 * 
	 */
	public boolean checkSyntax() {
		
		return functionExpression.checkSyntax();
		
	}
	
	
	/**
	 * Returns error message after checking the syntax.
	 * 
	 * @return     Error message as string.
	 */
	public String getErrorMessage() {
		
		return functionExpression.getErrorMessage();
		
	}
	
	
	/**
	 * clone method
	 */
	protected Function clone() {
	
		Function newFunction = new Function(this);
		
		return newFunction;
		
	}
	
	/**
	 * Calculates function value
	 * 
	 * @return     Function value as double.
	 */
	public double calculate() {
		
		return functionExpression.calculate();
	}	
	
	/**
	 * Calculates function value
	 * 
	 * @param      params              the function parameters values (as doubles)
	 * 
	 * @return     function value as double.
	 */
	public double calculate(double... params) {
		
		if (params.length <= this.getParametersNumber()) {
				
			
			for (int p = 0; p < params.length; p++)
				setArgumentValue(p, params[p]);
							
			return  calculate();
			
		}
		else
			return Double.NaN;
		
	}		

	/**
	 * Calculates function value
	 * 
	 * @param      arguments   function parameters (as Arguments)
	 * 
	 * @return     function value as double
	 */
	public double calculate(Argument... arguments) {
		
		if (arguments.length <= this.getParametersNumber()) {
				
			
			for (int p = 0; p < arguments.length; p++)
				setArgumentValue(p, arguments[p].getArgumentValue());
							
			return  calculate();
			
		}
		else
			return Double.NaN;
		
	}
	
	/*=================================================
	 * 
	 * Arguments handling API (the same as in Expression)
	 * (protected argument expression)
	 * 
	 *================================================= 
	 */
	
	private int countRecursiveArguments() {
		
		int numOfRecursiveArguments = 0;
		
		for (Argument argument : functionExpression.argumentsList)
			if (argument.getArgumentType() == Argument.RECURSIVE_ARGUMENT) numOfRecursiveArguments++;
		
		return numOfRecursiveArguments;
		
	}
	
	/**
	 * Adds arguments (variadic) to the function expression definition.
	 * 
	 * @param      arguments           the arguments list
	 *                                 (comma separated list)
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void addArguments(Argument... arguments) {
		
		functionExpression.addArguments(arguments);
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
		
	}
	
	/**
	 * Adds arguments to the function expression definition.
	 * 
	 * @param      argumentsList       the arguments list
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void addArguments(ArrayList<Argument> argumentsList) {
		
		functionExpression.addArguments(argumentsList);
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
		
	}
	

	/**
	 * Enables to define the arguments (associated with
	 * the function expression) based on the given arguments names.
	 * 
	 * @param      argumentsNames      the arguments names (variadic)
	 *                                 comma separated list
	 *                       
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void defineArguments(String... argumentsNames) {
		
		functionExpression.defineArguments(argumentsNames);
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();

	}
	
	/**
	 * Enables to define the argument (associated with the function expression)
	 * based on the argument name and the argument value.
	 * 
	 * @param      argumentName        the argument name
	 * @param      argumentValue       the the argument value
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void defineArgument(String argumentName, double argumentValue) {
		
		functionExpression.defineArgument(argumentName, argumentValue);
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();

	}
	

	
	/**
	 * Gets argument index from the function expression. 
	 * 
	 * @param      argumentName        the argument name
	 * 
	 * @return     The argument index if the argument name was found,
	 *             otherwise returns Argument.NOT_FOUND
	 *                       
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public int getArgumentIndex(String argumentName) {
		
		return functionExpression.getArgumentIndex(argumentName);
		
	}
	
	/**
	 * Gets argument from the function expression. 
	 * 
	 * 
	 * @param      argumentName        the argument name
	 * 
	 * @return     The argument if the argument name was found,
	 *             otherwise returns null.
	 *                       
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public Argument getArgument(String argumentName) {
		
		return functionExpression.getArgument(argumentName);
		
	}

	/**
	 * Gets argument from the function expression.
	 * 
	 * @param      argumentIndex       the argument index
	 * 
	 * @return     Argument if the argument index is between 0 and 
	 *             the last available argument index (getArgumentsNumber()-1),
	 *             otherwise returns null.
	 *                        
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public Argument getArgument(int argumentIndex) {
		
		return functionExpression.getArgument(argumentIndex);
		
	}
	
	/**
	 * Gets number of parameters associated with the function expression.
	 * 
	 * @return     The number of function parameters (int >= 0)
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public int getParametersNumber() {
		
		return parametersNumber;
		
	}

	/**
	 * Set parameters number.
	 * 
	 * @param      parametersNumber    the number of function parameters (default = number of arguments
	 *                                 (less number might be specified).
	 */
	public void setParametersNumber(int parametersNumber) {
		
		this.parametersNumber = parametersNumber;
		functionExpression.setExpressionModifiedFlag();
		
	}
	
	/**
	 * Gets number of arguments associated with the function expression.
	 * 
	 * @return     The number of arguments (int >= 0)
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public int getArgumentsNumber() {
		
		return functionExpression.getArgumentsNumber();
		
	}
	
	/**
	 * Removes first occurrences of the arguments
	 * associated with the function expression.
	 * 
	 * @param      argumentsNames      the arguments names
	 *                                 (variadic parameters) comma separated
	 *                                 list
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void removeArguments(String... argumentsNames) {
		
		functionExpression.removeArguments(argumentsNames);
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
		
	}

	/**
	 * Removes first occurrences of the arguments
	 * associated with the function expression.
	 * 
	 * @param      arguments           the arguments (variadic parameters)
	 *                                 comma separated list
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void removeArguments(Argument... arguments) {
		
		functionExpression.removeArguments(arguments);
		parametersNumber = functionExpression.getArgumentsNumber() - countRecursiveArguments();
		
	}
	
	/**
	 * Removes all arguments associated with the function expression.
	 * 
	 * @see        Argument
	 * @see        RecursiveArgument
	 */
	public void removeAllArguments() {
		
		functionExpression.removeAllArguments();
		parametersNumber = 0;
		
	}
	
	/*=================================================
	 * 
	 * Constants handling API (the same as in Expression)
	 * (protected argument expression)
	 * 
	 *================================================= 
	 */	
	
	/**
	 * Adds constants (variadic parameters) to the function expression definition.
	 * 
	 * @param      constants           the constants
	 *                                 (comma separated list)
	 *                    
	 * @see        Constant
	 */
	public void addConstants(Constant... constants) {
		
		functionExpression.addConstants(constants);
		
	}
	
	/**
	 * Adds constants to the function expression definition.
	 * 
	 * @param      constantsList       the list of constants
	 * 
	 * @see        Constant
	 */
	public void addConstants(ArrayList<Constant> constantsList) {
		
		functionExpression.addConstants(constantsList);
		
	}
	
	/**
	 * Enables to define the constant (associated with 
	 * the function expression) based on the constant name and
	 * constant value.
	 * 
	 * @param      constantName        the constant name
	 * @param      constantValue       the constant value
	 * 
	 * @see        Constant
	 */
	public void defineConstant(String constantName, double constantValue) {
		
		functionExpression.defineConstant(constantName, constantValue);
		
	}
	
	/**
	 * Gets constant index associated with the function expression.
	 * 
	 * @param      constantName        the constant name
	 * 
	 * @return     Constant index if constant name was found,
	 *             otherwise return Constant.NOT_FOUND.
	 * 
	 * @see        Constant
	 */
	public int getConstantIndex(String constantName) {
		
		return functionExpression.getConstantIndex(constantName);
		
	}
	
	/**
	 * Gets constant associated with the function expression.
	 * 
	 * @param      constantName        the constant name
	 * 
	 * @return     Constant if constant name was found,
	 *             otherwise return null.
	 * 
	 * @see        Constant
	 */
	public Constant getConstant(String constantName) {
		
		return functionExpression.getConstant(constantName);
		
	}
	
	/**
	 * Gets constant associated with the function expression.
	 *  
	 * @param      constantIndex       the constant index
	 * 
	 * @return     Constant if the constantIndex is between
	 *             0 and the last available constant index
	 *             (getConstantsNumber() - 1),
	 *             otherwise it returns null.
	 * 
	 * @see        Constant
	 */
	public Constant getConstant(int constantIndex) {
		
		return functionExpression.getConstant(constantIndex);
		
	}
	
	/**
	 * Gets number of constants associated with the function expression.
	 *  
	 * @return     number of constants (int >= 0)
	 * 
	 * @see        Constant
	 */
	public int getConstantsNumber() {
		
		return functionExpression.getConstantsNumber();
		
	}
	
	/**
	 * Removes first occurrences of the constants
	 * associated with the function expression.
	 * 
	 * @param      constantsNames      the constants names (variadic parameters)
	 *                                 comma separated list
	 * 
	 * @see        Constant
	 */
	public void removeConstants(String... constantsNames) {
		
		functionExpression.removeConstants(constantsNames);
		
	}
	
	/**
	 * Removes first occurrences of the constants
	 * associated with the function expression
	 * 
	 * @param      constants           the constants (variadic parameters)
	 *                                 comma separated list
	 * 
	 * @see        Constant
	 */
	public void removeConstants(Constant... constants) {
		
		functionExpression.removeConstants(constants);
		
	}
	
	/**
	 * Removes all constants
	 * associated with the function expression
	 *
	 * @see        Constant
	 */
	public void removeAllConstants() {
		
		functionExpression.removeAllConstants();
		
	}
	
	/*=================================================
	 * 
	 * Functions handling API (the same as in Expression)
	 * (protected argument expression)
	 * 
	 *================================================= 
	 */
	
	/**
	 * Adds functions (variadic parameters) to the function expression definition.
	 * 
	 * @param      functions           the functions
	 *                                 (variadic parameters) comma separated list
	 * 
	 * @see        Function
	 */
	public void addFunctions(Function... functions) {
		
		functionExpression.addFunctions(functions);
		
	}
	
	/**
	 * Adds functions to the function expression definition.
	 * 
	 * @param      functionsList       the functions list
	 * 
	 * @see        Function
	 */
	public void addFunctions(ArrayList<Function> functionsList) {	
		
		functionExpression.addFunctions(functionsList);
		
	}
	
	/**
	 * Enables to define the function (associated with 
	 * the function expression) based on the function name,
	 * function expression string and arguments names (variadic parameters).
	 * 
	 * @param      functionName                  the function name
	 * @param      functionExpressionString      the expression string
	 * @param      argumentsNames                the function arguments names
	 *                                           (variadic parameters)
	 *                                           comma separated list
	 * 
	 * @see        Function
	 */
	public void defineFunction(String functionName, String  functionExpressionString,
			String... argumentsNames) {
		
		functionExpression.defineFunction(functionName, functionExpressionString, argumentsNames);
		
	}
	
	/**
	 * Gets index of function associated with the function expression.
	 *  
	 * @param      functionName        the function name
	 * 
	 * @return     Function index if function name was found,
	 *             otherwise returns Function.NOT_FOUND
	 * 
	 * @see        Function
	 */
	public int getFunctionIndex(String functionName) {
		
		return functionExpression.getFunctionIndex(functionName);
		
	}
	
	/**
	 * Gets function associated with the function expression.  
	 * 
	 * @param      functionName        the function name
	 * 
	 * @return     Function if function name was found,
	 *             otherwise returns null.
	 * 
	 * @see        Function
	 */
	public Function getFunction(String functionName) {
		
		return functionExpression.getFunction(functionName);
		
	}
	
	/**
	 * Gets function associated with the function expression.
	 * 
	 * @param      functionIndex the function index
	 * 
	 * @return     Function if function index is between 0 and 
	 *             the last available function index (getFunctionsNumber()-1),
	 *             otherwise returns null.
	 * 
	 * @see        Function
	 */
	public Function getFunction(int functionIndex) {
		
		return functionExpression.getFunction(functionIndex);
		
	}
	
	/**
	 * Gets number of functions associated with the function expression.
	 *  
	 * @return     number of functions (int >= 0)
	 * 
	 * @see        Function
	 */
	public int getFunctionsNumber() {
		
		return functionExpression.getFunctionsNumber();
		
	}
	
	/**
	 * Removes first occurrences of the functions
	 * associated with the function expression.
	 * 
	 * @param      functionsNames      the functions names (variadic parameters)
	 *                                 comma separated list
	 * 
	 * @see        Function
	 */
	public void removeFunctions(String... functionsNames) {
		
		functionExpression.removeFunctions(functionsNames);
		
	}
	
	/**
	 * Removes first occurrences of the functions
	 * associated with the function expression.
	 * 
	 * @param      functions           the functions (variadic parameters)
	 *                                 comma separated list.
	 * 
	 * @see        Function
	 */
	public void removeFunctions(Function... functions) {
		
		functionExpression.removeFunctions(functions);
		
	}
	
	/**
	 * Removes all functions
	 * associated with the function expression.
	 * 
	 * @see        Function
	 */
	public void removeAllFunctions() {
		
		functionExpression.removeAllFunctions();
		
	}
	
	/*
	 * ---------------------------------------------
	 */

	
	/**
	 * Enables verbose function mode
	 */
	public void setVerboseMode() {
		functionExpression.setVerboseMode();
	}
	
	/**
	 * Disables function verbose mode (sets default silent mode)
	 */
	public void setSilentMode() {
		functionExpression.setSilentMode();
	}
	
	/**
	 * Returns verbose mode status
	 * 
	 * @return     true if verbose mode is on,
	 *             otherwise returns false
	 */
	public boolean getVerboseMode() {
		
		return functionExpression.getVerboseMode();
		
	}	
	
	
	/**
	 * Sets recursive mode
	 */
	public void setRecursiveMode() {

		functionExpression.setRecursiveMode();
		addFunctions(this);
		
	}
	
	/**
	 * Disables recursive mode 
	 */
	public void disableRecursiveMode() {

		functionExpression.disableRecursiveMode();
		removeFunctions(this);
		
	}
	
	/**
	 * Gets recursive mode status
	 * 
	 * @return     true if recursive mode is enabled,
	 *             otherwise returns false
	 * 
	 */
	public boolean getRecursiveMode() {

		return functionExpression.getRecursiveMode();

	}		
	
	/**
	 * Gets computing time
	 * 
	 * @return     computing time in seconds.
	 */
	public double getComputingTime() {

		return functionExpression.getComputingTime();

	}		
	
	/**
	 * Adds related expression.
	 * 
	 * @param      expression          the related expression
	 */
	void addRelatedExpression(Expression expression) {
		
		functionExpression.addRelatedExpression(expression);
		
	}
	
	/**
	 * Removes related expression.
	 * 
	 * @param      expression          the related expression
	 */
	void removeRelatedExpression(Expression expression) {
		
		functionExpression.removeRelatedExpression(expression);
		
	}
	
	/**
	 * Set expression modified flags in the related expressions.
	 */
	void setExpressionModifiedFlags() {
		
		functionExpression.setExpressionModifiedFlag();
		
	}
	
	/**
	 * Gets license info
	 * 
	 * @return     license info as string
	 */
	public String getLicense() {
		
		return mXparser.LICENSE;
		
	}		

	
}