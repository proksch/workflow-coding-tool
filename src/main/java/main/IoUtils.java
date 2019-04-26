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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

public class IoUtils {

	public static String readComment(String root, String id) {
		try {
			String path = root + id + ".txt";
			String cnt = FileUtils.readFileToString(new File(path), UTF_8);
			cnt = cnt.replaceAll("\n", "<br />\n");
			return cnt;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String, Workflow> readResults(String path) {
		System.out.println("searching for results in: " + path);
		Map<String, Workflow> res = new HashMap<String, Workflow>();
		Iterator<File> it = findFiles(path, f -> f.endsWith(".dot"));
		while (it.hasNext()) {
			File f = it.next();
			PseudoDotParser parser = MyParser.parse(f);
			for (Workflow w : parseWorkflows(parser, f.getAbsolutePath())) {
				if (res.containsKey(w.id)) {
					System.err.println("duplicate id: " + w.id);
				}
				res.put(w.id, w);
			}
		}

		return res;
	}

	public static Iterator<File> findFiles(String path, Predicate<String> predicate) {
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

	public static List<Workflow> parseWorkflows(PseudoDotParser parser, String path) {
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
					w.elements.add(new Entry(l));
				} else {
					String last = null;
					for (TerminalNode l : curLabels) {
						String label = l.getText();
						label = label.substring(1, label.length() - 1);
						w.elements.add(new Entry(label));
						if (last != null) {
							w.elements.add(new Entry(last, label));
						}
						last = label;
					}
				}
			}
			w.assertConsistency();
			ws.add(w);
		}
		return ws;
	}

	public static Set<String> readLabels(String path) {
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

	public static SortedSet<String> getSortedIds(Map<String, Workflow>... ws) {
		SortedSet<String> ids = new TreeSet<>(new IdComparator());
		for (Map<String, Workflow> w : ws) {
			ids.addAll(w.keySet());
		}
		return ids;
	}

	public static void writeDotAndGenerateImage(String root, String id, String s) {
		try {
			String imgPath = root + id + ".dot";
			FileUtils.writeStringToFile(new File(imgPath), s, UTF_8);

			Process p = Runtime.getRuntime().exec("/opt/local/bin/dot " + id + ".dot -T png -o " + id + ".png", null,
					new File(root));
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

	public static void write(List<Workflow> wfs, File file) {
		try {
			StringBuilder sb = new StringBuilder();

			for(Workflow wf : wfs) {
				printWorkflow(wf, sb);
			}
			
			FileUtils.writeStringToFile(file, sb.toString(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void printWorkflow(Workflow wf, StringBuilder sb) {
		sb.append("strict digraph ").append(wf.id).append(" {\n");
		
		for(Entry e : wf.elements) {
			sb.append('\t').append(e).append(";\n");
		}
		
		sb.append("}\n");
	}
}