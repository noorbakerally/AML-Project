/******************************************************************************
* Copyright 2013-2016 LASIGE                                                  *
*                                                                             *
* Licensed under the Apache License, Version 2.0 (the "License"); you may     *
* not use this file except in compliance with the License. You may obtain a   *
* copy of the License at http://www.apache.org/licenses/LICENSE-2.0           *
*                                                                             *
* Unless required by applicable law or agreed to in writing, software         *
* distributed under the License is distributed on an "AS IS" BASIS,           *
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    *
* See the License for the specific language governing permissions and         *
* limitations under the License.                                              *
*                                                                             *
*******************************************************************************
* Runs AgreementMakerLight in either GUI mode (if no command line arguments   *
* are given) or in CLI mode (otherwise).                                      *
* Example 1 - Use AML in automatic mach mode and save the output alignment:   *
* java AMLCommandLine -s store/anatomy/mouse.owl -t store/anatomy/human.owl   *
* -o store/anatomy/alignment.rdf -a                                           *
* Example 2 - Use AML in manual match mode and evaluate the alignment:        *
* java AMLCommandLine -s store/anatomy/mouse.owl -t store/anatomy/human.owl   *
* -i store/anatomy/reference.rdf -m                                           *
* Example 3 - Use AML in repair mode and save the repaired alignment:         *
* java AMLCommandLine -s store/anatomy/mouse.owl -t store/anatomy/human.owl   *
* -i store/anatomy/toRepair.rdf -r -o store/anatomy/repaired.rdf              *
*                                                                             *
* @author Daniel Faria                                                        *
******************************************************************************/
package aml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import aml.Generic.Correspondence;
import aml.match.Mapping;
import aml.ontology.Ontology;
import aml.settings.MatchStep;
import aml.settings.NeighborSimilarityStrategy;
import aml.settings.SelectionType;
import aml.settings.StringSimMeasure;
import aml.settings.WordMatchStrategy;

import com.google.gson.Gson;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.*;

public class Main
{
	
//Attributes
	
	//Path to the config file
	private static final String CONFIG = "store/config.ini";
	//Path to the background knowledge directory
	private static final String BK_PATH = "store/knowledge/";
	//Path where AML is running
	private static String dir;
	//The AML instance
	private static AML aml;
    
//Main Method
	
	/**
	 * Runs AgreementMakerLight in GUI or CLI mode
	 * depending on whether arguments are given
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, URISyntaxException {


		//String ontFromIRI = "file:///home/noor/Downloads/dogont.owl";
		//String ontToIRI = "https://www.w3.org/ns/ssn/ssn.rdf";

		String ontFromIRI = args[0].replaceAll("-from=","");
		String ontToIRI = args[1].replaceAll("-to=","");

		System.out.println("Ontology from:"+ontFromIRI);
		System.out.println("Ontology to:"+ontToIRI);

		AML aml = AML.getInstance();
		aml.openOntologies(ontFromIRI, ontToIRI);
		aml.matchAuto();
		System.out.println("Number of Alignment:"+aml.getAlignment().size());
		Set<Correspondence> correspondenceList = new HashSet<Correspondence>();


		for (Mapping mapping:aml.getAlignment()){

			Correspondence correspondence = new Correspondence();

			correspondence.iriFrom = mapping.getSourceURI();
			correspondence.iriTo = mapping.getTargetURI();
			correspondence.confidence = BigDecimal.valueOf(mapping.getSimilarity());

			//Correspondence
			if (mapping.getRelationship().equals("subclass")){
				correspondence.rel = Correspondence.relation.SUBCLASS;
			}
			if (mapping.getRelationship().equals("superclass")){
				correspondence.rel = Correspondence.relation.SUPERCLASS;
			}
			if (mapping.getRelationship().equals("equivalence")){
				correspondence.rel = Correspondence.relation.EQUIVALENT;
			}
			correspondenceList.add(correspondence);
		}


		Gson gson = new Gson();
		System.out.println("#EncodedMapping:"+gson.toJson(correspondenceList)+"#");



	}
}