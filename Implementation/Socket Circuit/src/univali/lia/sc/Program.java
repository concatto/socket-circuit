package univali.lia.sc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

import univali.lia.sc.lang.LexicalError;
import univali.lia.sc.lang.Lexico;
import univali.lia.sc.lang.SemanticError;
import univali.lia.sc.lang.Semantico;
import univali.lia.sc.lang.Sintatico;
import univali.lia.sc.lang.SyntaticError;
import univali.lia.sc.lang.Token;

public class Program {
	private static final double DEFAULT_VALUE = 0d;
	private HashMap<String, Double> variables = new HashMap<>();
	private Deque<Double> valueStack = new ArrayDeque<>();
	private Deque<String> operationStack = new ArrayDeque<>();
	
	public Program() {
//		try (Socket s = new Socket("localhost", 4000)) {
//			PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
//			for (int k = 0; k < 20; k++) {
//				StringBuilder colorBuilder = new StringBuilder("#");
//				for (int i = 0; i < 6; i++) {
//					colorBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
//				}
//				writer.println(colorBuilder.toString());
//				writer.flush();
//				Thread.sleep(200);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		Lexico lex = new Lexico("a = a + 10 + 5 - 2");
		Sintatico sin = new Sintatico();
		Semantico sem = new Semantico();
		sem.setActionConsumer(this::handleAction);
		try {
			sin.parse(lex, sem);
		} catch (LexicalError | SyntaticError | SemanticError e) {
			e.printStackTrace();
		}
	}
	
	private void handleAction(int action, Token token) {
		switch (action) {
		case 0:
			variables.put(token.getLexeme(), DEFAULT_VALUE);
			break;
		case 2:
			valueStack.push(extractValue(token.getLexeme()));
			break;
		case 3:
			operationStack.push(token.getLexeme());
			break;
		case 4:
			valueStack.push(extractValue(token.getLexeme()));
			System.out.println("Value is " + evaluate());
			break;
		}
	}

	private double evaluate() {
		while (valueStack.size() > 1) {			
			Double b = valueStack.pop();
			Double a = valueStack.pop();
			String op = operationStack.pop();
			
			if (op.equals("+")) {
				valueStack.push(a + b);
			} else if (op.equals("-")) {
				valueStack.push(a - b);
			}
		}
		
		return valueStack.pop();
	}

	private Double extractValue(String lexeme) {
		Double value = variables.get(lexeme);
		if (value == null) {
			if (Character.isDigit(lexeme.charAt(0))) {
				value = Double.parseDouble(lexeme);
			} else {
				System.out.println("Does not exist!");
				value = DEFAULT_VALUE;
			}
		}
		
		return value;
	}

	public static void main(String[] args) {
		new Program();
	}
}
