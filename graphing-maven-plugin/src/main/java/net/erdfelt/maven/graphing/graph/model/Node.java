package net.erdfelt.maven.graphing.graph.model;

/*
 * Copyright (c) Joakim Erdfelt.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.erdfelt.maven.graphing.graph.decorator.NodeDecorator;

/**
 * A Node in the DirectedAcyclicGraph.
 * <p>
 * Original code by <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * <p>
 * Updated to JDK 1.6 and Generics by <a href="joakim@erdfelt.net">Joakim Erdfelt</a>
 * 
 * @since 1.0
 */
public class Node implements Cloneable, Serializable
{
    private static final long serialVersionUID = 9023816267179798356L;

    private NodeDecorator decorator;

    private String label = null;

    private List<Node> children = new ArrayList<Node>();

    private List<Node> parents = new ArrayList<Node>();

    /**
     * Create a new Node with the following label.
     */
    public Node(final String label)
    {
        this.label = label;
    }

    /**
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * <p>
     * Add a child node to this node.
     * </p>
     * 
     * <p>
     * NOTE: This method is used solely by the {@link Graph}.
     * </p>
     * 
     * @param node
     *            the child node to add.
     */
    protected void addChildNode(final Node node)
    {
        // prevent duplicates
        if (!children.contains(node))
        {
            children.add(node);
        }
    }

    /**
     * <p>
     * Remove a child node from this node.
     * </p>
     * 
     * <p>
     * NOTE: This method is used solely by the {@link Graph}.
     * </p>
     * 
     * @param node
     */
    protected void removeChildNode(final Node node)
    {
        children.remove(node);
    }

    /**
     * @param node
     */
    protected void addParentNode(final Node node)
    {
        // prevent duplicates
        if (!parents.contains(node))
        {
            parents.add(node);
        }
    }

    protected void removeParentNode(final Node node)
    {
        parents.remove(node);
    }

    /**
     * Return list of {@link Node} objects.
     * 
     * @return the list of node objects
     */
    public List<Node> getChildren()
    {
        return children;
    }

    /**
     * Get the labels used by the most direct children.
     * 
     * @return the labels used by the most direct children.
     */
    public List<String> getChildLabels()
    {
        final List<String> retValue = new ArrayList<String>(children.size());

        for (Node node : children)
        {
            retValue.add(node.getLabel());
        }
        return retValue;
    }

    /**
     * Get the list the most direct ancestors (parents)
     * 
     * @return list of parents
     */
    public List<Node> getParents()
    {
        return parents;
    }

    /**
     * Get the labels used by the most direct ancestors (parents).
     * 
     * @return the labels used parents
     */
    public List<String> getParentLabels()
    {
        final List<String> retValue = new ArrayList<String>(parents.size());

        for (Node node : parents)
        {
            retValue.add(node.getLabel());
        }
        return retValue;
    }

    /**
     * Indicates if given node has no child
     * 
     * @return <code>true</true> if this node has no child, <code>false</code> otherwise
     */
    public boolean isLeaf()
    {
        return children.size() == 0;
    }

    /**
     * Indicates if given node has no parent
     * 
     * @return <code>true</true> if this node has no parent, <code>false</code> otherwise
     */
    public boolean isRoot()
    {
        return parents.size() == 0;
    }

    /**
     * Indicates if there is at least one edee leading to or from given node
     * 
     * @return <code>true</true> if this node is connected with other node,<code>false</code> otherwise
     */
    public boolean isConnected()
    {
        return isRoot() || isLeaf();
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        // Clone label
        Node clone = new Node(label);

        // Track cloned nodes to ensure proper tree cloning (even cycles!)
        Map<String, Node> clonedNodes = new HashMap<String, Node>();
        clonedNodes.put(label,clone);
        Iterator<Node> it;

        // Clone parents
        it = parents.iterator();
        while (it.hasNext())
        {
            Node subnode = it.next();
            Node subclone = clonedNodes.get(subnode.label);
            if (subclone == null)
            {
                subclone = (Node)subnode.clone();
                clonedNodes.put(subclone.label,subclone);
            }
            clone.parents.add(subclone);
        }

        // Clone children
        it = children.iterator();
        while (it.hasNext())
        {
            Node subnode = it.next();
            Node subclone = clonedNodes.get(subnode.label);
            if (subclone == null)
            {
                subclone = (Node)subnode.clone();
                clonedNodes.put(subclone.label,subclone);
            }
            clone.children.add(subclone);
        }

        return clone;
    }

    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("Node[");
        buf.append("label=").append(label);
        buf.append("parents=").append(parents.size());
        buf.append("children=").append(children.size());
        buf.append("]");

        return buf.toString();
    }

    public NodeDecorator getDecorator()
    {
        // Lazy Init of decorator.
        if (decorator == null)
        {
            decorator = new NodeDecorator();
        }
        return decorator;
    }

    public void setDecorator(NodeDecorator decorator)
    {
        this.decorator = decorator;
    }
}
