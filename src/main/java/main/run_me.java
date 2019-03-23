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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import data.Entry;
import data.IdComparator;
import data.Workflow;
import parser.MyParser;
import parser.PseudoDotParser;
import parser.PseudoDotParser.DigraphContext;
import parser.PseudoDotParser.EdgeContext;
import parser.PseudoDotParser.GraphsContext;

public class run_me {
	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final String IMAGE_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/images/";
	private static final String COMMENT_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/statements/";

	private static final Set<String> LABELS = readLabels(ROOT + "labels.txt");
	private static final Set<String> USED_LABELS = new HashSet<String>();

	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	private static final Map<String, Workflow> carmine = readResults(ROOT + "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");

	public static void main(String[] args) throws IOException {

		for (String id : getSortedIds()) {
			if (joined.containsKey(id)) {
				createImage(joined.get(id));
			} else {
				Workflow c = carmine.containsKey(id) ? carmine.get(id) : new Workflow(id);
				Workflow s = seb.containsKey(id) ? seb.get(id) : new Workflow(id);
				createImage(c, s);
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html><body style=\"font-family: Arial;\">\n");

		sb.append("<div style=\"margin-bottom: 5px; text-align: right;\">");
		sb.append(
				"<span style=\"background-color: #cccccc; padding: 2px 5px; border-radius: 3px;\">Agreement</span>\n");
		sb.append("<span style=\"background-color: #3ab56d; padding: 2px 5px; border-radius: 3px;\">Seb</span>\n");
		sb.append("<span style=\"background-color: #f07f7d; padding: 2px 5px; border-radius: 3px;\">Carmine</span>\n");
		sb.append("</div>");

		int lastP = -1;

		for (String id : getSortedIds()) {
			int thisP = Integer.parseInt(id.substring(id.indexOf('P') + 1));
			int thisS = Integer.parseInt(id.substring(1, id.indexOf('P')));

			if (thisP != lastP) {
				sb.append(
						"<h1 style=\"clear: left; margin: 0; background-color: black; color: white; padding: 3px; text-align: center;\">Participant ")
						.append(thisP).append("</h1>");
				lastP = thisP;
			}

			sb.append("<div style=\"width: 22%; float: left; margin: 10px 30px 0 0;\">");
			sb.append("<h2 style=\"margin: 0;\"> Scenario ").append(thisS);
			if (joined.containsKey(id)) {
				sb.append(" (joined)");
			}
			sb.append("</h2>\n");
			sb.append("<p>").append(readComment(id)).append("</p>");
			sb.append("<img src=\"").append(id).append(".png\" style=\"max-width: 100%;\" />");
			sb.append("</div>");
		}
		sb.append("</body></html>\n");
		FileUtils.writeStringToFile(new File(IMAGE_ROOT + "index.html"), sb.toString(), UTF_8);

		reportUnusedLabels();
	}

	private static String readComment(String id) {
		try {
			String path = COMMENT_ROOT + id + ".txt";
			String cnt = FileUtils.readFileToString(new File(path), UTF_8);
			cnt = cnt.replaceAll("\n", "<br />\n");
			return cnt;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void createImage(Workflow w) {
		StringBuilder sb = newGraph(w.id);
		for (Entry e : w.steps) {
			sb.append('\t').append(e).append(" [color=\"gray80\"];\n");
		}
		sb.append("}\n");
		write(w.id, sb.toString());
	}

	private static void createImage(Workflow a, Workflow b) {
		StringBuilder sb = newGraph(a.id);

		for (Entry ea : a.steps) {
			sb.append('\t').append(ea);
			if (b.steps.contains(ea)) {
				sb.append(" [color=\"gray80\"]");
			} else {
				sb.append(" [color=\"lightcoral\"]");
			}
			sb.append(";\n");
		}

		for (Entry eb : b.steps) {
			if (!a.steps.contains(eb)) {
				sb.append('\t').append(eb).append(" [color=\"mediumseagreen\"];\n");
			}
		}

		sb.append("}\n");
		write(a.id, sb.toString());
	}

	private static StringBuilder newGraph(String id) {
		StringBuilder sb = new StringBuilder();

		sb.append("strict digraph ").append(id).append(" {\n");
		sb.append("\tnode [style=filled];\n");
		return sb;
	}

	private static void write(String id, String s) {
		try {
			String imgPath = IMAGE_ROOT + id + ".dot";
			FileUtils.writeStringToFile(new File(imgPath), s, UTF_8);

			Process p = Runtime.getRuntime().exec("/opt/local/bin/dot " + id + ".dot -T png -o " + id + ".png", null,
					new File(IMAGE_ROOT));
			p.waitFor();

			printStream(p.getInputStream());
			printStream(p.getErrorStream());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void printStream(InputStream is) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String o = null;
			while ((o = br.readLine()) != null) {
				System.err.println(o);
			}
		}
	}

	private static void reportUnusedLabels() {
		System.out.println("Unused labels:");
		for (String label : LABELS) {
			if (!USED_LABELS.contains(label)) {
				System.out.printf("- %s\n", label);
			}
		}
	}

	private static SortedSet<String> getSortedIds() {
		SortedSet<String> ids = new TreeSet<>(new IdComparator());
		ids.addAll(joined.keySet());
		ids.addAll(carmine.keySet());
		ids.addAll(seb.keySet());
		return ids;
	}

	private static Map<String, Workflow> readResults(String path) {
		System.out.println("searching for results in: " + path);
		Map<String, Workflow> res = new HashMap<String, Workflow>();
		Iterator<File> it = findFiles(path, f -> f.endsWith(".dot"));
		while (it.hasNext()) {
			File f = it.next();
			PseudoDotParser parser = MyParser.parse(f);
			for (Workflow w : parseWorkflows(parser, f.getAbsolutePath())) {
				res.put(w.id, w);
			}
		}

		return res;
	}

	private static List<Workflow> parseWorkflows(PseudoDotParser parser, String path) {
		List<Workflow> ws = new LinkedList<>();

		GraphsContext graphs = parser.graphs();

		for (DigraphContext d : graphs.digraph()) {
			String id = d.ID().getText();
			Workflow w = new Workflow(id);

			for (EdgeContext e : d.edge()) {
				List<TerminalNode> curLabels = e.LABEL();

				if (curLabels.size() == 1) {
					String l = curLabels.iterator().next().getText();
					l = l.substring(1, l.length() - 1);
					w.steps.add(new Entry(l));
				} else {
					String last = null;
					for (TerminalNode l : curLabels) {
						String label = l.getText();
						label = label.substring(1, label.length() - 1);
						assertLabelExists(path, l, label);
						USED_LABELS.add(label);
						w.steps.add(new Entry(label));
						if (last != null) {
							w.steps.add(new Entry(last, label));
						}
						last = label;
					}
				}
			}
			w.assertConsistency();
			ws.add(w);
			System.out.println(w);
			System.out.println("------------");
		}
		return ws;
	}

	private static void assertLabelExists(String path, TerminalNode l, String label) {
		if (!LABELS.contains(label)) {
			throw new RuntimeException("Unkown label: " + label + " (" + path + ", " + l.getSourceInterval() + ")");
		}
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