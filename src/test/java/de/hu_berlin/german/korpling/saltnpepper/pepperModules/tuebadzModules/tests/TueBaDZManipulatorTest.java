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
package de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules.tests;

import junit.framework.TestCase;
import de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules.TueBaDZManipulator;
import de.hu_berlin.german.korpling.saltnpepper.salt.SaltFactory;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.SaltProject;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SCorpus;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SCorpusGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sCorpusStructure.SDocument;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDocumentGraph;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SDominanceRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SStructure;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.STYPE_NAME;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.STextualDS;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.STextualRelation;
import de.hu_berlin.german.korpling.saltnpepper.salt.saltCommon.sDocumentStructure.SToken;


public class TueBaDZManipulatorTest extends TestCase
{
	private TueBaDZManipulator fixture= null;
	
	public TueBaDZManipulator getFixture() {
		return fixture;
	}

	public void setFixture(TueBaDZManipulator fixture) {
		this.fixture = fixture;
	}

	@Override
	public void setUp()
	{
		this.setFixture(new TueBaDZManipulator());
	}
	
	public void testStartManipulator()
	{
		SDocumentGraph sDocGraph= this.createSDocGraph();
		SaltProject saltProject= SaltFactory.eINSTANCE.createSaltProject();
		SCorpusGraph sCorpGraph= SaltFactory.eINSTANCE.createSCorpusGraph();
		saltProject.getSCorpusGraphs().add(sCorpGraph);
		SCorpus sCorpus= SaltFactory.eINSTANCE.createSCorpus();
		sCorpGraph.addSNode(sCorpus);
		SDocument sDoc= SaltFactory.eINSTANCE.createSDocument();
		sDoc.setSName("sample1");
		sDoc.setSDocumentGraph(sDocGraph);
		sCorpGraph.addSDocument(sCorpus, sDoc);
//		saltProject.saveSaltProject_DOT(URI.createFileURI("D:/Test/mytest"));
		
		this.getFixture().start(sDoc.getSElementId());
		
//		saltProject.saveSaltProject_DOT(URI.createFileURI("D:/Test/mytest2"));
		System.out.println(sDocGraph.getSLayers());
		
//		{//tests
//			SLayer topoLayer= null;
//			SLayer syntaxLayer= null;
//			for (SLayer sLayer: sDoc.getSLayers())
//			{
//				if (sLayer.getSName().equals(this.getFixture().topoLayerName))
//					topoLayer= sLayer;
//				else if (sLayer.getSName().equals(this.getFixture().syntaxLayerName))
//					syntaxLayer= sLayer;	
//			}
//			assertNotNull(topoLayer);
//			assertNotNull(syntaxLayer);
//			assertEquals(10, syntaxLayer.getSNodes().size());
//			assertEquals(9, syntaxLayer.getSRelations().size());
//			
//			assertEquals(9, topoLayer.getSNodes().size());
//			assertEquals(8, topoLayer.getSRelations().size());
//		}//tests
	}
	
