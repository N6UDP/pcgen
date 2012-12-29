/**
 * pcgen.core.term.BasePCTermEvaluator.java
 * Copyright (c) 2008 Andrew Wilson <nuance@users.sourceforge.net>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created 07-Aug-2008 20:49:05
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.core.character.CharacterSpell;
import pcgen.core.display.CharacterDisplay;
import pcgen.core.spell.Spell;
import pcgen.util.Logging;

public abstract class BasePCDTermEvaluator extends BasePCTermEvaluator
{
	protected String originalText;

	final public String evaluate(PlayerCharacter pc) {
		return evaluate(pc.getDisplay());
	}

	protected String evaluate(CharacterDisplay display) {
		return Integer.toString(resolve(display).intValue());
	}

	final public String evaluate(PlayerCharacter pc,  final Spell aSpell) {
		return evaluate(pc.getDisplay(), aSpell);	
	}

	protected String evaluate(CharacterDisplay display, Spell aSpell) {
		return evaluate(display);
	}

	final public String evaluate(
			Equipment eq,
			boolean primary,
			PlayerCharacter pc) {
		return evaluate(pc);
	}
	
	final public Float resolve(PlayerCharacter pc)
	{
		return resolve(pc.getDisplay());
	}

	protected abstract Float resolve(CharacterDisplay display);

	public final Float resolve(PlayerCharacter pc, final CharacterSpell aSpell) {
		return convertToFloat(originalText, evaluate(pc, aSpell == null ? null : aSpell.getSpell()));
	}

	public final Float resolve(
			Equipment eq,
			boolean primary,
			PlayerCharacter pc) {
		return convertToFloat(originalText, evaluate(eq, primary, pc));
	}

	protected final Float convertToFloat(String element, String foo)
	{
		Float d = null;
		try
		{
			d = new Float(foo);
		}
		catch (NumberFormatException nfe)
		{
			// What we got back was not a number
		}

		Float retVal = null;
		if (d != null && !d.isNaN())
		{
			retVal = d;
			Logging.debugPrint(
					new StringBuilder("Export variable for: '")
							.append(element)
							.append("' = ")
							.append(d).toString());
		}

		return retVal;
	}

}