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

import static data.Workflow.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.Test;

public class WorkflowTest {

	@Test
	public void defaults() {
		Workflow sut = new Workflow("x");
		assertEquals("x", sut.id);
		assertEquals(new HashSet<>(), sut.steps);
		assertEquals(LinkedHashSet.class, sut.steps.getClass());
	}

	@Test
	public void equalityDefault() {
		Workflow a = new Workflow("a");
		Workflow b = new Workflow("a");
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityNonDefault() {
		Workflow a = new Workflow("x");
		a.steps.add(new Entry("a"));
		Workflow b = new Workflow("x");
		b.steps.add(new Entry("a"));
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityDiffId() {
		Workflow a = new Workflow("x");
		a.steps.add(new Entry("a"));
		Workflow b = new Workflow("y");
		b.steps.add(new Entry("a"));
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityDiffSteps() {
		Workflow a = new Workflow("x");
		a.steps.add(new Entry("a"));
		Workflow b = new Workflow("x");
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void consistencyDefault() {
		new Workflow("x").assertConsistency();
	}

	@Test
	public void consistencyLabel() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		sut.assertConsistency();
	}

	@Test
	public void consistencyLabel2() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		sut.steps.add(new Entry("b"));
		sut.assertConsistency();
	}

	@Test
	public void consistencyLabelsWithConnection() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		sut.steps.add(new Entry("b"));
		sut.steps.add(new Entry("a", "b"));
		sut.assertConsistency();
	}

	@Test(expected = RuntimeException.class)
	public void consistencyMissingFirstLabel() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("b"));
		sut.steps.add(new Entry("a", "b"));
		sut.assertConsistency();
	}

	@Test(expected = RuntimeException.class)
	public void consistencyMissingSecondLabel() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		sut.steps.add(new Entry("a", "b"));
		sut.assertConsistency();
	}

	@Test
	public void of0() {
		assertEquals(new Workflow("x"), of("x"));
	}

	@Test
	public void of1() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		assertEquals(sut, of("x", " a "));
	}

	@Test
	public void of2() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		sut.steps.add(new Entry("b"));
		assertEquals(sut, of("x", " a ", " b "));
	}

	@Test
	public void of3() {
		Workflow sut = new Workflow("x");
		sut.steps.add(new Entry("a"));
		sut.steps.add(new Entry("b"));
		sut.steps.add(new Entry("a", "b"));
		assertEquals(sut, of("x", " a ", " b ", " a > b "));
	}

	@Test
	public void toStringTest() {
		Workflow sut = of("x", "a", "b", "a>b");
		String expected = "Workflow(x) {\n" + "\t\"a\";\n" + "\t\"b\";\n" + "\t\"a\" -> \"b\";\n" + "}";
		assertEquals(expected, sut.toString());
	}
}