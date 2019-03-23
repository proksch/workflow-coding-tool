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

	public final String a;
	public final String b;

	public Entry(String a) {
		assertNotNull(a);
		this.a = a;
		this.b = null;
	}

	public Entry(String a, String b) {
		assertNotNull(a);
		assertNotNull(b);
		this.a = a;
		this.b = b;
	}

	private void assertNotNull(Object o) {
		if (o == null) {
			throw new RuntimeException("parameter is null");
		}
	}

	public boolean isNode() {
		return b == null;
	}

	public boolean isEdge() {
		return !isNode();
	}

	public Entry getFrom() {
		return new Entry(a);
	}

	public Entry getTo() {
		return new Entry(b);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
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
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (isNode()) {
			return format("\"%s\"", a);
		} else {
			return format("\"%s\" -> \"%s\"", a, b);
		}
	}
}