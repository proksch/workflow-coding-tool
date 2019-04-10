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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import data.Entry;
import data.Workflow;

public class run_workflow_validation {

	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final String IMAGE_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/images/";
	private static final String COMMENT_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/statements/";

	private static final Set<String> LABELS = IoUtils.readLabels(ROOT + "labels.txt");
	private static final Set<String> USED_LABELS = new HashSet<String>();

	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	private static final Map<String, Workflow> carmine = readResults(ROOT + "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");

	public static void main(String[] args) {
		System.out.println("--- JOINED ---");
		validateWorkflows(joined.values());
		System.out.println("--- CARMINE ---");
		validateWorkflows(carmine.values());
		System.out.println("--- SEB ---");
		validateWorkflows(seb.values());

		System.out.println("Unused labels:");
		for (String l : LABELS) {
			if (!USED_LABELS.contains(l)) {
				System.err.printf("- %s\n", l);
			}
		}
		System.err.println();
	}

	private static void validateWorkflows(Iterable<Workflow> ws) {
		for (Workflow w : ws) {
			w.assertConsistency();
			validateWorkflow(w);
		}
	}

	private static void validateWorkflow(Workflow w) {
		for (Entry s : w.elements) {
			validateLabel(w.id, s.from);
			if (s.isEdge()) {
				validateLabel(w.id, s.to);
			}
		}

		for (String l : new String[] { "Invalid answer", "Not a problem in practice", "I don't know" }) {
			if (w.elements.contains(new Entry(l))) {
				if (w.elements.size() > 1) {
					System.err.printf("Scenario %s contains %s, but also other labels", w.id, l);
				}
			}
		}

		if (!containsOneOf(w.elements, "Error notification", "Invalid answer", "Not a problem in practice",
				"I don't know")) {
			System.err.println("No Start state for scenario " + w.id);
		}

		if (!containsOneOf(w.elements, "End", "Invalid answer", "Not a problem in practice", "I don't know")) {
			System.err.println("No End state for scenario " + w.id);
		}

		String cmt = COMMENT_ROOT + w.id + ".txt";
		if (!new File(cmt).exists()) {
			System.err.println("No statement found for scenario " + w.id);
		}

		String img = IMAGE_ROOT + w.id + ".dot";
		if (!new File(img).exists()) {
			System.err.println("No image found for scenario " + w.id);
		}

	}

	private static boolean containsOneOf(Set<Entry> elements, String... labels) {
		for (String l : labels) {
			if (elements.contains(new Entry(l))) {
				return true;
			}
		}
		return false;
	}

	private static void validateLabel(String id, String l) {
		if (!LABELS.contains(l)) {
			throw new RuntimeException(String.format("Problem for %s: Label %s does not exist", id, l));
		}
		USED_LABELS.add(l);
	}
}