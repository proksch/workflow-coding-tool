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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import data.Entry;
import data.Workflow;

public class run_similarity {
	private static final String ROOT = "/Users/seb/versioned/documents/bart-paper/resources/survey_responses/";
	private static final Map<String, Workflow> joined = readResults(ROOT + "joined/");
	private static final Map<String, Workflow> carmine = readResults(ROOT + "carmine/");
	private static final Map<String, Workflow> seb = readResults(ROOT + "seb/");

	private static final IDistance DIST = new HammingDistance();

	public static void main(String[] args) {

		int num_total = 0;
		int num_joined = 0;
		int num_exact = 0;
		int num_invalid = 0;
		List<Double> similarities = new LinkedList<>();

		for (String id : getSortedIds()) {
			System.out.printf("%s: ", id);
			num_total++;

			if (joined.containsKey(id)) {
				System.out.println("joined");
				num_joined++;
				continue;
			}

			Workflow a = carmine.get(id);
			Workflow b = seb.get(id);

			if (a.equals(b)) {
				System.out.println("exact match");
				num_exact++;
				continue;
			}
			
			
			if(isInvalid(a) || isInvalid(b)) {
				System.out.println("one said invalid");
				num_invalid++;
				continue;
			}

			double distance = DIST.distance(a, b) * 100;
			similarities.add(distance);
			System.out.printf("%.2f%%\n", distance);
		}

		int num_separated = num_total - num_joined;

		System.out.println("------------");
		System.out.printf("total rating: %d (%d joined, %d separated)\n", num_total, num_joined, num_separated);
		System.out.printf("exact matches: %d (%.1f%%)\n", num_exact, (100 * num_exact / (double) num_separated));
		System.out.printf("one of us said \"Invalid answer\": %d (%.1f%%)\n", num_invalid, (100 * num_invalid / (double) num_separated));
		System.out.printf("partial matches: %d (avg similarity: %.1f%%)\n", similarities.size(),
				getAverage(similarities));
		System.out.printf("\n");

	}

	private static boolean isInvalid(Workflow w) {
		return w.elements.contains(new Entry("Invalid answer"));
	}

	private static double getAverage(Collection<Double> ds) {
		double sum = 0;
		for (double d : ds) {
			sum += d;
		}
		return sum / (double) ds.size();
	}

	private static SortedSet<String> getSortedIds() {
		return IoUtils.getSortedIds(joined, carmine, seb);
	}
}