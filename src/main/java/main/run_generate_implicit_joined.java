/**
 * Copyright 2019 Sebastian Proksch
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package main;

import static main.IoUtils.readResults;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Workflow;

public class run_generate_implicit_joined {
	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	private static final Map<String, Workflow> carmine = readResults(ROOT + "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");
	private static final String implicit = ROOT + "joined/implicit-agreement-for-all-training-cases.dot";

	private static final List<Workflow> implicits = new LinkedList<>();

	public static void main(String[] args) {
		
		if(new File(implicit).exists()) {
			throw new RuntimeException("The file 'implicit.dot' must not exist. Delete first, before running this script!");
		}

		// training 1
		extractImplicitAgreement(1, 1, 20);
		extractImplicitAgreement(2, 21, 40);
		extractImplicitAgreement(3, 41, 60);
		extractImplicitAgreement(4, 61, 80);

		// training 2
		extractImplicitAgreement(1, 21, 50);
		extractImplicitAgreement(2, 1, 20);
		extractImplicitAgreement(2, 41, 50);
		extractImplicitAgreement(3, 1, 30);
		extractImplicitAgreement(4, 1, 30);

		IoUtils.write(implicits, new File(implicit));
	}

	private static void extractImplicitAgreement(int sid, int fromIncl, int toIncl) {
		for (int i = fromIncl; i <= toIncl; i++) {
			String sp = String.format("S%dP%d", sid, i);

			if (joined.containsKey(sp)) {
				continue;
			}

			Workflow a = carmine.get(sp);
			Workflow b = seb.get(sp);
			if (!a.equals(b)) {
				throw new RuntimeException(
						String.format("Should not be different: %s\nCarmine: %s\nSeb: %s", sp, a, b));
			}

			implicits.add(a);
		}
	}
}