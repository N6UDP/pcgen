/*
 * PCGParseException.java
 * Copyright 2002 (C) Thomas Behr <ravenlock@gmx.de>
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
 * Created on March 15, 2002, 9:30 PM
 */
package pcgen.io;

import pcgen.core.Constants;

/**
 * <code>ParseException</code><br>
 *
 *
 * @author ???
 * @version $Revision$
 */
final class PCGParseException extends Exception
{
	private String errorLine;
	private String errorMessage;
	private String errorMethod;

	/**
	 * Constructor
	 *
	 * @deprecated Unused
	 */
	PCGParseException()
	{
		this("", "", "");
	}

	/**
	 * Constructor
	 * @param errorMethod
	 * @param errorLine
	 * @param errorMessage
	 */
	PCGParseException(String errorMethod, String errorLine, String errorMessage)
	{
		super("Method: " + errorMethod + Constants.s_LINE_SEP + "Line: " + errorLine + Constants.s_LINE_SEP
		    + "Message: " + errorMessage);

		this.errorMethod = errorMethod;
		this.errorLine = errorLine;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return error line
	 */
	public String getLine()
	{
		return errorLine;
	}

	/**
	 * @return error message
	 */
	public String getMessage()
	{
		return errorMessage;
	}

	/**
	 * @return error method
	 */
	public String getMethod()
	{
		return errorMethod;
	}
}
