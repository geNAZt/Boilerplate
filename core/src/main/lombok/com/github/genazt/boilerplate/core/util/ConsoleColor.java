/*
 * Copyright (c) 2015, geNAZt
 *
 * This code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.github.genazt.boilerplate.core.util;

import lombok.Getter;

/**
 * @author geNAZt
 * @version 1.0
 */
public enum ConsoleColor {
	/**
	 * Represents black.
	 */
	BLACK( '0', "black" ),
	/**
	 * Represents dark blue.
	 */
	DARK_BLUE( '1', "dark_blue" ),
	/**
	 * Represents dark green.
	 */
	DARK_GREEN( '2', "dark_green" ),
	/**
	 * Represents dark blue (aqua).
	 */
	DARK_AQUA( '3', "dark_aqua" ),
	/**
	 * Represents dark red.
	 */
	DARK_RED( '4', "dark_red" ),
	/**
	 * Represents dark purple.
	 */
	DARK_PURPLE( '5', "dark_purple" ),
	/**
	 * Represents gold.
	 */
	GOLD( '6', "gold" ),
	/**
	 * Represents gray.
	 */
	GRAY( '7', "gray" ),
	/**
	 * Represents dark gray.
	 */
	DARK_GRAY( '8', "dark_gray" ),
	/**
	 * Represents blue.
	 */
	BLUE( '9', "blue" ),
	/**
	 * Represents green.
	 */
	GREEN( 'a', "green" ),
	/**
	 * Represents aqua.
	 */
	AQUA( 'b', "aqua" ),
	/**
	 * Represents red.
	 */
	RED( 'c', "red" ),
	/**
	 * Represents light purple.
	 */
	LIGHT_PURPLE( 'd', "light_purple" ),
	/**
	 * Represents yellow.
	 */
	YELLOW( 'e', "yellow" ),
	/**
	 * Represents white.
	 */
	WHITE( 'f', "white" ),
	/**
	 * Represents magical characters that change around randomly.
	 */
	MAGIC( 'k', "obfuscated" ),
	/**
	 * Makes the text bold.
	 */
	BOLD( 'l', "bold" ),
	/**
	 * Makes a line appear through the text.
	 */
	STRIKETHROUGH( 'm', "strikethrough" ),
	/**
	 * Makes the text appear underlined.
	 */
	UNDERLINE( 'n', "underline" ),
	/**
	 * Makes the text italic.
	 */
	ITALIC( 'o', "italic" ),
	/**
	 * Resets all previous chat colors or formats.
	 */
	RESET( 'r', "reset" );

	private final char code;
	private final String toString;
	@Getter private final String name;

	ConsoleColor( char code, String name ) {
		this.code = code;
		this.name = name;
		this.toString = new String( new char[] { '§', code } );
	}

	@Override
	public String toString() {
		return toString;
	}
}
