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

import org.junit.Test;

import data.Workflow;

public class HammingDistanceTest {

	private HammingDistance sut = new HammingDistance();

	@Test
	public void empty() {
		assertSim(1, w(), w());
	}

	@Test
	public void diff1() {
		assertSim(0, w("a"), w());
	}

	@Test
	public void diff2() {
		assertSim(0, w("a"), w("b"));
	}

	@Test
	public void diff3() {
		assertSim(0, w("a", "b"), w());
	}

	@Test
	public void diff4() {
		assertSim(0.5, w("a", "b"), w("a"));
	}

	@Test
	public void diff5() {
		assertSim(0.5, w("a", "b"), w("a", "c"));
	}

	@Test
	public void diff6() {
		assertSim(0.666, w("a", "b", "a > b"), w("a", "b"));
	}

	@Test
	public void eq0() {
		assertSim(1, w(), w());
	}

	@Test
	public void eq1() {
		assertSim(1, w("a"), w("a"));
	}

	@Test
	public void eq2() {
		assertSim(1, w("a", "b"), w("a", "b"));
	}

	@Test
	public void eq3() {
		assertSim(1, w("a", "b", "c"), w("a", "b", "c"));
	}

	private static Workflow w(String... es) {
		return Workflow.of("x", es);
	}

	private void assertSim(double expected, Workflow a, Workflow b) {
		double actual = sut.distance(a, b);
		assertEquals(expected, actual, 0.001);

		actual = sut.distance(b, a);
		assertEquals(expected, actual, 0.001);
	}
}