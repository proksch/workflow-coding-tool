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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.FileUtils;

import parser.PseudoDotLexer;
import parser.PseudoDotParser;
import parser.PseudoDotParser.DigraphContext;
import parser.PseudoDotParser.EdgeContext;
import parser.PseudoDotParser.GraphsContext;

public class run_me {
	public static void main(String[] args) throws IOException {

		List<String> labels = readLabels("data/labels.txt");
		List<String> usedLabels = new LinkedList<>();
		PseudoDotParser parser = setupParser("data/a/scenario1.dot");
		System.out.println("-------------------------");

		GraphsContext graphs = parser.graphs();

		for (DigraphContext d : graphs.digraph()) {
			System.out.printf("## %s ##\n", d.PART_ID().getText());

			for (EdgeContext e : d.edge()) {
				List<TerminalNode> curLabels = e.LABEL();

				if (curLabels.size() == 1) {
					String l = curLabels.iterator().next().getText();
					l = l.substring(1, l.length() - 1);

					System.out.printf("%s;\n", l);
				} else {
					String last = null;
					for (TerminalNode l : curLabels) {
						String label = l.getText();
						label = label.substring(1, label.length() - 1);
						usedLabels.add(label);

						if (!labels.contains(label)) {
							System.err.println("Unkown label: " + label);
						}

						if (last != null) {
							System.out.printf("%s -> %s;\n", last, label);
						}
						last = label;
					}
				}
			}
		}
		System.out.println("-------------------------");
		System.out.println("Unused labels:");
		for (String label : labels) {
			if (!usedLabels.contains(label)) {
				System.out.printf("- %s\n", label);
			}
		}

	}

	private static PseudoDotParser setupParser(String path) throws IOException {
		String content = FileUtils.readFileToString(new File(path), UTF_8);

		MyErrorListener el = new MyErrorListener(path);
		CharStream stream = new ANTLRInputStream(content + "\n");
		PseudoDotLexer lexer = new PseudoDotLexer(stream);
		lexer.removeErrorListeners();
		lexer.addErrorListener(el);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PseudoDotParser parser = new PseudoDotParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(el);
		return parser;
	}

	private static List<String> readLabels(String path) throws IOException {
		List<String> labels = new LinkedList<>();
		for (String line : FileUtils.readLines(new File(path), UTF_8)) {
			if (line.contains("#")) {
				line = line.substring(0, line.indexOf("#"));
			}
			line = line.trim();

			if (line.startsWith("-") || line.startsWith("*")) {
				line = line.substring(1).trim();
			}

			if (line.isEmpty()) {
				continue;
			}

			labels.add(line);
		}

		System.out.println("Valid labels:");
		for (String label : labels) {
			System.out.printf("- %s\n", label);
		}

		return labels;
	}

	private static class MyErrorListener extends BaseErrorListener {
		private String pathname;

		public MyErrorListener(String pathname) {
			this.pathname = pathname;
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			throw new RuntimeException(msg + String.format(" (%s:L%d:%d)", pathname, line, charPositionInLine));
		}

		@Override
		public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
				ATNConfigSet configs) {
			System.err.println("reportContextSensitivity");
			super.reportContextSensitivity(recognizer, dfa, startIndex, stopIndex, prediction, configs);
		}

		@Override
		public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
				BitSet ambigAlts, ATNConfigSet configs) {
			// System.err.println("reportAmbiguity");
			super.reportAmbiguity(recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs);
		}

		@Override
		public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
				BitSet conflictingAlts, ATNConfigSet configs) {
			// System.err.println("reportAttemptingFullContext");
			super.reportAttemptingFullContext(recognizer, dfa, startIndex, stopIndex, conflictingAlts, configs);
		}
	}
}