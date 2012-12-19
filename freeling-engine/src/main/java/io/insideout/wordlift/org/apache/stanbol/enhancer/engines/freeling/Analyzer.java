package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling;

import edu.upc.freeling.ChartParser;
import edu.upc.freeling.DepTxala;
import edu.upc.freeling.HmmTagger;
import edu.upc.freeling.Maco;
import edu.upc.freeling.Nec;
import edu.upc.freeling.Senses;
import edu.upc.freeling.Splitter;
import edu.upc.freeling.Tokenizer;
import edu.upc.freeling.UkbWrap;

public class Analyzer {

	private final Maco maco;
	private final Tokenizer tokenizer;
	private final Splitter splitter;
	private final HmmTagger hmmTagger;
	private final ChartParser chartParser;
	private final DepTxala depTxala;
	private final Nec nec;
	private final Senses senses;
	private final UkbWrap ukbWrap;
	private final boolean alwaysFlush;

	public Analyzer(final Maco maco, final Tokenizer tokenizer,
			final Splitter splitter, final HmmTagger hmmTagger,
			final ChartParser chartParser, final DepTxala depTxala,
			final Nec nec, final Senses senses, final UkbWrap ukbWrap,
			final boolean alwaysFlush) {

		this.maco = maco;
		this.tokenizer = tokenizer;
		this.splitter = splitter;
		this.hmmTagger = hmmTagger;
		this.chartParser = chartParser;
		this.depTxala = depTxala;
		this.nec = nec;
		this.senses = senses;
		this.ukbWrap = ukbWrap;
		this.alwaysFlush = alwaysFlush;
	}

	public Maco getMaco() {
		return maco;
	}

	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public HmmTagger getHmmTagger() {
		return hmmTagger;
	}

	public ChartParser getChartParser() {
		return chartParser;
	}

	public DepTxala getDepTxala() {
		return depTxala;
	}

	public Nec getNec() {
		return nec;
	}

	public Senses getSenses() {
		return senses;
	}

	public UkbWrap getUkbWrap() {
		return ukbWrap;
	}

	public boolean isAlwaysFlush() {
		return alwaysFlush;
	}

}
