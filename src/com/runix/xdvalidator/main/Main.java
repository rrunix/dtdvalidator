package com.runix.xdvalidator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.DTDBuilder;
import com.runix.xdvalidator.dtd.dumper.XMLToDTDDumper;
import com.runix.xdvalidator.xml.validator.module.XMLValidation;
import com.runix.xdvalidator.xml.validator.module.XMLValidatorBuilder;
import com.runix.xdvalidator.xml.validator.module.node.Node;

public class Main {

	private static List<String> availableArgs = new ArrayList<String>();

	static {
		availableArgs.add("-dtd");
		availableArgs.add("-xml");
		availableArgs.add("-file");
	}

	public static void main(String[] args) throws FileNotFoundException,
			ValidationException {
		execute(new String[] { "-file", "/home/runix/xml/data_all.txt" });

	}

	public static void execute(String[] args) throws ValidationException {
		Map<String, String> map = paramsMap(args);
		System.out.println(Arrays.toString(args));
		if (map.containsKey("-file") && map.size() == 1) {
			runFile(map.get("-file"));
		} else if (map.containsKey("-xml") && map.containsKey("-dtd")) {
			run(map.get("-xml"), map.get("-dtd"));
		} else if (map.containsKey("-xml") && map.size() == 1) {
			runXML(map.get("-xml"));
		} else {
			throw new ValidationException(
					"Command not valid; availables are : [ -file file_with_commands; -xml file_xml; -xml file_xml -dtd file_dtd");
		}

	}

	public static void runFile(String file) throws ValidationException {
		String fileAsTriString = asString(file);
		String[] line = fileAsTriString.split("\n");
		for (String s : line) {
			if (s.trim().startsWith("#")) {
				System.out.println("\n" + s.substring(1, s.length()) + "\n");
			} else if (s.trim().length() == 0) {
				// ignore
			} else {
				String[] command = s.split("\\s");
				try {
					execute(command);
				} catch (ValidationException ex) {
					System.out.println("Error is"+ex.getLocalizedMessage());
				}
			}
		}
	}

	public static Map<String, String> paramsMap(String[] args)
			throws ValidationException {
		Map<String, String> params = new HashMap<String, String>();
		int i = 0;
		while (i < args.length) {
			i = resolveParam(args, i, params);
		}
		return params;
	}

	public static int resolveParam(String[] args, int current,
			Map<String, String> map) throws ValidationException {
		String name = args[current];
		if (!availableArgs.contains(name)) {
			System.err
					.println("argument " + name + " not supported, ignoring!");
		} else {
			current++;
			if (current == args.length) {
				throw new ValidationException("Missig value for arg " + name);
			}
			map.put(name, args[current]);
			return current + 1;
		}
		return current + 2;
	}

	public static void runXML(String xml) throws ValidationException {
		long time = System.currentTimeMillis();
		String xmlRaw = asString(xml);
		XMLValidation validator = getValidatorFor(xmlRaw);
		validator.validate();
		System.out.println("El xml esta bien formado. Verificado en "
				+ (System.currentTimeMillis() - time) + " milliseconds");
	}

	public static void run(String xml, String dtd) throws ValidationException {
		long time = System.currentTimeMillis();
		String xmlRaw = asString(xml);
		XMLValidation validator = getValidatorFor(xmlRaw);
		validator.validate();
		DTDBuilder builder = new DTDBuilder(asString(dtd));
		builder.build();
		XMLToDTDDumper dumper = new XMLToDTDDumper();
		dumper.build(validator.getContext().getRoot(), builder.getContext());
		System.out
				.println("El xml esta bien formado y es valido. Verificado en "
						+ (System.currentTimeMillis() - time) + " milliseconds");
	}

	public static XMLValidation getValidatorFor(String xml) {
		XMLValidatorBuilder builder = new XMLValidatorBuilder();
		Node start = builder.build();
		return new XMLValidation(start, xml);
	}

	public static String asString(String filename) throws ValidationException {
		try {
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			StringBuilder builder = new StringBuilder();
			while (sc.hasNextLine()) {
				builder.append(sc.nextLine() + "\n");
			}
			return builder.toString();
		} catch (FileNotFoundException ex) {
			throw new ValidationException("Filed " + filename + " not found");
		}
	}
}
