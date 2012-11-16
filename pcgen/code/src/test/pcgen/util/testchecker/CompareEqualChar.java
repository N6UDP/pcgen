/**
 * CompareEqualChar.java
 * Copyright 2005 (c) Andrew Wilson <nuance@sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */

package pcgen.util.testchecker;

import pcgen.util.TestChecker;

/**
 * Compare chars
 */
public class CompareEqualChar extends TestChecker
{
	private char ch;

	/**
	 * Constructor
	 * @param ch
	 */
	public CompareEqualChar(char ch)
	{
		this.ch = ch;
	}

    @Override
	public boolean check(Object obj)
	{
		return obj.equals(Character.valueOf(this.ch));
	}

    @Override
	public StringBuilder scribe(StringBuilder buf)
	{
		buf.append("a char ");
		buf.append(this.ch);
		return buf;
	}
}
