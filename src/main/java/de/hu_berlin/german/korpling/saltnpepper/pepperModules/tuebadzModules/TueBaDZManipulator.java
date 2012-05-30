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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import de.hu_berlin.german.korpling.saltnpepper.pepper.pepperExceptions.PepperModuleException;
import de.hu_berlin.german.korpling.saltnpepper.pepper.pepperModules.PepperManipulator;
import de.hu_berlin.german.korpling.saltnpepper.pepper.pepperModules.impl.PepperManipulatorImpl;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDocumentGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCore.SElementId;

/**
 * This manipulator was developed especially for the TueBaDZ Corpus.
 * It creates a SSpan-objects for every SToken object in the document. All annotations for STokens will be duplicated and added to the spans. 
 * The annotations of the tokens will be renamed from "annoName" to "annoName."
 * For example a "pos"-annotation of SToken-object will be renamedto a "pos."-annotation.
 * All spans, tokens and spanning relations will be added to an artificial layer named "TueBaDZ".
 * @author Florian Zipser
 * @version 1.0
 *
 */
@Component(name="TueBaDZManipulatorComponent", factory="PepperManipulatorComponentFactory")
@Service(value=PepperManipulator.class)
public class TueBaDZManipulator extends PepperManipulatorImpl 
{
	public TueBaDZManipulator()
	{
		super();
		//setting name of module
		this.name= "TueBaDZManipulator";
	}
	
	
	/**
	 * This method is called by method start() of superclass PepperManipulator, if the method was not overriden
	 * by the current class. If this is not the case, this method will be called for every document which has
	 * to be processed.
	 * This method traverses the graph of the {@link SDocumentGraph} object given by the given {@link SElementId} object by top down | 
	 * depth first algorithm. During the traversion each node will be identified by being either a topological node or a syntactic node. A node
	 * is identified as topological, when it has an annotation value being contained in the list {@link TueBaDZManipulator#topologicalAnnotations}, 
	 * all the other nodes are identified as a syntactic node. Each syntactic node and each topologic node as well will be copied in a common
	 * syntactic graph or topological graph. Both artificional graphs will be added to the {@link SDocumentGraph} object. All nodes of both graphs
	 * will be marked as syntactic or topological by putting them to a syntactic or morphological layer.
	 * <br/>
	 * An artificial root node for all topological nodes and another one for all syntactical nodes will be created.
	 * <br/>
	 * If a node is a syntactic layer, the node will be copied and added to the syntactical layer. The gap between to syntactical nodes (as father 
	 * or sun of a topological node) in the syntactical layer will be filled by copying the relation between sun and topo node to the syntactical layer
	 * between father and sun.   
	 * @param sElementId the id value for the current document or corpus to process  
	 */
	@Override
	public void start(SElementId sElementId) throws PepperModuleException 
	{
		if (	(sElementId!= null) &&
				(sElementId.getSIdentifiableElement()!= null) &&
				((sElementId.getSIdentifiableElement() instanceof SDocument)))
		{//only if given sElementId belongs to an object of type SDocument or SCorpus	
			
			SDocumentGraph sDocGraph= ((SDocument)sElementId.getSIdentifiableElement()).getSDocumentGraph();
			if(sDocGraph!= null)
			{//if document contains a document graph
				Rearanger rearanger= new Rearanger();
				rearanger.setsDocGraph(sDocGraph);
				rearanger.start();
			}//if document contains a document graph
		}//only if given sElementId belongs to an object of type SDocument or SCorpus
		
	}
}
