/*
 *  Initiative - A role playing utility to track turns
 *  Copyright (C) 2002 Devon D Jones
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  The author of this program grants you the ability to use this code
 *  in conjunction with code that is covered under the Open Gaming License
 *
 *  AttackModel.java
 *
 *
 *  This file is Open Game Content, covered by the OGL.
 */
package plugin.initiative;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.lang.StringUtil;

/**
 * <p>Models an attack; that is a weapon or natural weapon with its
 * attendant attack bonuses, damage, etc.</p>
 * <p>This class is built around the values generated by the output
 * tokens for weapons.  It splits the to-hit values by slashes and
 * represents them as a vector, counting all values after a ';' as
 * off-hand attacks (as in +10/+5;+10).  It splits damage into
 * primary and off-hand by a slash, and the same with crit multiples
 * and ranges.</p>
 *
 * @author Ross M. Lodge
 */
public class AttackModel extends PObjectModel
{
	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_TOHIT = 1;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_RANGE = 2;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_TYPE = 3;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_DAMAGE = 4;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_CRITRANGE = 5;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_CRITMULT = 6;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_HAND = 7;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_SIZE = 8;

	/** Constant for decoding incoming weapon strings */
	private static final int SEGMENT_POSITION_SPROP = 9;

	/** Hand of weapon as string */
	private String hand = null;

	/** Range of weapon as string */
	private String range = null;

	/** Size of weapon as string. */
	private String size = null;

	/** Special properties of weapon as string. */
	private String specialProp = null;

	/** Type of weapon as string */
	private String type = null;

	/** List of crit multiples for weapon. */
	private List<String> critMultiple = null;

	/** List of critical ranges for weapon. */
	private List<String> critRange = null;

	/** List of damage strings for weapon. */
	private List<String> damage = null;

	/** List of to-hit bonuses. */
	private List<String> toHit = null;

	/** Index of first off-hand attack. */
	private int firstOffHandAttack = -1;

	/**
	 * <p>Constructs a new attack model based on a string.  The string should
	 * have the following tokens, in the following order, separated by
	 * backslashes:</p>
	 * <ol>
	 * <li>|WEAPON.%weap.NAME|</li>
	 * <li>|WEAPON.%weap.TOTALHIT|</li>
	 * <li>|WEAPON.%weap.RANGE|</li>
	 * <li>|WEAPON.%weap.TYPE|</li>
	 * <li>|WEAPON.%weap.DAMAGE|</li>
	 * <li>|WEAPON.%weap.CRIT|</li>
	 * <li>|WEAPON.%weap.MULT|</li>
	 * <li>|WEAPON.%weap.HAND|</li>
	 * <li>|WEAPON.%weap.SIZE|</li>
	 * <li>|WEAPON.%weap.SPROP|</li>
	 * </ol>
	 *
	 * @param objectString
	 *             The string to interpret as a weapon/attack.
	 */
	public AttackModel(String objectString)
	{
		super(objectString);
		setToHit(getStringValue(outputTokens, SEGMENT_POSITION_TOHIT));
		setRange(getStringValue(outputTokens, SEGMENT_POSITION_RANGE));
		setType(getStringValue(outputTokens, SEGMENT_POSITION_TYPE));
		setDamage(getStringValue(outputTokens, SEGMENT_POSITION_DAMAGE));
		setCritRange(getStringValue(outputTokens, SEGMENT_POSITION_CRITRANGE));
		setCritMultiple(getStringValue(outputTokens, SEGMENT_POSITION_CRITMULT));
		setHand(getStringValue(outputTokens, SEGMENT_POSITION_HAND));
		setSize(getStringValue(outputTokens, SEGMENT_POSITION_SIZE));
		setSpecialProp(getStringValue(outputTokens, SEGMENT_POSITION_SPROP));
	}

