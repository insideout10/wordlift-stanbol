package io.insideout.wordlift.org.apache.stanbol.enhancer.engines.freeling.exceptions;

public class TimeOutWaitingForAnalyzerException extends Exception {

	public TimeOutWaitingForAnalyzerException(final String language,
			final int timeOutSeconds) {
		super(
				String.format(
						"A timeout occurred while waiting for an analyzer [ seconds :: %d ][ language :: %s ].",
						timeOutSeconds, language));
	}

	private static final long serialVersionUID = 5493072017819568254L;

}
