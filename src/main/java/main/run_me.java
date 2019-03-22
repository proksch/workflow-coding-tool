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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import data.Workflow;
import parser.MyParser;
import parser.PseudoDotParser;
import parser.PseudoDotParser.DigraphContext;
import parser.PseudoDotParser.EdgeContext;
import parser.PseudoDotParser.GraphsContext;

public class run_me {
	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";

	private static final Set<String> LABELS = readLabels(ROOT + "labels.txt");
	private static final Set<String> USED_LABELS = new HashSet<String>();

	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	// private static final Map<String, Workflow> carmine = readResults(ROOT +
	// "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");

	public static void main(String[] args) throws IOException {

		// TODO .. do something with data

		System.out.println("Unused labels:");
		for (String label : LABELS) {
			if (!USED_LABELS.contains(label)) {
				System.out.printf("- %s\n", label);
			}
		}

	}

	private static Map<String, Workflow> readResults(String path) {
		System.out.println("searching for results in: " + path);
		Map<String, Workflow> res = new HashMap<String, Workflow>();
		Iterator<File> it = findFiles(path, f -> f.endsWith(".dot"));
		while (it.hasNext()) {
			File f = it.next();
			PseudoDotParser parser = MyParser.parse(f);
			List<Workflow> ws = parseWorkflows(parser, f.getAbsolutePath());
		}

		return res;
	}

	private static List<Workflow> parseWorkflows(PseudoDotParser parser, String path) {
		List<Workflow> ws = new LinkedList<>();

		GraphsContext graphs = parser.graphs();

		for (DigraphContext d : graphs.digraph()) {
			String id = d.ID().getText();
			System.out.println("ID: " + id);
			Workflow w = new Workflow(id);

			for (EdgeContext e : d.edge()) {
				List<TerminalNode> curLabels = e.LABEL();

				if (curLabels.size() == 1) {
					String l = curLabels.iterator().next().getText();
					l = l.substring(1, l.length() - 1);

					System.out.printf("%s;\n", l);
				} else {
					String last = null;
					for (TerminalNode l : curLabels) {
						String label = l.getText();
						label = label.substring(1, label.length() - 1);
						if (!LABELS.contains(label)) {
							throw new RuntimeException(
									"Unkown label: " + label + " (" + path + ", " + l.getSourceInterval() + ")");
						}
						USED_LABELS.add(label);

						if (last != null) {
							System.out.printf("%s -> %s;\n", last, label);
						}
						last = label;
					}
				}
			}
			ws.add(w);
		}
		return ws;
	}

	private static Iterator<File> findFiles(String path, Predicate<String> predicate) {
		IOFileFilter fileFilter = new AbstractFileFilter() {
			@Override
			public boolean accept(File file) {
				return predicate.test(file.getAbsolutePath());
			}
		};
		IOFileFilter allDirs = FileFilterUtils.trueFileFilter();
		Iterator<File> it = FileUtils.iterateFiles(new File(path), fileFilter, allDirs);
		return it;
	}

	private static Set<String> readLabels(String path) {
		try {
			Set<String> labels = new LinkedHashSet<>();
			for (String line : FileUtils.readLines(new File(path), UTF_8)) {
				if (line.contains("#")) {
					line = line.substring(0, line.indexOf("#"));
				}
				line = line.trim();

				if (line.startsWith("-") || line.startsWith("*")) {
					line = line.substring(1).trim();
				}

				if (line.isEmpty()) {
					continue;
				}

				labels.add(line);
			}

			return labels;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}