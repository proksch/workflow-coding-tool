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
import static main.IoUtils.readComment;
import static main.IoUtils.readLabels;
import static main.IoUtils.readResults;
import static main.IoUtils.writeDotAndGenerateImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;

import data.Entry;
import data.Workflow;

public class run_html_overview {
	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final String IMAGE_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/images/";
	private static final String COMMENT_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/statements/";

	private static final Set<String> LABELS = readLabels(ROOT + "labels.txt");

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
			sb.append("<p>").append(readComment(COMMENT_ROOT, id)).append("</p>");
			sb.append("<img src=\"").append(id).append(".png\" style=\"max-width: 100%;\" />");
			sb.append("</div>");
		}
		sb.append("</body></html>\n");
		FileUtils.writeStringToFile(new File(IMAGE_ROOT + "index.html"), sb.toString(), UTF_8);
	}

	private static SortedSet<String> getSortedIds() {
		return IoUtils.getSortedIds(joined, carmine, seb);
	}

	private static void createImage(Workflow w) {
		StringBuilder sb = newGraph(w.id);
		for (Entry e : w.elements) {
			sb.append('\t').append(e).append(" [color=\"gray80\"];\n");
		}
		sb.append("}\n");
		writeDotAndGenerateImage(IMAGE_ROOT, w.id, sb.toString());
	}

	private static void createImage(Workflow a, Workflow b) {
		StringBuilder sb = newGraph(a.id);

		for (Entry ea : a.elements) {
			sb.append('\t').append(ea);
			if (b.elements.contains(ea)) {
				sb.append(" [color=\"gray80\"]");
			} else {
				sb.append(" [color=\"lightcoral\"]");
			}
			sb.append(";\n");
		}

		for (Entry eb : b.elements) {
			if (!a.elements.contains(eb)) {
				sb.append('\t').append(eb).append(" [color=\"mediumseagreen\"];\n");
			}
		}

		sb.append("}\n");
		writeDotAndGenerateImage(IMAGE_ROOT, a.id, sb.toString());
	}

	private static StringBuilder newGraph(String id) {
		StringBuilder sb = new StringBuilder();

		sb.append("strict digraph ").append(id).append(" {\n");
		sb.append("\tnode [style=filled];\n");
		return sb;
	}
}