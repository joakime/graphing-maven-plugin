package net.erdfelt.maven.graphing.graph.impl.graphviz;

import net.erdfelt.maven.graphing.graph.GraphingException;

public class GraphvizNotFoundException extends GraphingException {
	private static final long serialVersionUID = 1137193570348120230L;

	public GraphvizNotFoundException(String message) {
		super(message);
	}
}
