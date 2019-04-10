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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntryTest {

	@Test
	public void init1() {
		Entry sut = new Entry("a");
		assertEquals("a", sut.from);
	}

	@Test
	public void init2() {
		Entry sut = new Entry("a", "b");
		assertEquals("a", sut.from);
		assertEquals("b", sut.to);
	}

	@Test
	public void isEdge() {
		assertTrue(new Entry("a", "b").isEdge());
		assertFalse(new Entry("a").isEdge());
	}

	@Test
	public void isNode() {
		assertTrue(new Entry("a").isNode());
		assertFalse(new Entry("a", "b").isNode());
	}

	@Test
	public void getA() {
		Entry sut = new Entry("a", "b");
		assertEquals(new Entry("a"), sut.getFrom());
	}

	@Test
	public void getB() {
		Entry sut = new Entry("a", "b");
		assertEquals(new Entry("b"), sut.getTo());
	}

	@Test(expected = RuntimeException.class)
	public void noNull1() {
		new Entry(null);
	}

	@Test(expected = RuntimeException.class)
	public void noNull2a() {
		new Entry(null, "b");
	}

	@Test(expected = RuntimeException.class)
	public void noNull2b() {
		new Entry("a", null);
	}

	@Test
	public void equality1() {
		assertEquals(new Entry("a"), new Entry("a"));
		assertEquals(new Entry("a").hashCode(), new Entry("a").hashCode());
	}

	@Test
	public void diff1() {
		assertNotEquals(new Entry("a"), new Entry("b"));
		assertNotEquals(new Entry("a").hashCode(), new Entry("b").hashCode());
	}

	@Test
	public void equality2() {
		assertEquals(new Entry("a", "b"), new Entry("a", "b"));
		assertEquals(new Entry("a", "b").hashCode(), new Entry("a", "b").hashCode());
	}

	@Test
	public void diff2a() {
		assertNotEquals(new Entry("a", "b"), new Entry("a2", "b"));
		assertNotEquals(new Entry("a", "b").hashCode(), new Entry("a2", "b").hashCode());
	}

	@Test
	public void diff2b() {
		assertNotEquals(new Entry("a", "b"), new Entry("a", "b2"));
		assertNotEquals(new Entry("a", "b").hashCode(), new Entry("a", "b2").hashCode());
	}

	@Test
	public void toString1() {
		assertEquals("\"a\"", new Entry("a").toString());
	}

	@Test
	public void toString2() {
		assertEquals("\"a\" -> \"b\"", new Entry("a", "b").toString());
	}
}