	/**
	 * <p>Returns an integer array of bonuses as for a full attack, with the first attack (highest
	 * bonus) at index 0.  If the toHit field is not set, returns an array with a single element
	 * with the bonuse of 0.</p>
	 *
	 * @return <code>int[]</code> containing the integer bonuses for a full attack action
	 */
	public int[] getBonusList()
	{
		int[] returnValue = null;

		if ((toHit != null) && (toHit.size() > 0))
		{
			returnValue = new int[toHit.size()];

			for (int i = 0; i < toHit.size(); i++)
			{
				returnValue[i] = getInt(toHit.get(i).substring(1));
			}
		}

		return returnValue;
	}

	/**
	 * <p>
	 * Sets the crit multiple. This method splits the string before and after a
	 * slash, taking the value after the slash to indicate an off-hand attack
	 * (Head 2) multiple on a double weapon.
	 * </p>
	 *
	 * @param string
	 *            The crit multiple string.
	 */
	public void setCritMultiple(String string)
	{
		if ((string != null) && (string.length() > 0))
		{
			StringTokenizer tok = new StringTokenizer(string, "/");

			if (critMultiple == null)
			{
				critMultiple = new ArrayList<String>(tok.countTokens());
			}
			else
			{
				critMultiple.clear();
			}

			while (tok.hasMoreTokens())
			{
				critMultiple.add(tok.nextToken());
			}
		}
		else
		{
			if (critMultiple == null)
			{
				critMultiple = new ArrayList<String>(1);
			}
			else
			{
				critMultiple.clear();
			}

			critMultiple.add("2");
		}
	}

	/**
	 * <p>Assembles the crit multiple string and returns it</p>
	 *
	 * @return Crit multiple string
	 */
	public String getCritMultiple()
	{
		return StringUtil.join(critMultiple, "/");
	}

	/**
	 * <p>Gets the crit multiple at the specified attack bonus index, based
	 * on whether or not the index > or < the first off hand attack index.</p>
	 *
	 * @param index
	 * @return The crit multiple value.
	 */
	public String getCritMultiple(int index)
	{
		String returnValue = null;
		int lookupAt = 0;

		if ((firstOffHandAttack != -1) && (index >= firstOffHandAttack))
		{
			lookupAt = 1;
		}
		else
		{
			lookupAt = 0;
		}

		if ((critMultiple != null) && (critMultiple.size() > lookupAt))
		{
			returnValue = critMultiple.get(lookupAt);
		}
		else if ((critMultiple != null) && (critMultiple.size() > 0))
		{
			returnValue = critMultiple.get(0);
		}

		return returnValue;
	}

	/**
	 * <p>
	 * Sets the crit range before and after a slash, taking the values after
	 * the slash to indicate the bonuse for the off-hand (Head 2) of a double
	 * weapon.
	 * </p>
	 *
	 * @param string
	 *            The crit range string
	 */
	public void setCritRange(String string)
	{
		if ((string != null) && (string.length() > 0))
		{
			StringTokenizer tok = new StringTokenizer(string, "/");

			if (critRange == null)
			{
				critRange = new ArrayList<String>(tok.countTokens());
			}
			else
			{
				critRange.clear();
			}

			while (tok.hasMoreTokens())
			{
				critRange.add(tok.nextToken());
			}
		}
		else
		{
			if (critRange == null)
			{
				critRange = new ArrayList<String>(1);
			}
			else
			{
				critRange.clear();
			}

			critRange.add("20");
		}
	}

	/**
	 * <p>Gets the crit range value.</p>
	 *
	 * @return The crit range value.
	 */
	public String getCritRange()
	{
		return StringUtil.join(critRange, "/");
	}

	/**
	 * <p>Gets the crit range at the specified attack bonus index, based
	 * on whether or not the index > or < the first off hand attack index.</p>
	 *
	 * @param index The attack bonus index
	 * @return The requested crit range.
	 */
	public String getCritRange(int index)
	{
		String returnValue = null;
		int lookupAt = 0;

		if ((firstOffHandAttack != -1) && (index >= firstOffHandAttack))
		{
			lookupAt = 1;
		}
		else
		{
			lookupAt = 0;
		}

		if ((critRange != null) && (critRange.size() > lookupAt))
		{
			returnValue = critRange.get(lookupAt);
		}
		else if ((critRange != null) && (critRange.size() > 0))
		{
			returnValue = critRange.get(0);
		}

		return returnValue;
	}

