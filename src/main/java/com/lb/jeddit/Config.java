package com.lb.jeddit;

public class Config {

	public static final int EXPANDED_CARD = 0;
	public static final int CLASSIC_CARD = 1;

	private static int cardType = EXPANDED_CARD;

	public static int getCardType() {
		return cardType;
	}

	public static void setCardType(int cardType) {
		Config.cardType = cardType;
	}

}
