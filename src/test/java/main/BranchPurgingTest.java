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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import data.Entry;
import data.Workflow;

public class BranchPurgingTest {

	private BranchPurging sut;

	private Workflow in;
	private Workflow actual;
	private Workflow expected;

	@Before
	public void setup() {
		sut = new BranchPurging();
	}

	@Test
	public void partiallyFilteredWithShortBranch_Backwards() {
		in = w("a>b", "b>c", "c>d", "b>x", "x>c");
		actual = sut.purge(in, d("x>c"));
		expected = w("a>b", "b>c", "c>d", "Partially Filtered");

		assertEquals(expected, actual);
	}

	@Test
	public void partiallyFilteredWithShortBranch_Forwards() {
		in = w("a>b", "b>c", "c>d", "b>x", "x>c");
		actual = sut.purge(in, d("b>x"));
		expected = w("a>b", "b>c", "c>d", "Partially Filtered");

		assertEquals(expected, actual);
	}

	@Test
	public void partiallyFilteredWithLongerBranch() {
		in = w("a>b", "b>c", "c>d", "b>x", "x>y", "y>c");
		actual = sut.purge(in, d("b>x"));
		expected = w("a>b", "b>c", "c>d", "Partially Filtered");

		assertEquals(expected, actual);
	}

	@Test
	public void fullyFiltered() {
		in = w("a>b", "b>c", "c>d");
		actual = sut.purge(in, d("b>c"));
		expected = w("Filtered");

		assertEquals(expected, actual);
	}

	@Test
	public void nothingIsFilteref() {
		in = w("a>b", "b>c");
		actual = sut.purge(in, d("c>d"));
		expected = w("a>b", "b>c");

		assertEquals(expected, actual);
	}

	@Test
	public void newReferenceIsCreatedForChanges() {
		in = w("a>b", "b>c");
		actual = sut.purge(in, d("b>c"));

		assertNotSame(in, actual);
	}

	@Test
	public void newReferenceIsCreatedWithoutChange() {
		in = w("a>b", "b>c");
		actual = sut.purge(in, d("c>d"));

		assertNotSame(in, actual);
	}

	private Set<Entry> d(String... transisitions) {
		HashSet<Entry> s = new HashSet<>();
		for (String t : transisitions) {
			String[] parts = getParts(t);
			s.add(new Entry(parts[0], parts[1]));
		}
		return s;
	}

	private static Workflow w(String... transisitions) {
		Workflow w = new Workflow("X");
		for (String t : transisitions) {
			if (t.contains(">")) {
				String[] parts = getParts(t);
				w.elements.add(new Entry(parts[0]));
				w.elements.add(new Entry(parts[1]));
				w.elements.add(new Entry(parts[0], parts[1]));
			} else {
				w.elements.add(new Entry(t.trim()));
			}
		}
		return w;
	}

	private static String[] getParts(String t) {
		if (!t.contains(">")) {
			throw new RuntimeException("no transition provided: " + t);
		}
		String[] parts = t.split(">");
		if (parts.length > 2) {
			throw new RuntimeException("too many transitions, only 2 expected: " + t);
		}
		parts[0] = parts[0].trim();
		parts[1] = parts[1].trim();
		return parts;
	}
}