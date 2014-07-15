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

import org.osgi.service.component.annotations.Component;

import de.hu_berlin.german.korpling.saltnpepper.pepper.modules.PepperMapper;
import de.hu_berlin.german.korpling.saltnpepper.pepper.modules.impl.PepperManipulatorImpl;
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
public class TueBaDZManipulator extends PepperManipulatorImpl 
{
	public TueBaDZManipulator()
	{
		super();
		//setting name of module
		this.setName("TueBaDZManipulator");
	}
	
	/**
	 * Creates a mapper of type {@link PAULA2SaltMapper}.
	 * {@inheritDoc PepperModule#createPepperMapper(SElementId)}
	 */
	@Override
	public PepperMapper createPepperMapper(SElementId sElementId)
	{
		Rearanger mapper= new Rearanger();
		return(mapper);
	}
}
