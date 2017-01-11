package com.runix.xdvalidator.dtd.groups;

import javax.xml.bind.ValidationException;

import com.runix.xdvalidator.dtd.ValidationContext;

public class EmptyElementDef extends ElementDef {

	public EmptyElementDef(String name) {
		super(name);
	}

	public void validateThis(ValidationContext context)
			throws ValidationException {
		if (!this.closeOnDef) {
			throw new ValidationException("The element " + this.name
					+ " must be closed on def");
		}
		if (this.childs.size() > 0) {
			throw new ValidationException("The element " + this.name
					+ " is marked as empty, but it's not");
		}
	}
}
