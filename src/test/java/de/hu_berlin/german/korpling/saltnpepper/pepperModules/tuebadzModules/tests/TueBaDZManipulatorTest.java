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
package de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules.tests;

import org.corpus_tools.salt.SALT_TYPE;
import org.corpus_tools.salt.SaltFactory;
import org.corpus_tools.salt.common.SCorpus;
import org.corpus_tools.salt.common.SCorpusGraph;
import org.corpus_tools.salt.common.SDocument;
import org.corpus_tools.salt.common.SDocumentGraph;
import org.corpus_tools.salt.common.SDominanceRelation;
import org.corpus_tools.salt.common.SStructure;
import org.corpus_tools.salt.common.STextualDS;
import org.corpus_tools.salt.common.STextualRelation;
import org.corpus_tools.salt.common.SToken;
import org.junit.Before;
import org.junit.Test;

import de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules.TueBaDZManipulator;


public class TueBaDZManipulatorTest
{
	private TueBaDZManipulator fixture= null;
	
	public TueBaDZManipulator getFixture() {
		return fixture;
	}

	public void setFixture(TueBaDZManipulator fixture) {
		this.fixture = fixture;
	}

	@Before
	public void setUp()
	{
		this.setFixture(new TueBaDZManipulator());
		getFixture().setSaltProject(SaltFactory.createSaltProject());
	}
	@Test
	public void testStartManipulator()
	{
		SDocumentGraph sDocGraph= this.createSDocGraph();
		SCorpusGraph sCorpGraph= SaltFactory.createSCorpusGraph();
		getFixture().getSaltProject().getCorpusGraphs().add(sCorpGraph);
		SCorpus sCorpus= SaltFactory.createSCorpus();
		sCorpGraph.addNode(sCorpus);
		SDocument sDoc= SaltFactory.createSDocument();
		sDoc.setName("sample1");
		sDoc.setDocumentGraph(sDocGraph);
		sCorpGraph.addDocument(sCorpus, sDoc);
		
		getFixture().start(sDoc.getIdentifier());
		
		System.out.println(sDocGraph.getLayers());
		
//		{//tests
//			SLayer topoLayer= null;
//			SLayer syntaxLayer= null;
//			for (SLayer sLayer: sDoc.getLayers())
//			{
//				if (sLayer.getName().equals(getFixture().topoLayerName))
//					topoLayer= sLayer;
//				else if (sLayer.getName().equals(getFixture().syntaxLayerName))
//					syntaxLayer= sLayer;	
//			}
//			assertNotNull(topoLayer);
//			assertNotNull(syntaxLayer);
//			assertEquals(10, syntaxLayer.getNodes().size());
//			assertEquals(9, syntaxLayer.getRelations().size());
//			
//			assertEquals(9, topoLayer.getNodes().size());
//			assertEquals(8, topoLayer.getRelations().size());
//		}//tests
	}
	
	public SDocumentGraph createSDocGraph()
	{
		SDocumentGraph sDocGraph= SaltFactory.createSDocumentGraph();
		{//create SDocumentGraph
			STextualDS sText= null;
			{//create primary data
				sText= SaltFactory.createSTextualDS();
				sText.setText("\"Der Kampf wird ein Ende finden, wenn es da ist.\"");
				sDocGraph.addNode(sText);
			}//create primary data
			SToken tokAmp= SaltFactory.createSToken();
			SToken tokDer= SaltFactory.createSToken();
			SToken tokKampf= SaltFactory.createSToken();
			SToken tokWird= SaltFactory.createSToken();
			SToken tokEin= SaltFactory.createSToken();
			SToken tokEnde= SaltFactory.createSToken();
			SToken tokFinden= SaltFactory.createSToken();
			SToken tokComma= SaltFactory.createSToken();
			SToken tokWenn= SaltFactory.createSToken();
			SToken tokEs= SaltFactory.createSToken();
			SToken tokDa= SaltFactory.createSToken();
			SToken tokIst= SaltFactory.createSToken();
			SToken tokDot= SaltFactory.createSToken();
			SToken tokAmp2= SaltFactory.createSToken();
			
			{//create STokens
				STextualRelation sTextRel= null;
				
				tokAmp= SaltFactory.createSToken();
				sDocGraph.addNode(tokAmp);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokAmp);
				sTextRel.setStart(0);
				sTextRel.setEnd(1);
				sDocGraph.addRelation(sTextRel);
				
				tokDer= SaltFactory.createSToken();
				sDocGraph.addNode(tokDer);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokDer);
				sTextRel.setStart(1);
				sTextRel.setEnd(4);
				sDocGraph.addRelation(sTextRel);
				
				tokKampf= SaltFactory.createSToken();
				sDocGraph.addNode(tokKampf);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokKampf);
				sTextRel.setStart(5);
				sTextRel.setEnd(10);
				sDocGraph.addRelation(sTextRel);
				
