package de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules;

import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.Edge;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.Node;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.GraphTraverser;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.GraphTraverserObject;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.TraversalObject;
import de.hu_berlin.german.korpling.saltnpepper.salt.graph.modules.GraphTraverser.GRAPH_TRAVERSE_MODE;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDocumentGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDominanceRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SStructure;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SToken;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SAnnotation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SLayer;

public class Rearanger implements TraversalObject
{
	private SDocumentGraph sDocGraph= null;
	public void setsDocGraph(SDocumentGraph sDocGraph) {
		this.sDocGraph = sDocGraph;
	}

	public SDocumentGraph getsDocGraph() {
		return sDocGraph;
	}
	
	public Rearanger()
	{
		this.init();
	}
		
	/**
	 * Contains all annotation values marking a a node being part of a topological annotation.
	 */
	protected String[] topologicalAnnotations= {"VF", "LK", "MF", "VC", "C", "NF"};
	protected HashSet<String> topologicalAnnosHash= null;
	
	/**
	 * Name of the topological layer.
	 */
	public final String topoLayerName= "topo";
	
	public final String topoAnnoName= "field";
	
	/**
	 * Name of the syntactical layer.
	 */
	public final String syntaxLayerName= "syntax";
	
	public final String syntaxAnnoName= "cat";
	
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
	 * A stack containing all {@link SStructure} nodes belonging to the topological layer between current node and the root.
	 */
	protected Stack<SStructure> topoPath= null;
	
	/**
	 * A stack containing all {@link SStructure} nodes belonging to the syntactic layer between current node and the root.
	 */
	protected Stack<SStructure> syntaxPath= null;
	/**
	 * Artificial root for all topological nodes.
	 */
	protected SStructure topoRoot= null;
	/**
	 * Artificial root for all syntactic nodes.
	 */
	protected SStructure syntaxRoot= null;
	
	
	public void start()
	{
		GraphTraverser traverser= new GraphTraverser();
		traverser.setGraph(sDocGraph);
		GraphTraverserObject traverserObj= traverser.getTraverserObject(GRAPH_TRAVERSE_MODE.DEPTH_FIRST, this);
		traverserObj.start(traverser.getRoots());
		//wait until traversion finished
		traverserObj.waitUntilFinished();
	}
	
	/**
	 * returns if a node is a topological node or a syntactic node.
	 * @param sStructure
	 * @return
	 */
	protected boolean isTopoNode(SStructure sStructure)
	{
		Boolean isTopoNode= false;
		
		for (SAnnotation sAnno: sStructure.getSAnnotations())
		{//walk through all sAnnotations of node
			if (this.topologicalAnnosHash.contains(sAnno.getSValue()))
			{
				isTopoNode= true;
			}
		}//walk through all sAnnotations of node
		return(isTopoNode);
	}
	
//	protected Boolean isLastNodeTopoNode= false;
	
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
			SDocumentGraph sDocGraph= sStructure.getSDocumentGraph();
			SDominanceRelation sDRel= null;
			if (	(edge!= null)&&
					(edge instanceof SDominanceRelation))
				sDRel= (SDominanceRelation) edge;
			
			if (isTopoNode(sStructure))
			{//node is topo node
				{//creating new topoLayer, topoPath, topoRoot if necessary
					if (topoLayer== null)
					{
						topoLayer= SaltFactory.eINSTANCE.createSLayer();
						topoLayer.setSName(topoLayerName);
						sDocGraph.addSLayer(topoLayer);
						
						//creating topo Path
						topoPath= new Stack<SStructure>();
						
						//creating topo root
						this.topoRoot= SaltFactory.eINSTANCE.createSStructure();
						topoLayer.getSNodes().add(topoRoot);
						topoRoot.createSAnnotation(null, "cat", "TOP");
						sDocGraph.addSNode(topoRoot);
						
						//adding topoRoot to topoPath
						topoPath.push(topoRoot);
						System.out.println("added to topostack: "+ topoRoot);
					}
				}//creating new topoLayer, topoPath, topoRoot if necessary
				
				System.out.println("---------------->  topo node found:"+ sStructure);
				{//creating new node
//					SStructure topoNode= (SStructure) sStructure.clone();
					SStructure topoNode= SaltFactory.eINSTANCE.createSStructure();
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
					sDocGraph.addNode(topoNode);
					topoLayer.getNodes().add(topoNode);
					
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
					System.out.println("added to topostack: "+ sStructure);
				}//creating new node
			}//node is topo node
			else
			{//node is syntax node
				{//creating new syntaxLayer, syntaxPath, syntaxRoot if necessary
					if (syntaxLayer== null)
					{
						syntaxLayer= SaltFactory.eINSTANCE.createSLayer();
						syntaxLayer.setSName(syntaxLayerName);
						sDocGraph.addSLayer(syntaxLayer);
						
						//creating syntax Path
						syntaxPath= new Stack<SStructure>();
						
						//creating syntax root
						this.syntaxRoot= SaltFactory.eINSTANCE.createSStructure();
						syntaxLayer.getSNodes().add(syntaxRoot);
						syntaxRoot.createSAnnotation(null, "cat", "TOP");
						sDocGraph.addSNode(syntaxRoot);
						
						//adding syntaxRoot to syntaxPath
						syntaxPath.push(syntaxRoot);
					}
				}//creating new syntaxLayer, syntaxPath, syntaxRoot if necessary
				
				{//creating new node
					SStructure syntaxNode= SaltFactory.eINSTANCE.createSStructure();
					if (sStructure.getSAnnotations()!= null)
					{
						for (SAnnotation sAnno: sStructure.getSAnnotations())
						{
							if (sAnno.getSName().equalsIgnoreCase("cat"))
								syntaxNode.createSAnnotation(syntaxLayerName, syntaxAnnoName, sAnno.getSValueSTEXT());
							else
								syntaxNode.createSAnnotation(sAnno.getSNS(), sAnno.getSName(), sAnno.getSValueSTEXT());
						}
					}
					sDocGraph.addNode(syntaxNode);
					syntaxLayer.getNodes().add(syntaxNode);
					
					SStructure father= syntaxPath.peek();
					
					{//creating relation to father syntax node	
						if (father!= null)
						{
							SDominanceRelation sDomRel= SaltFactory.eINSTANCE.createSDominanceRelation();
							System.out.println("graph of father: "+ father.getSDocumentGraph());
							System.out.println("sDocGraph: "+ sDocGraph);
							System.out.println("father: "+ father);
							sDomRel.setSSource(father);
							sDomRel.setSTarget(syntaxNode);
							sDocGraph.addSRelation(sDomRel);
							if (	(sDRel!= null) &&
									(sDRel.getSAnnotations()!=null))
							{
								for (SAnnotation sAnno:sDRel.getSAnnotations())
								{
									sDomRel.createSAnnotation(syntaxLayerName, syntaxAnnoName, sAnno.getSValueSTEXT());
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
				this.topoPath.pop();
			}//node is topo node
			else
			{//node is syntax node
//				this.syntaxPath.pop();
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
				return(true);
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
