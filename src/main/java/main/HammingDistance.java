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

import static java.lang.Math.max;

import data.Entry;
import data.Workflow;

public class HammingDistance implements IDistance {
	@Override
	public double distance(Workflow a, Workflow b) {
		int max = max(a.elements.size(), b.elements.size());
		
		int inCommon = 0;

		for (Entry e : a.elements) {
			if (b.elements.contains(e)) {
				inCommon += 1;
			}
		}

		if (max == 0) {
			return 1;
		} else {
			return inCommon / (double) max;
		}
	}
}