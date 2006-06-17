/*
 * FollowerTypeToken.java
 * Copyright 2006 (C) James Dempsey <jdempsey@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on Jun 17, 2006
 *
 * $Id: InfoKnownSpells.java 1030 2006-05-26 08:25:10Z jdempsey $
 *
 */
package plugin.exporttokens;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.core.PlayerCharacter;
import pcgen.core.character.Follower;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;
import pcgen.util.Logging;

/**
 * Deal with FOLLOWERTYPE Token
 * 
 *
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2006-05-26 18:25:10 +1000 (Fri, 26 May 2006) $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 1030 $
 */
public class FollowerTypeToken extends Token
{
	/** Token Name */
	public static final String TOKENNAME = "FOLLOWERTYPE";

	/**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
	public String getTokenName()
	{
		return TOKENNAME;
	}

	/**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
	public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh)
	{
		// Handle FOLLOWERTYPE.<type>x.subtag stuff
		// New token syntax FOLLOWERTYPE.<type>.x instead of FOLLOWERTYPE.<type>x
		StringTokenizer aTok = new StringTokenizer(tokenSource, ".");
		aTok.nextToken(); // FOLLOWERTYPE

		String typeString = aTok.nextToken();
		String restString = "";
		int followerIndex = -1;

		if (aTok.hasMoreTokens())
		{
			restString = aTok.nextToken();

			// When removing old token syntax, remove the catch code
			try
			{
				followerIndex = Integer.parseInt(restString);
				restString = "";
			}
			catch (NumberFormatException exc)
			{
				// Error, not debug.  We want users to report
				// use of the deprecated syntax so we can fix
				// them as they are found.
				Logging.errorPrint("Old syntax FOLLOWERTYPEx will be replaced for FOLLOWERTYPE.x");

				int numCharToRemove = 0;

				for (int i = typeString.length() - 1; i > 0; i--)
				{
					if ((typeString.charAt(i) >= '0') && (typeString.charAt(i) <= '9'))
					{
						followerIndex = Integer.parseInt(typeString.substring(i));
						numCharToRemove++;
					}
					else
					{
						i = 0;
					}
				}

				if (numCharToRemove > 0)
				{
					typeString = typeString.substring(0, typeString.length() - numCharToRemove);
				}
			}

			while (aTok.hasMoreTokens())
			{
				restString = restString + "." + aTok.nextToken();
			}

			if (restString.indexOf(".") == 0)
			{
				restString = restString.substring(1);
			}
		}

		String result = "";
		List aList = getFollowersOfType(pc, typeString);
		if (followerIndex < aList.size())
		{
			if (aList.get(followerIndex) instanceof Follower)
			{
				final Follower aF = (Follower) aList.get(followerIndex);
				result = FollowerToken.getFollowerOutput(pc, eh, restString, aF);
			}
		}

		return result;
	}

	/**
	 * Retrieve a list of followers of the desired type.
	 * 
	 * @param pc The targte character 
	 * @param typeString The follower type being looked for
	 * @return The list of qualifying followers.
	 */
	private List getFollowersOfType(PlayerCharacter pc, String typeString)
	{
		List aList = new ArrayList();
		final List followers = pc.getFollowerList();
		for (int i = followers.size() - 1; i >= 0; --i)
		{
			final Follower fol = (Follower) followers.get(i);

			if (fol.getType().equalsIgnoreCase(typeString))
			{
				aList.add(fol);
			}
		}
		return aList;
	}
}
