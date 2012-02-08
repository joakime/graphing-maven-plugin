package net.erdfelt.maven.graphing.graph.impl.graphviz;

import java.awt.Color;
import java.io.File;

import net.erdfelt.maven.graphing.graph.GraphRenderer;
import net.erdfelt.maven.graphing.graph.decorator.EdgeDecorator;
import net.erdfelt.maven.graphing.graph.decorator.GraphDecorator;
import net.erdfelt.maven.graphing.graph.decorator.NodeDecorator;
import net.erdfelt.maven.graphing.graph.model.Edge;
import net.erdfelt.maven.graphing.graph.model.Graph;
import net.erdfelt.maven.graphing.graph.model.Node;

import org.codehaus.plexus.PlexusTestCase;
import org.junit.Assert;

/**
 * GraphvizRendererTest
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 */
public class GraphvizRendererTest extends PlexusTestCase {
	private GraphRenderer getRenderer() throws Exception {
		GraphRenderer renderer = (GraphRenderer) lookup(
				GraphRenderer.class.getName(), "graphviz");
		Assert.assertNotNull("GraphRenderer lookup should not be null",
				renderer);
		return renderer;
	}

	public void testSimple() throws Exception {
		GraphRenderer renderer = getRenderer();

		Graph model = new Graph();
		GraphDecorator decorator = new GraphDecorator();
		model.setDecorator(decorator);

		decorator.setTitle("symple");

		model.addEdge("main", "parse");
		model.addEdge("parse", "execute");
		model.addEdge("main", "init");
		model.addEdge("main", "cleanup");
		model.addEdge("execute", "make_string");
		model.addEdge("execute", "printf");
		model.addEdge("init", "make_string");
		model.addEdge("main", "printf");
		model.addEdge("execute", "compare");

		File outputFile = new File("target/graph/simple.png");
		File dotFile = new File("target/graph/simple.dot");

		try {
			renderer.render(model, outputFile);

			assertTrue(outputFile.exists());
			assertTrue(outputFile.isFile());
		} catch (GraphvizNotFoundException ignore) {
			// Do not test for png existence if graphviz is not present.
		}

		assertTrue(dotFile.exists());
		assertTrue(dotFile.isFile());
	}

	public void testFancy() throws Exception {
		GraphRenderer renderer = getRenderer();

		Graph model = new Graph();
		GraphDecorator decorator = new GraphDecorator();
		model.setDecorator(decorator);

		Edge edge;

		model.addEdge("main", "parse");
		model.addEdge("parse", "execute");

		edge = model.addEdge("main", "init");
		edge.setDecorator(new EdgeDecorator());
		edge.getDecorator().setStyle(EdgeDecorator.LineStyle.DASHED);
		edge.getDecorator().setLineColor(Color.CYAN);

		model.addEdge("main", "cleanup");

		Node makeString = model.addNode("make a \nstring");

		model.addEdge("execute", makeString.getLabel());
		model.addEdge("execute", "printf");
		model.addEdge("init", makeString.getLabel());

		edge = model.addEdge("main", "printf");
		edge.setDecorator(new EdgeDecorator());
		edge.getDecorator().setStyle(EdgeDecorator.LineStyle.BOLD);
		edge.getDecorator().setLineLabel("100 times");

		Node compare = model.addNode("compare");
		compare.setDecorator(new NodeDecorator());

		Color purple = new Color(0.7f, 0.3f, 1.0f);

		compare.getDecorator().setBackgroundColor(purple);
		compare.getDecorator().setBorderColor(purple);
		compare.getDecorator().setLabelColor(Color.WHITE);

		edge = model.addEdge("execute", "compare");
		edge.setDecorator(new EdgeDecorator());
		edge.getDecorator().setLineColor(Color.RED);

		File outputFile = new File("target/graph/fancy.png");
		File dotFile = new File("target/graph/fancy.dot");

		try {
			renderer.render(model, outputFile);

			assertTrue(outputFile.exists());
			assertTrue(outputFile.isFile());
		} catch (GraphvizNotFoundException ignore) {
			// Do not test for png existence if graphviz is not present.
		}

		assertTrue(dotFile.exists());
		assertTrue(dotFile.isFile());
	}
}
