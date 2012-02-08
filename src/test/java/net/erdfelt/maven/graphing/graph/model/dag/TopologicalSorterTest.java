package net.erdfelt.maven.graphing.graph.model.dag;

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

import java.util.ArrayList;
import java.util.List;

import net.erdfelt.maven.graphing.graph.model.GraphConstraintException;

import org.codehaus.plexus.PlexusTestCase;

/**
 * TopologicalSorterTest 
 *
 * @author <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * @author <a href="joakim@erdfelt.net">Joakim Erdfelt</a>
 * 
 * @since 1.0
 */
public class TopologicalSorterTest
    extends PlexusTestCase
{
    public void testSortSimpleForward()
        throws GraphConstraintException
    {
        // a --> b --->c
        //
        // result a,b,c
        final Dag dag = new Dag();

        dag.addEdge( "a", "b" );

        dag.addEdge( "b", "c" );

        final List<String> expected = new ArrayList<String>();

        expected.add( "c" );

        expected.add( "b" );

        expected.add( "a" );

        final List<String> actual = TopologicalSorter.sort( dag );

        assertEquals( "Order is different then expected", expected, actual );
    }

    public void testSortSimpleReverse()
        throws GraphConstraintException
    {
        //
        //  a <-- b <---c
        //
        // result c, b, a
        final Dag dag = new Dag();

        dag.addNode( "a" );

        dag.addNode( "b" );

        dag.addNode( "c" );

        dag.addEdge( "b", "a" );

        dag.addEdge( "c", "b" );

        final List<String> expected = new ArrayList<String>();

        expected.add( "a" );

        expected.add( "b" );

        expected.add( "c" );

        final List<String> actual = TopologicalSorter.sort( dag );

        assertEquals( "Order is different then expected", expected, actual );
    }

    public void testSortComplexOne() throws GraphConstraintException
    {
        //
        //  a --> b --> c --> e
        //        |     |     |
        //        |     V     V
        //          --> d <-- f  --> g
        // result d, g, f, c, b, a
        final Dag dag = new Dag();

        // force order of nodes in the graph
        dag.addNode( "a" );

        dag.addNode( "b" );

        dag.addNode( "c" );

        dag.addNode( "d" );

        dag.addNode( "e" );

        dag.addNode( "f" );

        dag.addEdge( "a", "b" );

        dag.addEdge( "b", "c" );

        dag.addEdge( "b", "d" );

        dag.addEdge( "c", "d" );

        dag.addEdge( "c", "e" );

        dag.addEdge( "f", "d" );

        dag.addEdge( "e", "f" );

        dag.addEdge( "f", "g" );

        final List<String> expected = new ArrayList<String>();

        expected.add( "d" );

        expected.add( "g" );

        expected.add( "f" );

        expected.add( "e" );

        expected.add( "c" );

        expected.add( "b" );

        expected.add( "a" );

        final List<String> actual = TopologicalSorter.sort( dag );
        
        // NOTE: The order if "d" and "g" could be swapped without affecting the correctness
        //       of the sort algorithm.

        assertEquals( "Order is different then expected", expected, actual );
    }
    
    public void testSortComplexTwo() throws GraphConstraintException
    {
        //
        //  a --> b --> c --> e
        //        |     |     |
        //        |     V     V
        //          --> d <-- f
        // result d, f, e, c, b, a
        final Dag dag = new Dag();
        // force order of nodes in the graph

        dag.addNode( "f" );

        dag.addNode( "e" );

        dag.addNode( "d" );

        dag.addNode( "c" );

        dag.addNode( "a" );

        dag.addNode( "b" );

        dag.addEdge( "a", "b" );

        dag.addEdge( "b", "c" );

        dag.addEdge( "b", "d" );

        dag.addEdge( "c", "d" );

        dag.addEdge( "c", "e" );

        dag.addEdge( "f", "d" );

        dag.addEdge( "e", "f" );

        final List<String> expected = new ArrayList<String>();

        expected.add( "d" );

        expected.add( "f" );

        expected.add( "e" );

        expected.add( "c" );

        expected.add( "b" );

        expected.add( "a" );

        final List<String> actual = TopologicalSorter.sort( dag );

        assertEquals( "Order is different then expected", expected, actual );
    }
}
