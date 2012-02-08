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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.erdfelt.maven.graphing.graph.model.Node;

/**
 * Used by Dag to detect cycles in the graph.
 * <p>
 * Original code by <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * <p>
 * Updated to JDK 1.6 and Generics by <a href="joakim@erdfelt.net">Joakim Erdfelt</a>
 * 
 * @since 1.0
 */
public class CycleDetector
{
    enum State
    {
        VISITING, NOT_VISITED, VISITED
    };

    public static List<String> hasCycle(final Dag graph)
    {
        final Collection<Node> nodes = graph.getNodes();

        final Map<Node, State> nodeStateMap = new HashMap<Node, State>();

        List<String> retValue = null;

        for (Node node : nodes)
        {
            if (isNotVisited(node,nodeStateMap))
            {
                retValue = introducesCycle(node,nodeStateMap);

                if (retValue != null)
                {
                    break;
                }
            }
        }

        return retValue;

    }

    /**
     * This method will be called when an egde leading to given node was added and we want to check if introduction of
     * this edge has not resulted in apparition of cycle in the graph
     * 
     * @param node
     * @param nodeStateMap
     * @return the list of node labels in the cycle
     */
    public static List<String> introducesCycle(final Node node, final Map<Node, State> nodeStateMap)
    {
        final LinkedList<String> cycleStack = new LinkedList<String>();

        final boolean hasCycle = dfsVisit(node,cycleStack,nodeStateMap);

        if (hasCycle)
        {
            // we have a situation like: [b, a, c, d, b, f, g, h].
            // Label of Node which introduced  the cycle is at the first position in the list
            // We have to find second occurence of this label and use its position in the list
            // for getting the sublist of node labels of cycle paricipants
            //
            // So in our case we are seraching for [b, a, c, d, b]
            final String label = cycleStack.getFirst();

            final int pos = cycleStack.lastIndexOf(label);

            final List<String> cycle = cycleStack.subList(0,pos + 1);

            Collections.reverse(cycle);

            return cycle;
        }

        return null;
    }

    public static List<String> introducesCycle(final Node node)
    {
        final Map<Node, State> nodeStateMap = new HashMap<Node, State>();

        return introducesCycle(node,nodeStateMap);

    }

    /**
     * @param node
     * @param nodeStateMap
     * @return
     */
    private static boolean isNotVisited(final Node node, final Map<Node, State> nodeStateMap)
    {
        if (!nodeStateMap.containsKey(node))
        {
            return true;
        }

        return nodeStateMap.get(node) == State.NOT_VISITED;
    }

    /**
     * @param node
     * @param nodeStateMap
     * @return
     */
    private static boolean isVisiting(final Node node, final Map<Node, State> nodeStateMap)
    {
        return nodeStateMap.get(node) == State.VISITING;
    }

    private static boolean dfsVisit(final Node node, final LinkedList<String> cycle, final Map<Node, State> nodeStateMap)
    {
        cycle.addFirst(node.getLabel());

        nodeStateMap.put(node,State.VISITING);

        final List<Node> nodes = node.getChildren();

        for (Node v : nodes)
        {
            if (isNotVisited(v,nodeStateMap))
            {
                final boolean hasCycle = dfsVisit(v,cycle,nodeStateMap);

                if (hasCycle)
                {
                    return true;
                }
            }
            else if (isVisiting(v,nodeStateMap))
            {
                cycle.addFirst(v.getLabel());

                return true;
            }
        }
        nodeStateMap.put(node,State.VISITED);

        cycle.removeFirst();

        return false;
    }
}