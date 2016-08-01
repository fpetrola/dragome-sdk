/*
 * Copyright (c) 2011-2014 Fernando Petrola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java.lang;

import com.dragome.commons.javascript.ScriptHelper;

/**
 * The Character class wraps a value of the primitive type char in an object.
 *
 *
 */
public final class Character
{
	public static final int MIN_RADIX = 2;
	public static final int MAX_RADIX = 36;

	public static final char MIN_HIGH_SURROGATE= '\uD800';

	public static final Class<Character> TYPE= Class.getType("char");

	private char value;

	/**
	 * Constructs a newly allocated Character object that represents the specified char value.
	 */
	public Character(char c)
	{
		value= c;
	}

	/**
	 * Compares this object to the specified object.
	 */
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Character))
			return false;
		return ((Character) obj).value == value;
	}

	/**
	 * Determines if the specified character is a digit.
	 * <br/>Warning: This method will only detect ISO-LATIN-1 digits ('0' through '9').
	 */
	public static boolean isDigit(char ch)
	{
		return String.valueOf(ch).matches("[0-9]");
	}

	public static int digit(char c, int radix) {
		if (radix < MIN_RADIX || radix > MAX_RADIX) {
			return -1;
		}

		if (c >= '0' && c < '0' + Math.min(radix, 10)) {
			return c - '0';
		}

		// The offset by 10 is to re-base the alpha values
		if (c >= 'a' && c < (radix + 'a' - 10)) {
			return c - 'a' + 10;
		}

		if (c >= 'A' && c < (radix + 'A' - 10)) {
			return c - 'A' + 10;
		}

		return -1;
	}

	/**
	 * Determines if the specified character is a letter.
	 * <br/>Warning: This method will only detect ISO-LATIN-1 letters ('a' through 'Z').
	 */
	public static boolean isLetter(char ch)
	{
		return String.valueOf(ch).matches("[a-zA-Z]");
	}

	/**
	 * Returns an Long object holding the specified value. Calls to this
	 * method may be generated by the autoboxing feature.
	 */
	public static Character valueOf(char value)
	{
		return new Character(value);
	}

	/**
	 * Returns the value of this Character object.
	 */
	public char charValue()
	{
		return value;
	}

	/**
	 * Returns a String object representing this Character's value.
	 */
	public String toString()
	{
		//		 Duplicate code to String#valueOf(char)
		ScriptHelper.put("c", value, this);
		return (String) ScriptHelper.eval("String.fromCharCode(c)", this);
	}
	public static char toUpperCase(char c)
	{
		return ("" + c).toUpperCase().charAt(0);
	}

	public static char toLowerCase(char c)
	{
		return ("" + c).toLowerCase().charAt(0);
	}

	public static boolean isISOControl(char ch)
	{
		return isISOControl((int) ch);
	}

	public static boolean isISOControl(int codePoint)
	{
		return (codePoint >= 0x0000 && codePoint <= 0x001F) || (codePoint >= 0x007F && codePoint <= 0x009F);
	}

	public static boolean isUpperCase(char c)
	{
		if ('A' <= c && c <= 'Z')
		{
			return true;
		}
		return false;
	}

	public static boolean isLowerCase(char c)
	{
		if ('a' <= c && c <= 'z')
		{
			return true;
		}
		return false;
	}

	public static boolean isSpaceChar(char c)
	{
		return isSpace(c);
	}

	public static boolean isWhitespace(char ch)
	{
		return isSpace(ch);
	}

	public static boolean isSpace(char c)
	{
		switch (c)
		{
			case ' ':
				return true;
			case '\n':
				return true;
			case '\t':
				return true;
			case '\f':
				return true;
			case '\r':
				return true;
			default:
				return false;
		}
	}

	public static boolean isLetterOrDigit(char c)
	{
		ScriptHelper.put("c", c, null);
		return ScriptHelper.evalBoolean("null != String.fromCharCode(c).match(/[A-Z\\d]/i)", null);
	};

	public static int toChars(int codePoint, char[] dst, int dstIndex)
	{
		return 1;
	}
}
