/**
 * Copyright 2009 Humboldt-Universität zu Berlin, INRIA.
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
package org.corpus_tools.peppermodules.tuebadzModules;

import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.corpus_tools.pepper.common.DOCUMENT_STATUS;
import org.corpus_tools.pepper.impl.PepperMapperImpl;
import org.corpus_tools.salt.SaltFactory;
import org.corpus_tools.salt.common.SDocumentGraph;
import org.corpus_tools.salt.common.SDominanceRelation;
import org.corpus_tools.salt.common.SStructure;
import org.corpus_tools.salt.common.SToken;
import org.corpus_tools.salt.core.GraphTraverseHandler;
import org.corpus_tools.salt.core.SAnnotation;
import org.corpus_tools.salt.core.SGraph.GRAPH_TRAVERSE_TYPE;
import org.corpus_tools.salt.core.SLayer;
import org.corpus_tools.salt.core.SNode;
import org.corpus_tools.salt.core.SRelation;

public class Rearanger extends PepperMapperImpl implements GraphTraverseHandler {
	public Rearanger() {
		this.init();
	}

	/**
	 * Contains all annotation values marking a a node being part of a
	 * topological annotation.
	 */
	protected String[] topologicalAnnotations = { "VF", "LK", "MF", "VC", "C", "NF", "LV" };
	protected HashSet<String> topologicalAnnosHash = null;

	/**
	 * Name of the topological layer.
	 */
	public static final String topoLayerName = "topo";

	public static final String topoAnnoName = "field";

	/**
	 * Name of the syntactical layer.
	 */
	public static final String syntaxLayerName = "syntax";

	public static final String syntaxAnnoName = "phrase";

	/**
	 * Name of the hybrid layer.
	 */
	public static final String hybridLayerName = "hybrid";

	public static final String hybridAnnoName = "node";

	/**
	 * initializes this object.
	 */
	protected void init() {
		topologicalAnnosHash = new HashSet<String>();
		for (String topAnno : this.topologicalAnnotations) {
			topologicalAnnosHash.add(topAnno);
		}
	}

	/**
	 * {@link SLayer} object of topological layer
	 */
	protected SLayer topoLayer = null;
	/**
	 * {@link SLayer} object of syntactic layer
	 */
	protected SLayer syntaxLayer = null;
	/**
	 * {@link SLayer} object of hybrid layer
	 */
	protected SLayer hybridLayer = null;

	/**
	 * A stack containing all {@link SStructure} nodes belonging to the
	 * topological layer between current node and the root.
	 */
	protected Stack<SStructure> topoPath = null;
	/**
	 * A stack containing all {@link SStructure} nodes belonging to the
	 * syntactic layer between current node and the root.
	 */
	protected Stack<SStructure> syntaxPath = null;
	/**
	 * A stack containing all {@link SStructure} nodes belonging to the hybrid
	 * layer between current node and the root.
	 */
	protected Stack<SStructure> hybridPath = null;

	/**
	 * Artificial root for all topological nodes.
	 */
	protected SStructure topoRoot = null;
	/**
	 * Artificial root for all syntactic nodes.
	 */
	protected SStructure syntaxRoot = null;
	/**
	 * Artificial root for all nodes.
	 */
	protected SStructure hybridRoot = null;

	/**
	 * initialises the layers
	 */
	protected void initLayers() {
		this.topoLayer = SaltFactory.createSLayer();
		this.topoLayer.setName(topoLayerName);
		this.syntaxLayer = SaltFactory.createSLayer();
		this.syntaxLayer.setName(syntaxLayerName);
		getDocument().getDocumentGraph().addLayer(syntaxLayer);
		getDocument().getDocumentGraph().addLayer(topoLayer);
	}

	/**
	 * resets the global variables
	 */
	protected void newSentence() {
		// creating topo Path
		this.topoPath = new Stack<SStructure>();
		this.topoRoot = null;

		// creating syntax Path
		this.syntaxPath = new Stack<SStructure>();
		this.syntaxRoot = null;
	}

	/**
	 * This method maps a Salt document to a Treetagger document
	 */
	@Override
	public DOCUMENT_STATUS mapSDocument() {
		if (getDocument().getDocumentGraph() == null) {
			getDocument().setDocumentGraph(SaltFactory.createSDocumentGraph());
		}
		List<SNode> roots = getDocument().getDocumentGraph().getRoots();
		getDocument().getDocumentGraph().traverse(roots, GRAPH_TRAVERSE_TYPE.TOP_DOWN_DEPTH_FIRST, "any", this);
		return (DOCUMENT_STATUS.COMPLETED);
	}

	/**
	 * returns if a node is a topological node or a syntactic node.
	 * 
	 * @param sStructure
	 * @return
	 */
	protected boolean isTopoNode(SStructure sStructure) {
		for (SAnnotation sAnno : sStructure.getAnnotations()) {// walk through
																// all
																// sAnnotations
																// of node
			if (this.topologicalAnnosHash.contains(sAnno.getValue())) {
				return (true);
			}
		}// walk through all sAnnotations of node
		return (false);
	}

	/**
	 * Stores all during traversion nodes which do not have some tokens
	 */
	protected Vector<SStructure> topoStructsWithoutSToken = new Vector<SStructure>();
	/**
	 * Stores all during traversion nodes which do not have some tokens
	 */
	protected Vector<SStructure> syntaxStructsWithoutSToken = new Vector<SStructure>();

	@Override
	public void nodeReached(GRAPH_TRAVERSE_TYPE traversalType, String traversalId, SNode currNode, SRelation relation, SNode fromNode, long order) {
		if (currNode instanceof SStructure) {

			SStructure sStructure = (SStructure) currNode;
			boolean belongsToToDuplicatingLayer = false;
			for (SLayer sLayer : sStructure.getLayers()) {
				if ("tiger".equalsIgnoreCase(sLayer.getName())) {
					belongsToToDuplicatingLayer = true;
					break;
				}
			}

			if (belongsToToDuplicatingLayer) {// only split (and duplicate)
												// nodes if they belong to the
												// interesting layer (tiger)
				SDocumentGraph sDocGraph = sStructure.getGraph();

				SDominanceRelation sDRel = null;
				if ((relation != null) && (relation instanceof SDominanceRelation))
					sDRel = (SDominanceRelation) relation;

				{// put original node in hybrid layer
					if (this.hybridLayer == null) {
						this.hybridLayer = SaltFactory.createSLayer();
						this.hybridLayer.setName(hybridLayerName);
						getDocument().getDocumentGraph().addLayer(this.hybridLayer);
						sStructure.addLayer(hybridLayer);
						if (sDRel != null) {
							sDRel.addLayer(hybridLayer);
						}
					}
				}// put original node in hybrid layer

				if (isTopoNode(sStructure)) {// node is topo node
					if (this.topoRoot == null) {// creating topo root and adding
												// to graph etc.
												// creating topo root
						this.topoRoot = SaltFactory.createSStructure();
						topoRoot.addLayer(topoLayer);
						this.topoRoot.createAnnotation(null, topoAnnoName, "TOP");
						getDocument().getDocumentGraph().addNode(topoRoot);
						// adding topoRoot to topoPath
						this.topoPath.push(topoRoot);
					}// creating topo root and adding to graph etc.

					{// creating new node
						SStructure topoNode = SaltFactory.createSStructure();
						sDocGraph.addNode(topoNode);
						topoLayer.getNodes().add(topoNode);

						if (sStructure.getAnnotations() != null) {
							for (SAnnotation sAnno : sStructure.getAnnotations()) {
								if (sAnno.getName().equalsIgnoreCase("cat"))
									topoNode.createAnnotation(topoLayerName, topoAnnoName, sAnno.getValue_STEXT());
								else
									topoNode.createAnnotation(sAnno.getNamespace(), sAnno.getName(), sAnno.getValue_STEXT());
							}
						}

						SStructure father = topoPath.peek();
						if (father != null) {
							{// creating relation to father topo node
								SDominanceRelation sDomRel = SaltFactory.createSDominanceRelation();
								sDomRel.setSource(father);
								sDomRel.setTarget(topoNode);
								sDocGraph.addRelation(sDomRel);
								sDomRel.addLayer(topoLayer);
								if ((sDRel != null) && (sDRel.getAnnotations() != null)) {
									for (SAnnotation sAnno : sDRel.getAnnotations()) {
										sDomRel.createAnnotation(topoLayerName, topoAnnoName, sAnno.getValue_STEXT());
									}
								}
							}// creating relation to father topo node
						}
						{// putting node to list of nodes without tokens and
							// clean up the list
							if (this.topoStructsWithoutSToken.contains(father))
								this.topoStructsWithoutSToken.remove(father);
							this.topoStructsWithoutSToken.add(topoNode);
						}// putting node to list of nodes without tokens and
							// clean up the list

						topoPath.push(topoNode);
					}// creating new node
				}// node is topo node
				else {// node is syntax node
					{// creating new node
						SStructure syntaxNode = SaltFactory.createSStructure();
						sDocGraph.addNode(syntaxNode);
						syntaxLayer.getNodes().add(syntaxNode);
						if (syntaxRoot == null)
							this.syntaxRoot = syntaxNode;

						if (sStructure.getAnnotations() != null) {
							for (SAnnotation sAnno : sStructure.getAnnotations()) {
								if (sAnno.getName().equalsIgnoreCase("cat"))
									syntaxNode.createAnnotation(syntaxLayerName, syntaxAnnoName, sAnno.getValue_STEXT());
								else
									syntaxNode.createAnnotation(sAnno.getNamespace(), sAnno.getName(), sAnno.getValue_STEXT());
							}
						}

						SStructure father = null;
						if (!syntaxPath.isEmpty())
							father = syntaxPath.peek();

						{// creating relation to father syntax node
							if (father != null) {
								SDominanceRelation sDomRel = SaltFactory.createSDominanceRelation();
								sDomRel.setSource(father);
								sDomRel.setTarget(syntaxNode);
								sDocGraph.addRelation(sDomRel);
								if ((sDRel != null) && (sDRel.getAnnotations() != null)) {
									for (SAnnotation sAnno : sDRel.getAnnotations()) {
										sDomRel.createAnnotation(syntaxLayerName, sAnno.getName(), sAnno.getValue_STEXT());
									}
								}
								sDomRel.addLayer(syntaxLayer);
							}
						}// creating relation to father syntax node
						{// putting node to list of nodes without tokens and
							// clean up the list
							if (this.syntaxStructsWithoutSToken.contains(father))
								this.syntaxStructsWithoutSToken.remove(father);
							this.syntaxStructsWithoutSToken.add(syntaxNode);
						}// putting node to list of nodes without tokens and
							// clean up the list

						syntaxPath.push(syntaxNode);
					}// creating new node
				}// node is syntax node
			}
		}
	}

	@Override
	public void nodeLeft(GRAPH_TRAVERSE_TYPE traversalType, String traversalId, SNode currNode, SRelation<SNode, SNode> relation, SNode fromNode, long order) {
		if (currNode instanceof SStructure) {// node is SStructure node
			SStructure sStructure = (SStructure) currNode;
			if (isTopoNode(sStructure)) {// node is topo node
				if (this.topoPath.size() > 0) {
					this.topoPath.pop();
				}
			}// node is topo node
			else {// node is syntax node
				if (this.syntaxPath.size() > 0) {
					this.syntaxPath.pop();
				}
			}// node is syntax node
		}// node is SStructure node
	}

	@Override
	public boolean checkConstraint(GRAPH_TRAVERSE_TYPE traversalType, String traversalId, SRelation relation, SNode currNode, long order) {
		if (currNode instanceof SStructure) {
			if ((relation == null) || (relation instanceof SDominanceRelation))
				return (true);
			else
				return (false);
		} else if (currNode instanceof SToken) {
			if (relation instanceof SDominanceRelation) {
				if ((this.topoPath != null) && (this.topoPath.size() > 0)) {
					SDominanceRelation sDomRel_new = this.connectStructWithStoken(this.topoPath.peek(), (SToken) currNode, (SDominanceRelation) relation);
					sDomRel_new.setSource(this.topoPath.peek());
					sDomRel_new.addLayer(this.topoLayer);
					if ((sDomRel_new).getAnnotations() != null) {
						for (SAnnotation sAnno : (sDomRel_new).getAnnotations()) {
							sDomRel_new.createAnnotation(topoLayerName, topoAnnoName, sAnno.getValue_STEXT());
						}
					}
				}

				if ((this.syntaxPath != null) && (this.syntaxPath.size() > 0)) {
					SDominanceRelation sDomRel_new = this.connectStructWithStoken(this.syntaxPath.peek(), (SToken) currNode, (SDominanceRelation) relation);
					sDomRel_new.setSource(this.syntaxPath.peek());
					sDomRel_new.addLayer(syntaxLayer);
					if ((sDomRel_new).getAnnotations() != null) {
						for (SAnnotation sAnno : (sDomRel_new).getAnnotations()) {
							sDomRel_new.createAnnotation(syntaxLayerName, syntaxAnnoName, sAnno.getValue_STEXT());
						}
					}
				}
			}
			return (false);
		} else
			return (false);
	}

	protected SDominanceRelation connectStructWithStoken(SStructure sStruct, SToken sToken, SDominanceRelation sDomRel) {
		SDocumentGraph sDocGraph = sStruct.getGraph();
		SDominanceRelation sDomRel_new = SaltFactory.createSDominanceRelation();
		sDomRel_new.setTarget(sToken);
		sDocGraph.addRelation(sDomRel_new);

		return (sDomRel_new);
	}
}
