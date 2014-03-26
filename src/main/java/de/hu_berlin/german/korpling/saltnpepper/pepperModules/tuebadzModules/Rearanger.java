/**
 * Copyright 2009 Humboldt University of Berlin, INRIA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules;

import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.emf.common.util.EList;

import de.hu_berlin.german.korpling.saltnpepper.pepper.common.DOCUMENT_STATUS;
import de.hu_berlin.german.korpling.saltnpepper.pepper.modules.impl.PepperMapperImpl;
import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.Edge;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.Node;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.GraphTraverser;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.GraphTraverser.GRAPH_TRAVERSE_MODE;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.GraphTraverserObject;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.TraversalObject;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDocumentGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDominanceRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SStructure;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SToken;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAnnotation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SLayer;

public class Rearanger extends PepperMapperImpl implements TraversalObject
{	
	public Rearanger()
	{
		this.init();
	}
	
	/**
	 * Contains all annotation values marking a a node being part of a topological annotation.
	 */
	protected String[] topologicalAnnotations= {"VF", "LK", "MF", "VC", "C", "NF", "LV"};
	protected HashSet<String> topologicalAnnosHash= null;
	
	/**
	 * Name of the topological layer.
	 */
	public static final String topoLayerName= "topo";
	
	public static final String topoAnnoName= "field";
	
	/**
	 * Name of the syntactical layer.
	 */
	public static final String syntaxLayerName= "syntax";
	
	public static final String syntaxAnnoName= "phrase";

	/**
	 * Name of the hybrid layer.
	 */
	public static final String hybridLayerName= "hybrid";
	
	public static final String hybridAnnoName= "node";
	
	
	/**
	 * initializes this object.
	 */
	protected void init()
	{
		topologicalAnnosHash= new HashSet<String>();
		for (String topAnno: this.topologicalAnnotations)
		{
			topologicalAnnosHash.add(topAnno);
		}
	}

	/**
	 * {@link SLayer} object of topological layer
	 */
	protected SLayer topoLayer= null;
	/**
	 * {@link SLayer} object of syntactic layer
	 */
	protected SLayer syntaxLayer= null;
	/**
	 * {@link SLayer} object of hybrid layer
	 */
	protected SLayer hybridLayer= null;
	
	/**
	 * A stack containing all {@link SStructure} nodes belonging to the topological layer between current node and the root.
	 */
	protected Stack<SStructure> topoPath= null;
	/**
	 * A stack containing all {@link SStructure} nodes belonging to the syntactic layer between current node and the root.
	 */
	protected Stack<SStructure> syntaxPath= null;
	/**
	 * A stack containing all {@link SStructure} nodes belonging to the hybrid layer between current node and the root.
	 */
	protected Stack<SStructure> hybridPath= null;
	
	/**
	 * Artificial root for all topological nodes.
	 */
	protected SStructure topoRoot= null;
	/**
	 * Artificial root for all syntactic nodes.
	 */
	protected SStructure syntaxRoot= null;
	/**
	 * Artificial root for all nodes.
	 */
	protected SStructure hybridRoot= null;
	
	/**
	 * initialises the layers
	 */
	protected void initLayers(){
		this.topoLayer= SaltFactory.eINSTANCE.createSLayer();
		this.topoLayer.setSName(topoLayerName);
		this.syntaxLayer= SaltFactory.eINSTANCE.createSLayer();
		this.syntaxLayer.setSName(syntaxLayerName);
		getSDocument().getSDocumentGraph().addSLayer(syntaxLayer);
		getSDocument().getSDocumentGraph().addSLayer(topoLayer);		
	}
	
	/**
	 * resets the global variables
	 */
	protected void newSentence() {
		//creating topo Path
		this.topoPath= new Stack<SStructure>();
		this.topoRoot= null;
		
		//creating syntax Path
		this.syntaxPath= new Stack<SStructure>();
		this.syntaxRoot= null;
	}
	
	/**
	 * This method maps a Salt document to a Treetagger document  
	 */
	@Override
	public DOCUMENT_STATUS mapSDocument() {
		if (getSDocument().getSDocumentGraph()== null)
			getSDocument().setSDocumentGraph(SaltFactory.eINSTANCE.createSDocumentGraph());
		GraphTraverser traverser= new GraphTraverser();
		traverser.setGraph(getSDocument().getSDocumentGraph());
		this.initLayers();
		EList<Node> roots = traverser.getRoots();
		for (int idx=0; idx<roots.size(); idx++) {
			this.newSentence();
			GraphTraverserObject traverserObj= traverser.getTraverserObject(GRAPH_TRAVERSE_MODE.DEPTH_FIRST, this);
			traverserObj.start(roots.get(idx));
			traverserObj.waitUntilFinished();			
		}
		return(DOCUMENT_STATUS.COMPLETED);
	}
	
	/**
	 * returns if a node is a topological node or a syntactic node.
	 * @param sStructure
	 * @return
	 */
	protected boolean isTopoNode(SStructure sStructure)
	{
		for (SAnnotation sAnno: sStructure.getSAnnotations())
		{//walk through all sAnnotations of node
			if (this.topologicalAnnosHash.contains(sAnno.getSValue()))
			{
				return (true);
			}
		}//walk through all sAnnotations of node
		return (false);
	}
	
	/**
	 * Stores all during traversion nodes which do not have some tokens
	 */
	protected Vector<SStructure> topoStructsWithoutSToken= new Vector<SStructure>();
	/**
	 * Stores all during traversion nodes which do not have some tokens
	 */
	protected Vector<SStructure> syntaxStructsWithoutSToken= new Vector<SStructure>();
	
	public void nodeReached(	GRAPH_TRAVERSE_MODE traversalMode, 
								Long traversalId,
								Node currNode, 
								Edge edge, 
								Node fromNode, 
								long order)
	{
		if (currNode instanceof SStructure)
		{
			
			SStructure sStructure= (SStructure) currNode;
			boolean belongsToToDuplicatingLayer= false;
			for (SLayer sLayer: sStructure.getSLayers())
			{
				if ("tiger".equalsIgnoreCase(sLayer.getSName()))
				{
					belongsToToDuplicatingLayer= true;
					break;
				}
			}
			
			if (belongsToToDuplicatingLayer)
			{//only split (and duplicate) nodes if they belong to the interesting layer (tiger)
				SDocumentGraph sDocGraph= sStructure.getSDocumentGraph();
	
				SDominanceRelation sDRel= null;
				if (	(edge!= null)&&
						(edge instanceof SDominanceRelation))
					sDRel= (SDominanceRelation) edge;
				
				{//put original node in hybrid layer
					if (this.hybridLayer== null)
					{
						this.hybridLayer= SaltFactory.eINSTANCE.createSLayer();
						this.hybridLayer.setSName(hybridLayerName);
						getSDocument().getSDocumentGraph().getSLayers().add(this.hybridLayer);
						this.hybridLayer.getSNodes().add(sStructure);
						if (sDRel!= null)
							this.hybridLayer.getSRelations().add(sDRel);
					}
				}//put original node in hybrid layer
				
				if (isTopoNode(sStructure))
				{//node is topo node
					if (this.topoRoot== null)
					{//creating topo root and adding to graph etc.
						//creating topo root
						this.topoRoot= SaltFactory.eINSTANCE.createSStructure();
						this.topoLayer.getSNodes().add(topoRoot);
						this.topoRoot.createSAnnotation(null, topoAnnoName, "TOP");
						getSDocument().getSDocumentGraph().addSNode(topoRoot);
						//adding topoRoot to topoPath
						this.topoPath.push(topoRoot);
					}//creating topo root and adding to graph etc.
					
					{//creating new node
						SStructure topoNode= SaltFactory.eINSTANCE.createSStructure();
						sDocGraph.addNode(topoNode);
						topoLayer.getNodes().add(topoNode);
						
						if (sStructure.getSAnnotations()!= null)
						{
							for (SAnnotation sAnno: sStructure.getSAnnotations())
							{
								if (sAnno.getSName().equalsIgnoreCase("cat"))
									topoNode.createSAnnotation(topoLayerName, topoAnnoName, sAnno.getSValueSTEXT());
								else
									topoNode.createSAnnotation(sAnno.getSNS(), sAnno.getSName(), sAnno.getSValueSTEXT());
							}
						}
					
						SStructure father= topoPath.peek();
						if (father!= null)
						{
							{//creating relation to father topo node	
								SDominanceRelation sDomRel= SaltFactory.eINSTANCE.createSDominanceRelation();
								sDomRel.setSSource(father);
								sDomRel.setSTarget(topoNode);
								sDocGraph.addSRelation(sDomRel);
								topoLayer.getSRelations().add(sDomRel);
								if (	(sDRel!= null) &&
										(sDRel.getSAnnotations()!=null))
								{
									for (SAnnotation sAnno:sDRel.getSAnnotations())
									{
										sDomRel.createSAnnotation(topoLayerName, topoAnnoName, sAnno.getSValueSTEXT());
									}
								}
							}//creating relation to father topo node
						}
						{//putting node to list of nodes without tokens and clean up the list
							if (this.topoStructsWithoutSToken.contains(father))
								this.topoStructsWithoutSToken.remove(father);
							this.topoStructsWithoutSToken.add(topoNode);
						}//putting node to list of nodes without tokens and clean up the list
						
						topoPath.push(topoNode);
					}//creating new node
				}//node is topo node
				else
				{//node is syntax node
					{//creating new node
						SStructure syntaxNode= SaltFactory.eINSTANCE.createSStructure();
						sDocGraph.addNode(syntaxNode);
						syntaxLayer.getNodes().add(syntaxNode);
						if (syntaxRoot== null)
							this.syntaxRoot= syntaxNode;
						
						if (sStructure.getSAnnotations()!= null) {
							for (SAnnotation sAnno: sStructure.getSAnnotations())
							{
								if (sAnno.getSName().equalsIgnoreCase("cat"))
									syntaxNode.createSAnnotation(syntaxLayerName, syntaxAnnoName, sAnno.getSValueSTEXT());
								else
									syntaxNode.createSAnnotation(sAnno.getSNS(), sAnno.getSName(), sAnno.getSValueSTEXT());
							}
						}
						
						SStructure father= null;
						if (!syntaxPath.isEmpty())
							 father= syntaxPath.peek();
						
						{//creating relation to father syntax node	
							if (father!= null)
							{
								SDominanceRelation sDomRel= SaltFactory.eINSTANCE.createSDominanceRelation();
								sDomRel.setSSource(father);
								sDomRel.setSTarget(syntaxNode);
								sDocGraph.addSRelation(sDomRel);
								if (	(sDRel!= null) &&
										(sDRel.getSAnnotations()!=null))
								{
									for (SAnnotation sAnno:sDRel.getSAnnotations())
									{
										sDomRel.createSAnnotation(syntaxLayerName, sAnno.getSName(), sAnno.getSValueSTEXT());
									}
								}
								syntaxLayer.getSRelations().add(sDomRel);
							}
						}//creating relation to father syntax node
						{//putting node to list of nodes without tokens and clean up the list
							if (this.syntaxStructsWithoutSToken.contains(father))
								this.syntaxStructsWithoutSToken.remove(father);
							this.syntaxStructsWithoutSToken.add(syntaxNode);
						}//putting node to list of nodes without tokens and clean up the list
						
						syntaxPath.push(syntaxNode);
					}//creating new node
				}//node is syntax node
			}
		}
	}

	/**
	* <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	* @model
	* @generated
	*/
	public void nodeLeft(	GRAPH_TRAVERSE_MODE traversalMode, 
							Long traversalId,
							Node currNode, 
							Edge edge, 
							Node fromNode, 
							long order)
	{
		if (currNode instanceof SStructure)
		{//node is SStructure node
			SStructure sStructure= (SStructure) currNode;
			if (isTopoNode(sStructure))
			{//node is topo node
				if (this.topoPath.size()>0) {
					this.topoPath.pop();
				}
			}//node is topo node
			else
			{//node is syntax node
				if (this.syntaxPath.size()>0) {
					this.syntaxPath.pop();
				}
			}//node is syntax node
		}//node is SStructure node
	}

	/**
	* <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	* @model
	* @generated
	*/
	public boolean checkConstraint(		GRAPH_TRAVERSE_MODE traversalMode, 
						Long traversalId,
						Edge edge, 
						Node currNode, 
						long order)
	{
		if (currNode instanceof SStructure)
		{
			if (	(edge == null)||
					(edge instanceof SDominanceRelation))
				return(true);
			else return(false);
		}
		else if (currNode instanceof SToken)
		{
			if (edge instanceof SDominanceRelation)
			{
				if (	(this.topoPath!= null)&&
						(this.topoPath.size()>0))
				{
					SDominanceRelation sDomRel_new= this.connectStructWithStoken(this.topoPath.peek(), (SToken) currNode, (SDominanceRelation) edge);
					sDomRel_new.setSSource(this.topoPath.peek());
					this.topoLayer.getSRelations().add(sDomRel_new);
					if ((sDomRel_new).getSAnnotations()!=null)
					{
						for (SAnnotation sAnno: (sDomRel_new).getSAnnotations())
						{
							sDomRel_new.createSAnnotation(topoLayerName, topoAnnoName, sAnno.getSValueSTEXT());
						}
					}
				}
				
				if (	(this.syntaxPath!= null)&&
						(this.syntaxPath.size()>0))
				{
					SDominanceRelation sDomRel_new= this.connectStructWithStoken(this.syntaxPath.peek(), (SToken) currNode, (SDominanceRelation) edge);
					sDomRel_new.setSSource(this.syntaxPath.peek());
					this.syntaxLayer.getSRelations().add(sDomRel_new);
					if ((sDomRel_new).getSAnnotations()!=null)
					{
						for (SAnnotation sAnno: (sDomRel_new).getSAnnotations())
						{
							sDomRel_new.createSAnnotation(syntaxLayerName, syntaxAnnoName, sAnno.getSValueSTEXT());
						}
					}
				}
			}
			return(false);
		}
		else return(false);
	}
	
	protected SDominanceRelation connectStructWithStoken(SStructure sStruct, SToken sToken, SDominanceRelation sDomRel)
	{
		SDocumentGraph sDocGraph= sStruct.getSDocumentGraph();
		SDominanceRelation sDomRel_new= SaltFactory.eINSTANCE.createSDominanceRelation();
		sDomRel_new.setSTarget(sToken);
		sDocGraph.addSRelation(sDomRel_new);
		
		return(sDomRel_new);
	}
}
