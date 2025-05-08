package net.erdfelt.maven.graphing;

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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import net.erdfelt.maven.graphing.graph.GraphRenderer;
import net.erdfelt.maven.graphing.graph.GraphingException;
import net.erdfelt.maven.graphing.graph.decorator.EdgeDecorator;
import net.erdfelt.maven.graphing.graph.decorator.GraphDecorator;
import net.erdfelt.maven.graphing.graph.decorator.NodeDecorator;
import net.erdfelt.maven.graphing.graph.model.Edge;
import net.erdfelt.maven.graphing.graph.model.Graph;
import net.erdfelt.maven.graphing.graph.model.GraphConstraintException;
import net.erdfelt.maven.graphing.graph.model.Node;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;

/**
 * MultimoduleGraphMojo
 * 
 * @author <a href="mailto:joakim@erdfelt.net">Joakim Erdfelt</a>
 *
 * @goal multimodule
 * @aggregator
 */
public class MultimoduleGraphMojo
extends AbstractMojo
{
    /**
     * The projects in the current build. Each of these is subject to refreshing.
     * 
     * @parameter default-value="${reactorProjects}"
     * @required
     * @readonly
     */
    private List projects;

    /**
     * @component role="net.erdfelt.maven.graphing.graph.GraphRenderer" roleHint="graphviz"
     */
    private GraphRenderer graphRenderer;

    /**
     * @parameter property="graphing.ignoreVersions" default-value="true"
     */
    private boolean ignoreVersions;

    /**
     * @parameter property="graphing.filterTests" default-value="true"
     */
    private boolean filterTests;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info( "Found " + projects.size() + " Project(s)" );

        Graph graph = new Graph();
        
        getLog().debug("Using: " + graphRenderer);

        try
        {
            Iterator it = projects.iterator();
            while ( it.hasNext() )
            {
                MavenProject project = (MavenProject) it.next();
                List deps = project.getDependencies();

                if ( !"pom".equals( project.getPackaging() ) )
                {
                    Node currentNode = graph.addNode( toNode( project ) );
                    getLog().info( "   Project: " + project.getId() + "  - " + deps.size() + " dep(s)" );

                    addDependenciesToGraph( graph, currentNode, deps );
                }
            }

            if ( graph.getDecorator() == null )
            {
                graph.setDecorator( new GraphDecorator() );
            }

            graph.getDecorator().setTitle( "Module Relationship" );
            graph.getDecorator().setOrientation( GraphDecorator.LEFT_TO_RIGHT );

            graphRenderer.render( graph, new File( "target/graph-multimodule.png" ) );
        }
        catch ( GraphConstraintException e )
        {
            getLog().error( "Unable to generate graph." );
        }
        catch ( IOException e )
        {
            getLog().error( "Unable to generate graph.", e );
        }
        catch ( GraphingException e )
        {
            getLog().error( "Unable to generate graph.", e );
        }
    }
    
    private boolean isMultiModuleDependency( Dependency dep )
    {
        boolean ret = false;

        Iterator it = projects.iterator();
        while ( it.hasNext() )
        {
            MavenProject project = (MavenProject) it.next();
            if ( Objects.equals( project.getGroupId(), dep.getGroupId() )
                && Objects.equals( project.getArtifactId(), dep.getArtifactId() )
                && Objects.equals( project.getPackaging(), dep.getType() ) )
            {
                // Found dep that matches on groupId / artifactId / type only.
                if ( ignoreVersions )
                {
                    // No test of version.
                    ret = true;
                    break;
                }
                else if ( Objects.equals( project.getVersion(), dep.getVersion() ) )
                {
                    // Found dep that matches on version too.
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    private void addDependenciesToGraph( Graph graph, Node currentNode, List deps )
        throws GraphConstraintException
    {
        Iterator it = deps.iterator();
        while ( it.hasNext() )
        {
            Dependency dep = (Dependency) it.next();

            boolean isModule = isMultiModuleDependency( dep );

            if ( filterTests && isTestDep( dep ) )
            {
                // Skip
                continue;
            }

            if ( isModule )
            {
                Node depNode = graph.addNode( toNode( dep ) );
                Edge edge = graph.addEdge( currentNode, depNode );

                if ( isTestDep( dep ) )
                {
                    if ( edge.getDecorator() == null )
                    {
                        edge.setDecorator( new EdgeDecorator() );
                    }
                    edge.getDecorator().setLineColor( Color.blue );

                    Color testColor = new Color( 200, 200, 255 );

                    if ( depNode.getDecorator() == null )
                    {
                        depNode.setDecorator( new NodeDecorator() );
                    }

                    depNode.getDecorator().setBackgroundColor( testColor );
                    depNode.getDecorator().setBorderColor( testColor );

                    graph.addNode( depNode );
                }
            }

            getLog().info( "     " + ( isModule ? "* " : "  " ) + dep );
        }
    }

    private boolean isTestDep( Dependency dep )
    {
        return Objects.equals( "test", dep.getScope() );
    }

    private Node toNode( Dependency dep )
    {
        return toNode( dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getType() );
    }

    private Node toNode( MavenProject project )
    {
        return toNode( project.getGroupId(), project.getArtifactId(), project.getVersion(), project.getPackaging() );
    }

    private Node toNode( String groupId, String artifactId, String version, String type )
    {
        StringBuffer label = new StringBuffer();
        label.append( groupId ).append( "\n" );
        label.append( artifactId ).append( "\n" );

        if ( !ignoreVersions )
        {
            label.append( version ).append( "\n" );
        }

        label.append( type );

        return new Node( label.toString() );
    }
}
