// Generated from PseudoDot.g4 by ANTLR 4.7.2

   package parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PseudoDotParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, LABEL=7, ID=8, PART_ID=9, 
		SCEN_ID=10, WS=11;
	public static final int
		RULE_graphs = 0, RULE_digraph = 1, RULE_edge = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"graphs", "digraph", "edge"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'strict'", "'digraph'", "'{'", "'}'", "'->'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, "LABEL", "ID", "PART_ID", "SCEN_ID", 
			"WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PseudoDot.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PseudoDotParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class GraphsContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PseudoDotParser.EOF, 0); }
		public List<DigraphContext> digraph() {
			return getRuleContexts(DigraphContext.class);
		}
		public DigraphContext digraph(int i) {
			return getRuleContext(DigraphContext.class,i);
		}
		public GraphsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graphs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PseudoDotListener ) ((PseudoDotListener)listener).enterGraphs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PseudoDotListener ) ((PseudoDotListener)listener).exitGraphs(this);
		}
	}

	public final GraphsContext graphs() throws RecognitionException {
		GraphsContext _localctx = new GraphsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_graphs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(6);
				digraph();
				}
				}
				setState(9); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__0 );
			setState(11);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DigraphContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PseudoDotParser.ID, 0); }
		public List<TerminalNode> WS() { return getTokens(PseudoDotParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(PseudoDotParser.WS, i);
		}
		public List<EdgeContext> edge() {
			return getRuleContexts(EdgeContext.class);
		}
		public EdgeContext edge(int i) {
			return getRuleContext(EdgeContext.class,i);
		}
		public DigraphContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_digraph; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PseudoDotListener ) ((PseudoDotListener)listener).enterDigraph(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PseudoDotListener ) ((PseudoDotListener)listener).exitDigraph(this);
		}
	}

	public final DigraphContext digraph() throws RecognitionException {
		DigraphContext _localctx = new DigraphContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_digraph);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			match(T__0);
			setState(15); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(14);
				match(WS);
				}
				}
				setState(17); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(19);
			match(T__1);
			setState(21); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(20);
				match(WS);
				}
				}
				setState(23); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(25);
			match(ID);
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(26);
				match(WS);
				}
				}
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(32);
			match(T__2);
			setState(34); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(33);
					edge();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(36); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(38);
				match(WS);
				}
				}
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(44);
			match(T__3);
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(45);
				match(WS);
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EdgeContext extends ParserRuleContext {
		public List<TerminalNode> LABEL() { return getTokens(PseudoDotParser.LABEL); }
		public TerminalNode LABEL(int i) {
			return getToken(PseudoDotParser.LABEL, i);
		}
		public List<TerminalNode> WS() { return getTokens(PseudoDotParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(PseudoDotParser.WS, i);
		}
		public EdgeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edge; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PseudoDotListener ) ((PseudoDotListener)listener).enterEdge(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PseudoDotListener ) ((PseudoDotListener)listener).exitEdge(this);
		}
	}

	public final EdgeContext edge() throws RecognitionException {
		EdgeContext _localctx = new EdgeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_edge);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(51);
				match(WS);
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(57);
			match(LABEL);
			setState(74);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(61);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==WS) {
						{
						{
						setState(58);
						match(WS);
						}
						}
						setState(63);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(64);
					match(T__4);
					setState(68);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==WS) {
						{
						{
						setState(65);
						match(WS);
						}
						}
						setState(70);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(71);
					match(LABEL);
					}
					} 
				}
				setState(76);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(80);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(77);
				match(WS);
				}
				}
				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(83);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\rX\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\6\2\n\n\2\r\2\16\2\13\3\2\3\2\3\3\3\3\6\3\22\n\3\r\3\16"+
		"\3\23\3\3\3\3\6\3\30\n\3\r\3\16\3\31\3\3\3\3\7\3\36\n\3\f\3\16\3!\13\3"+
		"\3\3\3\3\6\3%\n\3\r\3\16\3&\3\3\7\3*\n\3\f\3\16\3-\13\3\3\3\3\3\7\3\61"+
		"\n\3\f\3\16\3\64\13\3\3\4\7\4\67\n\4\f\4\16\4:\13\4\3\4\3\4\7\4>\n\4\f"+
		"\4\16\4A\13\4\3\4\3\4\7\4E\n\4\f\4\16\4H\13\4\3\4\7\4K\n\4\f\4\16\4N\13"+
		"\4\3\4\7\4Q\n\4\f\4\16\4T\13\4\3\4\3\4\3\4\2\2\5\2\4\6\2\2\2`\2\t\3\2"+
		"\2\2\4\17\3\2\2\2\68\3\2\2\2\b\n\5\4\3\2\t\b\3\2\2\2\n\13\3\2\2\2\13\t"+
		"\3\2\2\2\13\f\3\2\2\2\f\r\3\2\2\2\r\16\7\2\2\3\16\3\3\2\2\2\17\21\7\3"+
		"\2\2\20\22\7\r\2\2\21\20\3\2\2\2\22\23\3\2\2\2\23\21\3\2\2\2\23\24\3\2"+
		"\2\2\24\25\3\2\2\2\25\27\7\4\2\2\26\30\7\r\2\2\27\26\3\2\2\2\30\31\3\2"+
		"\2\2\31\27\3\2\2\2\31\32\3\2\2\2\32\33\3\2\2\2\33\37\7\n\2\2\34\36\7\r"+
		"\2\2\35\34\3\2\2\2\36!\3\2\2\2\37\35\3\2\2\2\37 \3\2\2\2 \"\3\2\2\2!\37"+
		"\3\2\2\2\"$\7\5\2\2#%\5\6\4\2$#\3\2\2\2%&\3\2\2\2&$\3\2\2\2&\'\3\2\2\2"+
		"\'+\3\2\2\2(*\7\r\2\2)(\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,.\3\2\2"+
		"\2-+\3\2\2\2.\62\7\6\2\2/\61\7\r\2\2\60/\3\2\2\2\61\64\3\2\2\2\62\60\3"+
		"\2\2\2\62\63\3\2\2\2\63\5\3\2\2\2\64\62\3\2\2\2\65\67\7\r\2\2\66\65\3"+
		"\2\2\2\67:\3\2\2\28\66\3\2\2\289\3\2\2\29;\3\2\2\2:8\3\2\2\2;L\7\t\2\2"+
		"<>\7\r\2\2=<\3\2\2\2>A\3\2\2\2?=\3\2\2\2?@\3\2\2\2@B\3\2\2\2A?\3\2\2\2"+
		"BF\7\7\2\2CE\7\r\2\2DC\3\2\2\2EH\3\2\2\2FD\3\2\2\2FG\3\2\2\2GI\3\2\2\2"+
		"HF\3\2\2\2IK\7\t\2\2J?\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2MR\3\2\2\2"+
		"NL\3\2\2\2OQ\7\r\2\2PO\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2SU\3\2\2\2"+
		"TR\3\2\2\2UV\7\b\2\2V\7\3\2\2\2\16\13\23\31\37&+\628?FLR";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}