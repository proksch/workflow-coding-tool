// Generated from PseudoDot.g4 by ANTLR 4.7.2

   package parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PseudoDotParser}.
 */
public interface PseudoDotListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PseudoDotParser#graphs}.
	 * @param ctx the parse tree
	 */
	void enterGraphs(PseudoDotParser.GraphsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PseudoDotParser#graphs}.
	 * @param ctx the parse tree
	 */
	void exitGraphs(PseudoDotParser.GraphsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PseudoDotParser#digraph}.
	 * @param ctx the parse tree
	 */
	void enterDigraph(PseudoDotParser.DigraphContext ctx);
	/**
	 * Exit a parse tree produced by {@link PseudoDotParser#digraph}.
	 * @param ctx the parse tree
	 */
	void exitDigraph(PseudoDotParser.DigraphContext ctx);
	/**
	 * Enter a parse tree produced by {@link PseudoDotParser#edge}.
	 * @param ctx the parse tree
	 */
	void enterEdge(PseudoDotParser.EdgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PseudoDotParser#edge}.
	 * @param ctx the parse tree
	 */
	void exitEdge(PseudoDotParser.EdgeContext ctx);
}