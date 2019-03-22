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
package data;

import java.util.LinkedHashSet;
import java.util.Set;

public class Workflow {

	public final String id;
	public final Set<Entry> steps = new LinkedHashSet<>();

	public Workflow(String id) {
		this.id = id;
	}

	public void assertConsistency() {
		for (Entry e : steps) {
			if (e.isEdge()) {
				boolean isFromDefined = steps.contains(e.getFrom());
				boolean isToDefined = steps.contains(e.getTo());
				if (!(isFromDefined && isToDefined)) {
					throw new RuntimeException();
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((steps == null) ? 0 : steps.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Workflow other = (Workflow) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (steps == null) {
			if (other.steps != null)
				return false;
		} else if (!steps.equals(other.steps))
			return false;
		return true;
	}

	public static Workflow of(String id, String... es) {
		Workflow w = new Workflow(id);
		for (String e : es) {
			if (e.contains(">")) {
				String a = e.substring(0, e.indexOf(">")).trim();
				String b = e.substring(e.indexOf(">") + 1).trim();
				w.steps.add(new Entry(a, b));
			} else {
				w.steps.add(new Entry(e.trim()));
			}
		}
		return w;
	}
}