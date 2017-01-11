package com.runix.xdvalidator.xml.validator.module;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.xml.validator.module.node.Node;
import com.runix.xdvalidator.xml.validator.module.node.Result;

public class XMLValidation {

	private Node start;

	private String xml;

	private XMLValidationContext context;

	public XMLValidation(Node start, String xml) {
		this.start = start;
		this.xml = xml;
		this.context = new XMLValidationContext();
	}

	public void validateFirstLine() throws ValidationException {
		String first = this.xml.substring(0, this.xml.indexOf("\n"));
		this.xml = this.xml.substring(this.xml.indexOf("\n"));
		String[] chunks = first.split("\\s");
		StringBuilder error = new StringBuilder();
		if (!chunks[0].equals("<?xml")) {
			error.append("Well formed xml start is mandatory\n");
		}
		if (!chunks[1].equals("version=\"")) {
			if (!(chunks[1].endsWith("\"") || chunks[1].endsWith("\"?>"))) {
				error.append("Well formed version is mandatory\n");
			}
		}

		if (chunks.length == 3) {
			if (!chunks[2].equals("encoding=\"") && !chunks[2].endsWith("\"?>")) {
				error.append("Encoding is mandatory");
			}
		}
		if (error.length() > 0) {
			throw new ValidationException("On first line : " + error.toString());
		}
	}

	public void validate() throws ValidationException {
		this.validateFirstLine();
		char[] raw = xml.toCharArray();
		IntegerWrapper wrapper = new IntegerWrapper();
		Node actual = start;
		boolean showLine = true;
		try {
			while (wrapper.value < raw.length) {
				Result result = actual.handle(wrapper, raw, this.context);
				if (wrapper.value == raw.length) {
					break;
				}
				Node next = actual.getModuleFor(raw[wrapper.value]);
				if (next == null) {
					throw new ValidationException("Unexpected character ( "
							+ raw[wrapper.value] + " ), expected ( "
							+ actual.nextNodes() + " )");
				}
				actual = next;
			}

			context.validate();
			if (!actual.isFinal()) {
				showLine = false;
				throw new ValidationException(
						"End reached in a non-final node; non closed nodes "
								+ context.nonClosedNodes());
			}
		} catch (ValidationException exception) {
			String start = "";
			if (showLine) {
				start += "In line [ " + resolveLine(wrapper, raw) + " ] =>";
			}
			throw new ValidationException(start + " " + exception.getMessage());
		}
	}

	private String resolveLine(IntegerWrapper iw, char[] xml) {
		int lpivot = iw.value;
		while (lpivot > 0 && xml[lpivot] != '\n') {
			--lpivot;
		}
		int rpivot = iw.value;
		while (rpivot < xml.length && xml[rpivot] != '\n') {
			++rpivot;
		}
		return new String(xml, lpivot, (rpivot - lpivot));
	}

	public XMLValidationContext getContext() {
		return this.context;
	}
}
