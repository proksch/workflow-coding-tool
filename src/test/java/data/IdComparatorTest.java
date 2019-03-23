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

import org.junit.Test;

public class IdComparatorTest {

	@Test
	public void variousCases() {
		assertEq("S1P1", "S1P1");
		assertLt("S1P1", "S1P2");
		assertLt("S2P2", "S3P2");
		assertGt("S2P2", "S1P2");
	}

	private void assertEq(String a, String b) {
		int res = new IdComparator().compare(a, b);
		if (res != 0) {
			throw new RuntimeException(String.format("Expected equality, comp(%s, %s) was %d though", a, b, res));
		}
	}

	private void assertLt(String a, String b) {
		int res = new IdComparator().compare(a, b);
		if (res >= 0) {
			throw new RuntimeException(String.format("Expected a<b, but comp(a:%s, b:%s) was %d", a, b, res));
		}
	}

	private void assertGt(String a, String b) {
		int res = new IdComparator().compare(a, b);
		if (res <= 0) {
			throw new RuntimeException(String.format("Expected a>b, but comp(a:%s, b:%s) was %d", a, b, res));
		}
	}
}