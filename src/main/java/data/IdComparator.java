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

import java.util.Comparator;

public class IdComparator implements Comparator<String> {

	@Override
	public int compare(String a, String b) {
		int[] as = parseId(a);
		int[] bs = parseId(b);

		if (as[1] != bs[1]) {
			return as[1] - bs[1];
		} else {
			return as[0] - bs[0];
		}
	}

	private int[] parseId(String id) {
		if (!id.startsWith("S") || !id.contains("P")) {
			throw new RuntimeException("Cannot parse " + id);
		}

		id = id.substring(1);
		int s = Integer.parseInt(id.substring(0, id.indexOf('P')));
		int p = Integer.parseInt(id.substring(id.indexOf('P') + 1));
		return new int[] { s, p };
	}
}