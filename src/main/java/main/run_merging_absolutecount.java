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
import static main.run_merging_purgebranches.MIN_REQUIRED_COUNT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import data.Entry;
import data.Workflow;

public class run_merging_absolutecount {

	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final String MERGED_ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/merged/";

	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	private static final Map<String, Workflow> carmine = readResults(ROOT + "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");

	public static void main(String[] args) {
		System.out.println("\nMinimum Count: >=" + MIN_REQUIRED_COUNT);
		for (String scenario : new String[] { "S1", "S2", "S3", "S4", "All" }) {
			mergeScenario(scenario);
		}
	}

	private static void mergeScenario(String sid) {

		List<Workflow> toMerge = new LinkedList<>();
		for (String id : getSortedIds()) {
			if (id.startsWith(sid) || "All".equals(sid)) {
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

		BranchPurging bp = new BranchPurging();

		boolean isDone = true;
		do {
			isDone = true;
			counts = new HashMap<Entry, Integer>();
			outer: for (Workflow w : toMerge) {

				// kickout unwanted workflows
				for (Entry flagged : toDel) {
					if (w.elements.contains(flagged)) {
						w = bp.purge(w, toDel);
					}
				}

				// count
				for (Entry e : w.elements) {
					if (counts.containsKey(e)) {
						counts.put(e, counts.get(e) + 1);
					} else {
						counts.put(e, 1);
					}
				}
			}

			for (Entry e : counts.keySet()) {
				if (e.isEdge() && counts.get(e) < MIN_REQUIRED_COUNT) {
					toDel.add(e);
					isDone = false;
				}
			}
		} while (!isDone);

		Map<Entry, Integer> unfilteredCounts = new HashMap<>();
		for (Workflow w : toMerge) {
			for (Entry e : w.elements) {
				if (unfilteredCounts.containsKey(e)) {
					unfilteredCounts.put(e, unfilteredCounts.get(e) + 1);
				} else {
					unfilteredCounts.put(e, 1);
				}
			}
		}

		System.out.printf("\n== %s ==\n", sid);
		for (Entry e : unfilteredCounts.keySet()) {
			if (e.isEdge()) {
				continue;
			}
			int uc = 0;
			int fc = 0;
			if (unfilteredCounts.containsKey(e)) {
				uc = unfilteredCounts.get(e);
			}
			if (counts.containsKey(e)) {
				fc = counts.get(e);
			}
			int delta = uc - fc;
			if (fc == 0 && delta >= MIN_REQUIRED_COUNT) {
				System.out.printf("%s\t%d\n", e.from, delta);
			}
		}
	}

	private static double round(double v, int scale) {
		double s = Math.pow(10, scale);
		return Math.round(v * s) / s;
	}

	private static SortedSet<String> getSortedIds() {
		return IoUtils.getSortedIds(joined, carmine, seb);
	}
}