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
package de.hu_berlin.german.korpling.saltnpepper.pepperModules.tuebadzModules;

import org.corpus_tools.pepper.common.PepperConfiguration;
import org.corpus_tools.pepper.impl.PepperManipulatorImpl;
import org.corpus_tools.pepper.modules.PepperMapper;
import org.corpus_tools.salt.graph.Identifier;
import org.eclipse.emf.common.util.URI;
import org.osgi.service.component.annotations.Component;

/**
 * This manipulator was developed especially for the TueBaDZ Corpus. It creates
 * a SSpan-objects for every SToken object in the document. All annotations for
 * STokens will be duplicated and added to the spans. The annotations of the
 * tokens will be renamed from "annoName" to "annoName." For example a
 * "pos"-annotation of SToken-object will be renamedto a "pos."-annotation. All
 * spans, tokens and spanning relations will be added to an artificial layer
 * named "TueBaDZ".
 * 
 * @author Florian Zipser
 * @version 1.0
 *
 */
@Component(name = "TueBaDZManipulatorComponent", factory = "PepperManipulatorComponentFactory")
public class TueBaDZManipulator extends PepperManipulatorImpl {
	public TueBaDZManipulator() {
		super();
		setSupplierContact(URI.createURI(PepperConfiguration.EMAIL));
		setSupplierHomepage(URI.createURI("https://github.com/korpling/pepperModules-TueBaDZModules"));
		setDesc("This manipulator was developed especially for the TueBaDZ Corpus. It creates a SSpan-objects for every SToken object in the document. All annotations for STokens will be duplicated and added to the spans. The annotations of the tokens will be renamed from 'annoName' to 'annoName.' For example a 'pos'-annotation of SToken-object will be renamedto a 'pos.'-annotation. All spans, tokens and spanning relations will be added to an artificial layer named 'TueBaDZ'. ");
		// setting name of module
		this.setName("TueBaDZManipulator");
	}

	/**
	 * Creates a mapper of type {@link PAULA2SaltMapper}. {@inheritDoc
	 * PepperModule#createPepperMapper(Identifier)}
	 */
	@Override
	public PepperMapper createPepperMapper(Identifier sElementId) {
		Rearanger mapper = new Rearanger();
		return (mapper);
	}
}
