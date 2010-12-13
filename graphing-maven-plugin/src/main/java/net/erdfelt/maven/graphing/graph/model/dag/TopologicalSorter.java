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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.erdfelt.maven.graphing.graph.model.Node;

/**
 * Sort nodes by topology of Dag.
 * 
 * <p>
 * Original code by <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * <p>
 * Updated to JDK 1.6 and Generics by <a href="joakim@erdfelt.net">Joakim Erdfelt</a>
 * 
 * @since 1.0
 */
public class TopologicalSorter
{
    enum State
    {
        VISITING, NOT_VISITED, VISITED
    };

    /**
     * Perform a Depth First Search based sort against the graph.
     * 
     * @param graph
     *            the graph to sort
     * @return the sorted list of node labels
     */
    public static List<String> sort(final Dag graph)
    {
        return sortDepthFirstSearch(graph);
    }

    /**
     * Perform a Depth First Search based sort against the node.
     * 
     * @param node
     *            the node to search from.
     * @return the sorted list of node labels.
     */
    public static List<String> sort(final Node node)
    {
        // we need to use addFirst method so we will use LinkedList explicitly
        final LinkedList<String> retValue = new LinkedList<String>();

        final Map<Node, State> nodeStateMap = new HashMap<Node, State>();

        visitDepthFirstSearch(node,nodeStateMap,retValue);

        return retValue;
    }

    private static List<String> sortDepthFirstSearch(final Dag graph)
    {
        final Collection<Node> verticies = graph.getNodes();

        // we need to use addFirst method so we will use LinkedList explicitly
        final LinkedList<String> retValue = new LinkedList<String>();

        final Map<Node, State> nodeStateMap = new HashMap<Node, State>();

        for (Node node : verticies)
        {
            if (isNotVisited(node,nodeStateMap))
            {
                visitDepthFirstSearch(node,nodeStateMap,retValue);
            }
        }

        return retValue;
    }

    /**
     * @param node
     * @param nodeStateMap
     * @return true if node is not visited (yet)
     */
    private static boolean isNotVisited(final Node node, final Map<Node, State> nodeStateMap)
    {
        if (!nodeStateMap.containsKey(node))
        {
            return true;
        }

        return nodeStateMap.get(node) == State.NOT_VISITED;
    }

    private static void visitDepthFirstSearch(final Node node, final Map<Node, State> nodeStateMap, final LinkedList<String> list)
    {
        nodeStateMap.put(node,State.VISITING);

        final List<Node> verticies = node.getChildren();

        for (Node v : verticies)
        {
            if (isNotVisited(v,nodeStateMap))
            {
                visitDepthFirstSearch(v,nodeStateMap,list);
            }
        }

        nodeStateMap.put(node,State.VISITED);

        list.add(node.getLabel());
    }

}
