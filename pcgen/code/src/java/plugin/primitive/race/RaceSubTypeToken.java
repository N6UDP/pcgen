/*
 * Copyright 2010 (C) Thomas Parker <thpr@users.sourceforge.net>
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
 */
package plugin.primitive.race;

import java.util.HashSet;
import java.util.Set;

import pcgen.cdom.enumeration.GroupingState;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.RaceSubType;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.Race;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.PrimitiveToken;

public class RaceSubTypeToken implements PrimitiveToken<Race>
{

	private static final Class<Race> RACE_CLASS = Race.class;
	private RaceSubType racetype;

	public boolean initialize(LoadContext context, Class<Race> cl,
			String value, String args)
	{
		if (args != null)
		{
			return false;
		}
		racetype = RaceSubType.getConstant(value);
		return true;
	}

	public String getTokenName()
	{
		return "RACESUBTYPE";
	}

	public Class<Race> getReferenceClass()
	{
		return RACE_CLASS;
	}

	public String getLSTformat()
	{
		return getTokenName() + "=" + racetype.toString();
	}

	public boolean allow(PlayerCharacter pc, Race race)
	{
		return race.containsInList(ListKey.RACESUBTYPE, racetype);
	}

	public Set<Race> getSet(PlayerCharacter pc)
	{
		HashSet<Race> RaceSet = new HashSet<Race>();
		for (Race race : Globals.getContext().ref
				.getConstructedCDOMObjects(RACE_CLASS))
		{
			if (race.containsInList(ListKey.RACESUBTYPE, racetype))
			{
				RaceSet.add(race);
			}
		}
		return RaceSet;
	}

	public GroupingState getGroupingState()
	{
		return GroupingState.ANY;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj instanceof RaceSubTypeToken)
		{
			RaceSubTypeToken other = (RaceSubTypeToken) obj;
			return racetype.equals(other.racetype);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return racetype == null ? -11 : racetype.hashCode();
	}
}
