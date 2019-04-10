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

import static java.lang.String.format;

public class Entry {

	public final String from;
	public final String to;

	public Entry(String a) {
		assertNotNull(a);
		this.from = a;
		this.to = null;
	}

	public Entry(String a, String b) {
		assertNotNull(a);
		assertNotNull(b);
		this.from = a;
		this.to = b;
	}

	private void assertNotNull(Object o) {
		if (o == null) {
			throw new RuntimeException("parameter is null");
		}
	}

	public boolean isNode() {
		return to == null;
	}

	public boolean isEdge() {
		return !isNode();
	}

	public Entry getFrom() {
		return new Entry(from);
	}

	public Entry getTo() {
		return new Entry(to);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Entry other = (Entry) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (isNode()) {
			return format("\"%s\"", from);
		} else {
			return format("\"%s\" -> \"%s\"", from, to);
		}
	}
}