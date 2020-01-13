package hr.fer.tinf.lab.zad3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
	
	private static Map<String, BinaryBlockCode> variables = new HashMap<>();

	/**
	 * Method used for retrieving content of src/main/resources/README.txt
	 * @return content of src/main/resources/README.txt
	 */
	public static String getHelp() {
		try {
			List<String> lines = Files.readAllLines(
				Paths.get("src/main/resources/README.txt")
			);
			return String.join("\n", lines);
		} catch (NoSuchFileException nsfe) {
			return "Could not find src/main/resources/README.txt. Make sure cwd"
					+ " is project root";
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * Main method that includes main loop from which all the methods are called
	 * @param arguments program arguments (ignored)
	 */
    public static void main(String... arguments) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome! For help enter \"help\"");
        while (true) {
        	System.err.flush();
            System.out.print(">");
            System.out.flush();
            String commandString = sc.nextLine();
            if ("exit".equals(commandString.toLowerCase())) break;
            if ("".equals(commandString.trim())) continue;
            if ("help".equals(commandString.toLowerCase())) {
            	System.out.println(getHelp());
            	continue;
            }
            
            String[] splitCommand = commandString.split(" ");
            int pointIndex = splitCommand[0].indexOf('.');
            
            if (pointIndex == -1) {
        		System.err.println(
        			"Error: first token has to be 'variable.method'"
				);
        		continue;
            }
            String variable = splitCommand[0].substring(0, pointIndex);
            String method = splitCommand[0].substring(pointIndex + 1);
            
            if ("new".equals(method)) {
            	if (splitCommand.length > 1) {
            		try {
            			int n = Integer.parseInt(splitCommand[1]);
            			variables.put(variable, new BinaryBlockCode(n));
            		} catch (NumberFormatException nfe) {
            			System.err.println(
        					"Error: Block size has to be a natural number"
    					);
            		} catch (IllegalArgumentException iae) {
            			System.err.println("Error: " + iae.getMessage());
            		}
            	} else {
            		System.err.println("Error: please define block size");
            	}
            } else if ("delete".equals(method)) {
            	variables.remove(variable);
        	} else {
            	if (variables.containsKey(variable)) {
            		String[] args = Arrays.copyOfRange(
        				splitCommand, 1, splitCommand.length
    				);
            		String msg = call(variables.get(variable), method, args);
            		if (msg.startsWith("Error: ")) {
            			System.err.println(msg);
            		} else {
            			System.out.println(msg);
            		}
            	} else {
            		System.err.println(
            			"Error: Variable not defined. Please use " + variable +
            			".new n to define a block code of size n"
    				);
            	}
            }
            
        }
        sc.close();
    }

	/**
	 * Handles method calls
	 * @param bbc binary block code whose method is being called
	 * @param method method name or abbreviation
	 * @param args arguments for the method
	 * @return Feedback for user about command execution
	 */
	public static String call(
		BinaryBlockCode bbc, String method, String... args
	) {
		BinaryVector cw;
		StringBuilder sb;
		try {
			switch (method) {
			case "n":
				return "n: " + bbc.getN();
			case "k":
				return "k: " + bbc.getK();
			case "sa":
			case "standardArray":
				BinaryVector[][] sa = bbc.getStandardArray();
				sb = new StringBuilder();
				for (BinaryVector[] bva : sa) {
					sb.append(Arrays.toString(bva));
					sb.append('\n');
				}
				sb.deleteCharAt(sb.length() - 1);
				return sb.toString();
			case "decode":
				if (args == null || args.length == 0) {
					throw new IllegalArgumentException(
						"No codeword provided for decoding."
					);
				}
				cw = new BinaryVector(args[0]);
				return bbc.decode(cw);
			case "add":
				if (args == null || args.length < 1) {
					throw new IllegalArgumentException(
						"Please define a codeword to add."
					);
				}
				sb = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					String[] temp = { args[i], null };
					if (args[i].contains(":")) {
						temp = args[i].split(":");
					}
					try {
						cw = new BinaryVector(temp[0]);
						bbc.addCodeWord(cw, temp[1]);
						sb.append("Codeword " + cw + " successfully added.");
						if (i != args.length - 1) sb.append('\n');
					} catch (RuntimeException re) {
						throw new RuntimeException(
							sb.toString() + re.getMessage(), re
						);
					}
				}
				return sb.toString();
			case "remove":
				if (args == null || args.length < 1) {
					throw new IllegalArgumentException(
						"Please define a codeword to remove."
					);
				}
				sb = new StringBuilder();
				for (String arg : args) {
					try {
						cw = new BinaryVector(arg);
						bbc.removeCodeword(cw);
						sb.append("Codeword " + cw + " successfully removed.\n");
					} catch (RuntimeException re) {
						throw new RuntimeException(
							sb.toString() + re.getMessage(), re
						);
					}
				}
				sb.deleteCharAt(sb.length() - 1);
				return sb.toString();
			case "linear":
				return "The code is" + (bbc.isLinear() ? "" : "n't")
					+ " linear.";
			case "p":
				if (args == null || args.length == 0) {
					throw new IllegalArgumentException(
						"No codeword provided for decoding."
					);
				}
				double p = Double.parseDouble(args[0]);
				return "Correct decoding probability: "
					+ bbc.correctDecodingProbability(p);
			case "t":
				return "The code can correct " + bbc.getT() + " error(s).";
			case "distance":
			case "dist":
			case "d":
				return "Code distance: " + bbc.distance();
			case "print":
				return bbc.toString();
			case "safety":
				return (bbc.toggleSafety() ? "Safe" : "Unsafe") + " decoding";
			default:
				return "Error: Method " + method + " is not implemented.";
			}
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}
}