	/**
	 * <p>Gets the minimum value of the crit range at the specified attack bonus
	 * index as an integer.  So 19-20 would produce 19.</p>
	 *
	 * @param index
	 *             The attack bonus index to retrieve values for.
	 * @return
	 *             The minimum value for the crit range as an integer.
	 */
	public int getCritRangeMin(int index)
	{
		int returnValue;
		String aRange =
				new StringTokenizer(getCritRange(index), "-").nextToken();
		returnValue = getInt(aRange);

		return returnValue;
	}

	/**
	 * <p>
	 * Sets the damage string. This method splits the damage string around a
	 * slash, interpreting values after the slash to be the damage for the
	 * off-hand (head 2) of a double weapon.
	 * </p>
	 *
	 * @param string
	 *            Damage string.
	 */
	public void setDamage(String string)
	{
		if ((string != null) && (string.length() > 0))
		{
			StringTokenizer tok = new StringTokenizer(string, "/");

			if (damage == null)
			{
				damage = new ArrayList<String>(tok.countTokens());
			}
			else
			{
				damage.clear();
			}

			while (tok.hasMoreTokens())
			{
				damage.add(tok.nextToken());
			}

			if (damage.size() > 1)
			{
				//If we've got a double weapon, pcgen is using AdB+C/+D, so
				String damageDice = damage.get(0);
				if (damageDice.lastIndexOf("+") > 0)
				{
					damageDice =
							damageDice
								.substring(0, damageDice.lastIndexOf("+"));
				}
				else if (damageDice.lastIndexOf("-") > 0)
				{
					damageDice =
							damageDice
								.substring(0, damageDice.lastIndexOf("-"));
				}
				for (int i = 1; i < damage.size(); i++)
				{
					String secondaryDamage = damage.get(i);
					if (secondaryDamage.startsWith("+")
						|| secondaryDamage.startsWith("-"))
					{
						damage.set(i, damageDice + secondaryDamage);
					}
				}
			}
		}
		else
		{
			if (damage == null)
			{
				damage = new ArrayList<String>(1);
			}
			else
			{
				damage.clear();
			}

			damage.add("1");
		}
	}

	/**
	 * <p>Gets the damage string for the attack.</p>
	 * @return The damage string
	 */
	public String getDamage()
	{
		return StringUtil.join(damage, "/");
	}

	/**
	 * <p>Gets the damage dice at the specified attack bonus index, based
	 * on whether or not the index > or < the first off hand attack index.</p>
	 *
	 * @param index The attack bonus index
	 * @return The requested damage string.
	 */
	public String getDamage(int index)
	{
		String returnValue = null;
		int lookupAt = 0;

		if ((firstOffHandAttack != -1) && (index >= firstOffHandAttack))
		{
			lookupAt = 1;
		}
		else
		{
			lookupAt = 0;
		}

		if ((damage != null) && (damage.size() > lookupAt))
		{
			returnValue = damage.get(lookupAt);
		}
		else if ((damage != null) && (damage.size() > 0))
		{
			returnValue = damage.get(0);
		}

		return returnValue;
	}

	/**
	 * <p>Sets the hand value.</p>
	 * @param string
	 */
	public void setHand(String string)
	{
		hand = string;
	}

	/**
	 * <p>Gets the hand of the weapon as a string</p>
	 * @return The hand of the weapon
	 */
	public String getHand()
	{
		return hand;
	}

	/**
	 * <p>Sets the range value</p>
	 * @param string
	 */
	public void setRange(String string)
	{
		if ((string == null) || (string.length() > 0))
		{
			range = string;
		}
		else
		{
			range = "0'";
		}
	}

