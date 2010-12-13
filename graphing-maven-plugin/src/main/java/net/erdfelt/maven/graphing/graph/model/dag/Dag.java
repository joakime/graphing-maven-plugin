package net.erdfelt.maven.graphing.graph.model.dag;

/*
 * Copyright (c) Joakim Erdfelt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.erdfelt.maven.graphing.graph.model.Edge;
import net.erdfelt.maven.graphing.graph.model.Graph;
import net.erdfelt.maven.graphing.graph.model.GraphConstraintException;
import net.erdfelt.maven.graphing.graph.model.Node;

/**
 * Dag - Directed Acyclic Graph
 * 
 * @since 1.0
 */
public class Dag extends Graph implements Cloneable, Serializable
{
    private static final long serialVersionUID = 8764845595085810676L;

    @Override
    protected void assertGraphConstraints(Edge edge) throws GraphConstraintException
    {
        super.assertGraphConstraints(edge);

        List<String> cycle = CycleDetector.introducesCycle(edge.getFrom());

        if (cycle != null)
        {
            // remove edge which introduced cycle

            if (removeEdge(edge) == null)
            {
                throw new IllegalStateException("Unable to remove edge " + edge);
            }

            final String msg = "Edge between '" + edge.getFrom().getLabel() + "' and '" + edge.getTo().getLabel() + "' introduces to cycle in the graph";

            throw new CycleDetectedException(msg,cycle);
        }
    }

    /**
     * Return the list of labels of successor in order decided by topological sort
     * 
     * @param label
     *            The label of the node whose predessors are serched
     * 
     * @return The list of labels. Returned list contains also the label passed as parameter to this method. This label
     *         should always be the last item in the list.
     */
    public List<String> getSuccessorLabels(final String label)
    {
        final Node node = getNode(label);

        final List<String> retValue;

        //optimization.
        if (node.isLeaf())
        {
            retValue = new ArrayList<String>(1);

            retValue.add(label);
        }
        else
        {
            retValue = TopologicalSorter.sort(node);
        }

        return retValue;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
