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
import static main.IoUtils.writeDotAndGenerateImage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import data.Entry;
import data.Workflow;

public class run_merging_purgeworkflows {

	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final String MERGED_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/merged/";

	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	private static final Map<String, Workflow> carmine = readResults(ROOT + "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");

	public static void main(String[] args) {
		for (String scenario : new String[] { "S1", "S2", "S3", "S4" }) {
			mergeScenario(scenario);
		}
	}

	private static void mergeScenario(String sid) {
		List<Workflow> toMerge = new LinkedList<>();
		for (String id : getSortedIds()) {
			if (id.startsWith(sid)) {
				if (joined.containsKey(id)) {
					toMerge.add(joined.get(id));
				} else if (carmine.containsKey(id)) {
					toMerge.add(carmine.get(id));
				} else if (seb.containsKey(id)) {
					toMerge.add(seb.get(id));
				}
			}
		}

		merge(sid, toMerge);

	}

	private static void merge(String sid, List<Workflow> toMerge) {
		Map<Entry, Integer> counts;
		Set<Entry> toDel = new HashSet<>();

		boolean isDone = true;
		do {
			isDone = true;
			counts = new HashMap<Entry, Integer>();
			outer: for (Workflow w : toMerge) {

				// kickout unwanted workflows
				for (Entry flagged : toDel) {
					if (w.elements.contains(flagged)) {

						Entry f = new Entry("Filtered");
						if (counts.containsKey(f)) {
							counts.put(f, counts.get(f) + 1);
						} else {
							counts.put(f, 1);
						}
						continue outer;
					}
				}

				// count the rest
				for (Entry e : w.elements) {
					if (counts.containsKey(e)) {
						counts.put(e, counts.get(e) + 1);
					} else {
						counts.put(e, 1);
					}
				}
			}

			for (Entry e : counts.keySet()) {
				if (e.isEdge() && counts.get(e) < run_merging_purgebranches.MIN_REQUIRED_COUNT) {
					toDel.add(e);
					isDone = false;
				}
			}
		} while (!isDone);

		StringBuilder sb = new StringBuilder();
		sb.append("strict digraph ").append(sid).append(" {\n");
		sb.append("\tforcelabels=true;\n");
		String label;
		for (Entry e : counts.keySet()) {
			int count = counts.get(e);
			sb.append("\t").append(e.toString());
			if (e.isEdge()) {
				double width = Math.max(0.1, round(Math.log10(count) * 3, 1));
				sb.append(" [penwidth=").append(width).append(",");
				label = String.format("%d", count);
			} else {
				sb.append(" [");
				label = String.format("%s (%d)", e.from, count);
			}
			sb.append("label=\"").append(label).append("\"];\n");
		}
		sb.append("}");

		writeDotAndGenerateImage(MERGED_ROOT, sid, sb.toString());
	}

	private static double round(double v, int scale) {
		double s = Math.pow(10, scale);
		return Math.round(v * s) / s;
	}

	private static SortedSet<String> getSortedIds() {
		return IoUtils.getSortedIds(joined, carmine, seb);
	}
}