	/**
	 * <p>Gets the range of the weapon as a string.</p>
	 * @return The range string.
	 */
	public String getRange()
	{
		return range;
	}

	/**
	 * <p>Gets the range of the weapon as an integer.</p>
	 * @return The range of the weapon as an integer.
	 */
	public int getRangeAsInt()
	{
		return getInt(range.replaceAll("\\D", ""));
	}

	/**
	 * <p>Sets the size value</p>
	 * @param string
	 */
	public void setSize(String string)
	{
		size = string;
	}

	/**
	 * <p>Gets the weapon size.</p>
	 * @return Size
	 */
	public String getSize()
	{
		return size;
	}

	/**
	 * <p>Sets the special property value.</p>
	 * @param string
	 */
	public void setSpecialProp(String string)
	{
		specialProp = string;
	}

	/**
	 * <p>Gets the special properties string.</p>
	 * @return Special properties.
	 */
	public String getSpecialProp()
	{
		return specialProp;
	}

	/**
	 * <p>
	 * Sets the two hit value. This method drives much of the functionality of
	 * the attack model. The string should be in the form <br>
	 * <code>
	 *    Bonus/Bonus/Bonus...;off hand bonus/off hand bonus...
	 * </code>
	 * <br>
	 * The method splits the string into primary and off-hand bonus values, and
	 * splits the separate strings into individual bonuses. The length of the
	 * resulting list of bonuses drives the indices used to determine bonuses
	 * for each attack, and also which attacks are primary and off-hand.
	 * </p>
	 *
	 * @param string
	 *             The attack bonuse string.
	 */
	public void setToHit(String string)
	{
		if (toHit == null)
		{
			toHit = new ArrayList<String>();
		}
		else
		{
			toHit.clear();
		}

		firstOffHandAttack = -1;

		StringTokenizer tok = new StringTokenizer(string, ";");
		int handCount = 0;
		int attackIndex = -1;

		while (tok.hasMoreTokens())
		{
			handCount++;

			StringTokenizer tok2 = new StringTokenizer(tok.nextToken(), "/");

			while (tok2.hasMoreTokens())
			{
				attackIndex++;

				if ((handCount == 2) && (firstOffHandAttack == -1))
				{
					firstOffHandAttack = attackIndex;
				}

				toHit.add(tok2.nextToken());
			}
		}
	}

	/**
	 * <p>Gets the to-hit string (like +10/+5;+8 . . .).</p>
	 * @return The to-hit string.
	 */
	public String getToHit()
	{
		StringBuilder sb = new StringBuilder();

		if ((toHit != null) && (toHit.size() > 0))
		{
			for (int i = 0; i < toHit.size(); i++)
			{
				if (sb.length() > 0)
				{
					if (i == firstOffHandAttack)
					{
						sb.append(";");
					}
					else
					{
						sb.append("/");
					}
				}

				sb.append(toHit.get(i));
			}
		}

		return sb.toString();
	}

	/**
	 * <p>Gets the to-hit string at the specified attack index.</p>
	 * 
	 * @param index
	 * @return The to-hit string.
	 */
	public String getToHit(int index)
	{
		String returnValue = null;

		if ((toHit != null) && (toHit.size() > index))
		{
			returnValue = toHit.get(index);
		}

		return returnValue;
	}

	/**
	 * <p>Sets teh weapon type.</p>
	 * @param string
	 */
	public void setType(String string)
	{
		type = string;
	}

	/**
	 * <p>Gets the weapon type.</p>
	 * @return The weapon type
	 */
	public String getType()
	{
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 *
	 * Gets a string representation of the object.
	 */
	@Override
	public String toString()
	{
		String returnValue;
		returnValue =
				getName() + " " + getToHit() + " " + getRange() + "/"
					+ getType() + " (" + getDamage() + " " + getCritRange()
					+ "/x" + getCritMultiple() + " " + getHand() + " "
					+ getSize()
					+ ("".equals(getSpecialProp()) ? "" : getSpecialProp())
					+ ")";

		return returnValue;
	}
}
