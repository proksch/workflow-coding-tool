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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import data.Entry;
import data.Workflow;

public class BranchPurging {

	public Workflow purge(Workflow in, Set<Entry> toDel) {

		Workflow w = new Workflow(in.id);
		w.elements.addAll(in.elements);

		for (Entry e : toDel) {
			if (w.elements.contains(e)) {
				w = purge(w, e);
			}
		}

		return w;
	}

	private static Workflow purge(Workflow w, Entry e) {

		w.elements.remove(e);

		if (outgoingEdges(e.from, w).size() == 0) {
			w.elements.remove(new Entry(e.from));

			for (Entry in : incomingEdges(e.from, w)) {
				w = purge(w, in);
			}
		}

		if (incomingEdges(e.to, w).size() == 0) {
			w.elements.remove(new Entry(e.to));

			for (Entry out : outgoingEdges(e.to, w)) {
				w = purge(w, out);
			}
		}

		return addFilterNode(w);
	}

	private static Workflow addFilterNode(Workflow w) {
		if (w.elements.size() == 0) {
			w.elements.add(new Entry("Filtered"));
		} else {
			w.elements.add(new Entry("Partially Filtered"));
		}
		return w;
	}

	private static Set<Entry> incomingEdges(String l, Workflow w) {
		return edges(l, w, e -> e.to);
	}

	private static Set<Entry> outgoingEdges(String l, Workflow w) {
		return edges(l, w, e -> e.from);
	}

	private static Set<Entry> edges(String l, Workflow w, Function<Entry, Object> getTarget) {
		HashSet<Entry> es = new HashSet<>();
		for (Entry e : w.elements) {
			if (e.isEdge() && l.equals(getTarget.apply(e))) {
				es.add(e);
			}
		}
		return es;
	}
}