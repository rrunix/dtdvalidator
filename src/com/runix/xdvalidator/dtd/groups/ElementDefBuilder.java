package com.runix.xdvalidator.dtd.groups;

public class ElementDefBuilder {

	public ElementDef build(String name, String type) {
		if(type.equals("EMPTY")) {
			return new EmptyElementDef(name);
		} else if(type.equals("ANY")) {
			return new AnyElementDef(name);
		} else {
			return new ChildElementDef(name, type);
		}
	}
}