				tokWird= SaltFactory.createSToken();
				sDocGraph.addNode(tokWird);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokWird);
				sTextRel.setStart(11);
				sTextRel.setEnd(15);
				sDocGraph.addRelation(sTextRel);
				
				tokEin= SaltFactory.createSToken();
				sDocGraph.addNode(tokEin);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokEin);
				sTextRel.setStart(17);
				sTextRel.setEnd(25);
				sDocGraph.addRelation(sTextRel);
				
				tokEnde= SaltFactory.createSToken();
				sDocGraph.addNode(tokEnde);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokEnde);
				sTextRel.setStart(20);
				sTextRel.setEnd(24);
				sDocGraph.addRelation(sTextRel);
				
				tokFinden= SaltFactory.createSToken();
				sDocGraph.addNode(tokFinden);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokFinden);
				sTextRel.setStart(25);
				sTextRel.setEnd(31);
				sDocGraph.addRelation(sTextRel);
				
				tokComma= SaltFactory.createSToken();
				sDocGraph.addNode(tokComma);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokComma);
				sTextRel.setStart(31);
				sTextRel.setEnd(32);
				sDocGraph.addRelation(sTextRel);
				
				tokWenn= SaltFactory.createSToken();
				sDocGraph.addNode(tokWenn);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokWenn);
				sTextRel.setStart(33);
				sTextRel.setEnd(37);
				sDocGraph.addRelation(sTextRel);
				
				tokEs= SaltFactory.createSToken();
				sDocGraph.addNode(tokEs);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokEs);
				sTextRel.setStart(38);
				sTextRel.setEnd(40);
				sDocGraph.addRelation(sTextRel);
				
				tokDa= SaltFactory.createSToken();
				sDocGraph.addNode(tokDa);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokDa);
				sTextRel.setStart(41);
				sTextRel.setEnd(43);
				sDocGraph.addRelation(sTextRel);
				
				tokIst= SaltFactory.createSToken();
				sDocGraph.addNode(tokIst);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokIst);
				sTextRel.setStart(44);
				sTextRel.setEnd(47);
				sDocGraph.addRelation(sTextRel);
				
				tokDot= SaltFactory.createSToken();
				sDocGraph.addNode(tokDot);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokDot);
				sTextRel.setStart(47);
				sTextRel.setEnd(48);
				sDocGraph.addRelation(sTextRel);
				
				tokAmp2= SaltFactory.createSToken();
				sDocGraph.addNode(tokAmp2);
				sTextRel= SaltFactory.createSTextualRelation();
				sTextRel.setTarget(sText);
				sTextRel.setSource(tokAmp2);
				sTextRel.setStart(48);
				sTextRel.setEnd(49);
				sDocGraph.addRelation(sTextRel);
			}//create STokens
			
			SStructure topNode= SaltFactory.createSStructure();
			topNode.createAnnotation(null, "cat", "TOP");
			sDocGraph.addNode(topNode);
			SDominanceRelation sDomRel= null;
			
			{//TOP --> tokAmp
				sDomRel= SaltFactory.createSDominanceRelation();
				sDomRel.setSource(topNode);
				sDomRel.setTarget(tokAmp);
				sDocGraph.addRelation(sDomRel);
				sDomRel.createAnnotation(null, "label", "--");
			}//TOP --> tokAmp
			
			SStructure sStruct_SIMPX= SaltFactory.createSStructure();
			SStructure sStruct_VF= SaltFactory.createSStructure();
			SStructure sStruct_NX1= SaltFactory.createSStructure();
			SStructure sStruct_LK= SaltFactory.createSStructure();
			SStructure sStruct_VXFIN= SaltFactory.createSStructure();
			SStructure sStruct_MF= SaltFactory.createSStructure();
			SStructure sStruct_NX2= SaltFactory.createSStructure();
			SStructure sStruct_VC= SaltFactory.createSStructure();
			SStructure sStruct_VXINF= SaltFactory.createSStructure();
			SStructure sStruct_NF= SaltFactory.createSStructure();
			SStructure sStruct_SIMPX2= SaltFactory.createSStructure();
			SStructure sStruct_C= SaltFactory.createSStructure();
			SStructure sStruct_MF2= SaltFactory.createSStructure();
			SStructure sStruct_NX3= SaltFactory.createSStructure();
			SStructure sStruct_ADVX= SaltFactory.createSStructure();
			SStructure sStruct_VC2= SaltFactory.createSStructure();
			SStructure sStruct_VXFIN2= SaltFactory.createSStructure();
			
			{//TOP --> SIMPX
				sDocGraph.addNode(topNode, sStruct_SIMPX, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_SIMPX.createAnnotation(null, "cat", "SIMPX");
			}//TOP --> SIMPX
			
			{//SIMPX --> VF
				sDocGraph.addNode(sStruct_SIMPX, sStruct_VF, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_VF.createAnnotation(null, "cat", "VF");
			}//SIMPX --> VF
			
			{//VF --> NX1
				sDocGraph.addNode(sStruct_VF, sStruct_NX1, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "ON");
				sStruct_NX1.createAnnotation(null, "cat", "NX");
			}//VF --> NX1
			
			{//NX1 --> Der
				sDocGraph.addNode(sStruct_NX1, tokDer, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
			}//NX1 --> Der
			
			{//NX1 --> Kampf
				sDocGraph.addNode(sStruct_NX1, tokKampf, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//NX1 --> Kampf
			
			{//SIMPX --> LK
				sDocGraph.addNode(sStruct_SIMPX, sStruct_LK, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_LK.createAnnotation(null, "cat", "LK");
			}//SIMPX --> LK
			
			{//LK --> VXFIN
				sDocGraph.addNode(sStruct_LK, sStruct_VXFIN, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
				sStruct_VXFIN.createAnnotation(null, "cat", "VXFIN");
			}//LK --> VXFIN
			
			{//VXFIN --> wird
				sDocGraph.addNode(sStruct_VXFIN, tokWird, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//VXFIN --> wird
			
			
			{//SIMPX --> MF
				sDocGraph.addNode(sStruct_SIMPX, sStruct_MF, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_MF.createAnnotation(null, "cat", "MF");
			}//SIMPX --> MF
			
			{//MF --> NX2
				sDocGraph.addNode(sStruct_MF, sStruct_NX2, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "OA");
				sStruct_NX2.createAnnotation(null, "cat", "NX");
			}//MF --> NX2
			
			{//NX2 --> ein
				sDocGraph.addNode(sStruct_NX2, tokEin, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
			}//NX2 --> ein
			
			{//NX2 --> Ende
				sDocGraph.addNode(sStruct_NX2, tokEnde, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//NX2 --> Ende
			
			
			{//SIMPX --> VC
				sDocGraph.addNode(sStruct_SIMPX, sStruct_VC, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_VC.createAnnotation(null, "cat", "VC");
			}//SIMPX --> VC
			
			{//VC --> VXINF
				sDocGraph.addNode(sStruct_VC, sStruct_VXINF, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "OV");
				sStruct_VXINF.createAnnotation(null, "cat", "VXINF");
			}//VC --> VXINF
			
			{//VXINF --> finden
				sDocGraph.addNode(sStruct_VXINF, tokFinden, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//VXINF --> finden
			
			{//SIMPX --> tokCOmma
				sDocGraph.addNode(sStruct_SIMPX, tokComma, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
			}//SIMPX --> tokCOmma
			
			{//SIMPX --> NF
				sDocGraph.addNode(sStruct_SIMPX, sStruct_NF, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_NF.createAnnotation(null, "cat", "NF");
			}//SIMPX --> NF
			
			{//NF --> SIMPX
				sDocGraph.addNode(sStruct_NF, sStruct_SIMPX2, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "MOD");
				sStruct_SIMPX2.createAnnotation(null, "cat", "SIMPX");
			}//NF --> SIMPX
			
			{//SIMPX --> C
				sDocGraph.addNode(sStruct_SIMPX2, sStruct_C, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_C.createAnnotation(null, "cat", "C");
			}//SIMPX --> C
			
			{//C --> wenn
				sDocGraph.addNode(sStruct_C, tokWenn, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
			}//C --> wenn
			
			{//SIMPX --> MF2
				sDocGraph.addNode(sStruct_SIMPX2, sStruct_MF2, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_MF2.createAnnotation(null, "cat", "MF");
			}//SIMPX --> MF2
			
			{//MF2 --> NX3
				sDocGraph.addNode(sStruct_MF2, sStruct_NX3, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "ON");
				sStruct_NX3.createAnnotation(null, "cat", "NX");
			}//MF2 --> NX3
			
			{//NX3 --> es
				sDocGraph.addNode(sStruct_NX3, tokEs, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//NX3 --> es
			
			{//MF2 --> ADVX
				sDocGraph.addNode(sStruct_MF2, sStruct_ADVX, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "PRED");
				sStruct_ADVX.createAnnotation(null, "cat", "ADVX");
			}//MF2 --> ADVX
			
			{//NX3 --> es
				sDocGraph.addNode(sStruct_ADVX, tokDa, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//NX3 --> es
			
			{//SIMPX --> VC2
				sDocGraph.addNode(sStruct_SIMPX2, sStruct_VC2, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "--");
				sStruct_VC2.createAnnotation(null, "cat", "VC");
			}//SIMPX --> VC2
			
			{//VC2 --> VXFIN2
				sDocGraph.addNode(sStruct_VC2, sStruct_VXFIN2, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
				sStruct_VXFIN2.createAnnotation(null, "cat", "VXFIN");
			}//VC2 --> VXFIN2
			
			{//VXFIN2 --> ist
				sDocGraph.addNode(sStruct_VXFIN2, tokIst, SALT_TYPE.SDOMINANCE_RELATION).createAnnotation(null, "label", "HD");
			}//VXFIN2 --> ist
			
			{//TOP --> tokDot
				sDomRel= SaltFactory.createSDominanceRelation();
				sDomRel.setSource(topNode);
				sDomRel.setTarget(tokDot);
				sDocGraph.addRelation(sDomRel);
				sDomRel.createAnnotation(null, "label", "--");
			}//TOP --> tokDot
			
			{//TOP --> tokAmp2
				sDomRel= SaltFactory.createSDominanceRelation();
				sDomRel.setSource(topNode);
				sDomRel.setTarget(tokAmp2);
				sDocGraph.addRelation(sDomRel);
				sDomRel.createAnnotation(null, "label", "--");
			}//TOP --> tokAmp2
			
			
			
		}//create SDocumentGraph
		return(sDocGraph);
	}
}