	public SDocumentGraph createSDocGraph()
	{
		SDocumentGraph sDocGraph= SaltFactory.eINSTANCE.createSDocumentGraph();
		{//create SDocumentGraph
			STextualDS sText= null;
			{//create primary data
				sText= SaltFactory.eINSTANCE.createSTextualDS();
				sText.setSText("\"Der Kampf wird ein Ende finden, wenn es da ist.\"");
				sDocGraph.addSNode(sText);
			}//create primary data
			SToken tokAmp= SaltFactory.eINSTANCE.createSToken();
			SToken tokDer= SaltFactory.eINSTANCE.createSToken();
			SToken tokKampf= SaltFactory.eINSTANCE.createSToken();
			SToken tokWird= SaltFactory.eINSTANCE.createSToken();
			SToken tokEin= SaltFactory.eINSTANCE.createSToken();
			SToken tokEnde= SaltFactory.eINSTANCE.createSToken();
			SToken tokFinden= SaltFactory.eINSTANCE.createSToken();
			SToken tokComma= SaltFactory.eINSTANCE.createSToken();
			SToken tokWenn= SaltFactory.eINSTANCE.createSToken();
			SToken tokEs= SaltFactory.eINSTANCE.createSToken();
			SToken tokDa= SaltFactory.eINSTANCE.createSToken();
			SToken tokIst= SaltFactory.eINSTANCE.createSToken();
			SToken tokDot= SaltFactory.eINSTANCE.createSToken();
			SToken tokAmp2= SaltFactory.eINSTANCE.createSToken();
			
			{//create STokens
				STextualRelation sTextRel= null;
				
				tokAmp= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokAmp);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokAmp);
				sTextRel.setSStart(0);
				sTextRel.setSEnd(1);
				sDocGraph.addSRelation(sTextRel);
				
				tokDer= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokDer);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokDer);
				sTextRel.setSStart(1);
				sTextRel.setSEnd(4);
				sDocGraph.addSRelation(sTextRel);
				
				tokKampf= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokKampf);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokKampf);
				sTextRel.setSStart(5);
				sTextRel.setSEnd(10);
				sDocGraph.addSRelation(sTextRel);
				
				tokWird= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokWird);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokWird);
				sTextRel.setSStart(11);
				sTextRel.setSEnd(15);
				sDocGraph.addSRelation(sTextRel);
				
				tokEin= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokEin);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokEin);
				sTextRel.setSStart(17);
				sTextRel.setSEnd(25);
				sDocGraph.addSRelation(sTextRel);
				
				tokEnde= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokEnde);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokEnde);
				sTextRel.setSStart(20);
				sTextRel.setSEnd(24);
				sDocGraph.addSRelation(sTextRel);
				
				tokFinden= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokFinden);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokFinden);
				sTextRel.setSStart(25);
				sTextRel.setSEnd(31);
				sDocGraph.addSRelation(sTextRel);
				
				tokComma= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokComma);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokComma);
				sTextRel.setSStart(31);
				sTextRel.setSEnd(32);
				sDocGraph.addSRelation(sTextRel);
				
				tokWenn= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokWenn);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokWenn);
				sTextRel.setSStart(33);
				sTextRel.setSEnd(37);
				sDocGraph.addSRelation(sTextRel);
				
				tokEs= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokEs);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokEs);
				sTextRel.setSStart(38);
				sTextRel.setSEnd(40);
				sDocGraph.addSRelation(sTextRel);
				
				tokDa= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokDa);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokDa);
				sTextRel.setSStart(41);
				sTextRel.setSEnd(43);
				sDocGraph.addSRelation(sTextRel);
				
				tokIst= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokIst);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokIst);
				sTextRel.setSStart(44);
				sTextRel.setSEnd(47);
				sDocGraph.addSRelation(sTextRel);
				
				tokDot= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokDot);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokDot);
				sTextRel.setSStart(47);
				sTextRel.setSEnd(48);
				sDocGraph.addSRelation(sTextRel);
				
				tokAmp2= SaltFactory.eINSTANCE.createSToken();
				sDocGraph.addSNode(tokAmp2);
				sTextRel= SaltFactory.eINSTANCE.createSTextualRelation();
				sTextRel.setSTextualDS(sText);
				sTextRel.setSToken(tokAmp2);
				sTextRel.setSStart(48);
				sTextRel.setSEnd(49);
				sDocGraph.addSRelation(sTextRel);
			}//create STokens
			
			SStructure topNode= SaltFactory.eINSTANCE.createSStructure();
			topNode.createSAnnotation(null, "cat", "TOP");
			sDocGraph.addSNode(topNode);
			SDominanceRelation sDomRel= null;
			
			{//TOP --> tokAmp
				sDomRel= SaltFactory.eINSTANCE.createSDominanceRelation();
				sDomRel.setSSource(topNode);
				sDomRel.setSTarget(tokAmp);
				sDocGraph.addSRelation(sDomRel);
				sDomRel.createSAnnotation(null, "label", "--");
			}//TOP --> tokAmp
			
			SStructure sStruct_SIMPX= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_VF= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_NX1= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_LK= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_VXFIN= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_MF= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_NX2= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_VC= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_VXINF= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_NF= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_SIMPX2= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_C= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_MF2= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_NX3= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_ADVX= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_VC2= SaltFactory.eINSTANCE.createSStructure();
			SStructure sStruct_VXFIN2= SaltFactory.eINSTANCE.createSStructure();
			
			{//TOP --> SIMPX
				sDocGraph.addSNode(topNode, sStruct_SIMPX, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_SIMPX.createSAnnotation(null, "cat", "SIMPX");
			}//TOP --> SIMPX
			
			{//SIMPX --> VF
				sDocGraph.addSNode(sStruct_SIMPX, sStruct_VF, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_VF.createSAnnotation(null, "cat", "VF");
			}//SIMPX --> VF
			
			{//VF --> NX1
				sDocGraph.addSNode(sStruct_VF, sStruct_NX1, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "ON");
				sStruct_NX1.createSAnnotation(null, "cat", "NX");
			}//VF --> NX1
			
			{//NX1 --> Der
				sDocGraph.addSNode(sStruct_NX1, tokDer, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
			}//NX1 --> Der
			
			{//NX1 --> Kampf
				sDocGraph.addSNode(sStruct_NX1, tokKampf, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//NX1 --> Kampf
			
			{//SIMPX --> LK
				sDocGraph.addSNode(sStruct_SIMPX, sStruct_LK, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_LK.createSAnnotation(null, "cat", "LK");
			}//SIMPX --> LK
			
			{//LK --> VXFIN
				sDocGraph.addSNode(sStruct_LK, sStruct_VXFIN, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
				sStruct_VXFIN.createSAnnotation(null, "cat", "VXFIN");
			}//LK --> VXFIN
			
			{//VXFIN --> wird
				sDocGraph.addSNode(sStruct_VXFIN, tokWird, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//VXFIN --> wird
			
			
			{//SIMPX --> MF
				sDocGraph.addSNode(sStruct_SIMPX, sStruct_MF, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_MF.createSAnnotation(null, "cat", "MF");
			}//SIMPX --> MF
			
			{//MF --> NX2
				sDocGraph.addSNode(sStruct_MF, sStruct_NX2, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "OA");
				sStruct_NX2.createSAnnotation(null, "cat", "NX");
			}//MF --> NX2
			
			{//NX2 --> ein
				sDocGraph.addSNode(sStruct_NX2, tokEin, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
			}//NX2 --> ein
			
			{//NX2 --> Ende
				sDocGraph.addSNode(sStruct_NX2, tokEnde, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//NX2 --> Ende
			
			
			{//SIMPX --> VC
				sDocGraph.addSNode(sStruct_SIMPX, sStruct_VC, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_VC.createSAnnotation(null, "cat", "VC");
			}//SIMPX --> VC
			
			{//VC --> VXINF
				sDocGraph.addSNode(sStruct_VC, sStruct_VXINF, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "OV");
				sStruct_VXINF.createSAnnotation(null, "cat", "VXINF");
			}//VC --> VXINF
			
			{//VXINF --> finden
				sDocGraph.addSNode(sStruct_VXINF, tokFinden, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//VXINF --> finden
			
			{//SIMPX --> tokCOmma
				sDocGraph.addSNode(sStruct_SIMPX, tokComma, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
			}//SIMPX --> tokCOmma
			
			{//SIMPX --> NF
				sDocGraph.addSNode(sStruct_SIMPX, sStruct_NF, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_NF.createSAnnotation(null, "cat", "NF");
			}//SIMPX --> NF
			
			{//NF --> SIMPX
				sDocGraph.addSNode(sStruct_NF, sStruct_SIMPX2, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "MOD");
				sStruct_SIMPX2.createSAnnotation(null, "cat", "SIMPX");
			}//NF --> SIMPX
			
			{//SIMPX --> C
				sDocGraph.addSNode(sStruct_SIMPX2, sStruct_C, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_C.createSAnnotation(null, "cat", "C");
			}//SIMPX --> C
			
			{//C --> wenn
				sDocGraph.addSNode(sStruct_C, tokWenn, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
			}//C --> wenn
			
			{//SIMPX --> MF2
				sDocGraph.addSNode(sStruct_SIMPX2, sStruct_MF2, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_MF2.createSAnnotation(null, "cat", "MF");
			}//SIMPX --> MF2
			
			{//MF2 --> NX3
				sDocGraph.addSNode(sStruct_MF2, sStruct_NX3, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "ON");
				sStruct_NX3.createSAnnotation(null, "cat", "NX");
			}//MF2 --> NX3
			
			{//NX3 --> es
				sDocGraph.addSNode(sStruct_NX3, tokEs, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//NX3 --> es
			
			{//MF2 --> ADVX
				sDocGraph.addSNode(sStruct_MF2, sStruct_ADVX, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "PRED");
				sStruct_ADVX.createSAnnotation(null, "cat", "ADVX");
			}//MF2 --> ADVX
			
			{//NX3 --> es
				sDocGraph.addSNode(sStruct_ADVX, tokDa, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//NX3 --> es
			
			{//SIMPX --> VC2
				sDocGraph.addSNode(sStruct_SIMPX2, sStruct_VC2, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "--");
				sStruct_VC2.createSAnnotation(null, "cat", "VC");
			}//SIMPX --> VC2
			
			{//VC2 --> VXFIN2
				sDocGraph.addSNode(sStruct_VC2, sStruct_VXFIN2, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
				sStruct_VXFIN2.createSAnnotation(null, "cat", "VXFIN");
			}//VC2 --> VXFIN2
			
			{//VXFIN2 --> ist
				sDocGraph.addSNode(sStruct_VXFIN2, tokIst, STYPE_NAME.SDOMINANCE_RELATION).createSAnnotation(null, "label", "HD");
			}//VXFIN2 --> ist
			
			{//TOP --> tokDot
				sDomRel= SaltFactory.eINSTANCE.createSDominanceRelation();
				sDomRel.setSSource(topNode);
				sDomRel.setSTarget(tokDot);
				sDocGraph.addSRelation(sDomRel);
				sDomRel.createSAnnotation(null, "label", "--");
			}//TOP --> tokDot
			
			{//TOP --> tokAmp2
				sDomRel= SaltFactory.eINSTANCE.createSDominanceRelation();
				sDomRel.setSSource(topNode);
				sDomRel.setSTarget(tokAmp2);
				sDocGraph.addSRelation(sDomRel);
				sDomRel.createSAnnotation(null, "label", "--");
			}//TOP --> tokAmp2
			
			
			
		}//create SDocumentGraph
		return(sDocGraph);
	}
}
