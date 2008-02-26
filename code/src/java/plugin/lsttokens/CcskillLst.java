/*
 * Copyright 2006-2007 (C) Tom Parker <thpr@users.sourceforge.net>
 * Copyright 2005-2006 (C) Devon Jones
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
package plugin.lsttokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.CDOMGroupRef;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.LSTWriteable;
import pcgen.cdom.base.ReferenceUtilities;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.SkillCost;
import pcgen.cdom.inst.CDOMSkill;
import pcgen.cdom.inst.ClassSkillList;
import pcgen.core.PObject;
import pcgen.persistence.lst.GlobalLstToken;
import pcgen.rules.context.AssociatedChanges;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;
import pcgen.util.MapToList;

/**
 * @author djones4
 * 
 */
public class CcskillLst extends AbstractToken implements GlobalLstToken,
		CDOMPrimaryToken<CDOMObject>
{

	private static final Class<CDOMSkill> SKILL_CLASS = CDOMSkill.class;

	private static final Class<ClassSkillList> CLASSSKILLLIST_CLASS = ClassSkillList.class;

	@Override
	public String getTokenName()
	{
		return "CCSKILL";
	}

	public boolean parse(PObject obj, String value, int anInt)
	{
		final StringTokenizer tok = new StringTokenizer(value, "|");
		while (tok.hasMoreTokens())
		{
			obj.addCcSkill(tok.nextToken());
		}
		return true;
	}

	public boolean parse(LoadContext context, CDOMObject obj, String value)
	{
		/*
		 * TODO Question is whether the use of this in a PCClass should really
		 * alter the SkillList attached to the PCClass...
		 */
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}

		boolean foundAny = false;
		boolean foundOther = false;

		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);
		boolean first = true;

		while (tok.hasMoreTokens())
		{
			String tokText = tok.nextToken();
			if (Constants.LST_DOT_CLEAR.equals(tokText))
			{
				if (!first)
				{
					Logging.errorPrint("  Non-sensical " + getTokenName()
							+ ": .CLEAR was not the first list item");
					return false;
				}
				context.getGraphContext().removeAll(getTokenName(), obj);
			}
			else if (tokText.startsWith(Constants.LST_DOT_CLEAR_DOT))
			{
				CDOMReference<CDOMSkill> ref;
				String clearText = tokText.substring(7);
				if (Constants.LST_ALL.equals(clearText))
				{
					ref = context.ref.getCDOMAllReference(SKILL_CLASS);
				}
				else
				{
					ref = TokenUtilities.getTypeOrPrimitive(context,
							SKILL_CLASS, clearText);
				}
				if (ref == null)
				{
					Logging.errorPrint("  Error was encountered while parsing "
							+ getTokenName());
					return false;
				}
				context.getGraphContext().remove(getTokenName(), obj, ref);
			}
			else
			{
				/*
				 * Note this HAS to be done one-by-one, because the
				 * .clearChildNodeOfClass method above does NOT recognize the
				 * C/CC Skill object and therefore doesn't know how to search
				 * the sublists
				 */
				CDOMReference<CDOMSkill> ref;
				if (Constants.LST_ALL.equals(tokText))
				{
					foundAny = true;
					ref = context.ref.getCDOMAllReference(SKILL_CLASS);
				}
				else
				{
					foundOther = true;
					ref = TokenUtilities.getTypeOrPrimitive(context,
							SKILL_CLASS, tokText);
				}
				if (ref == null)
				{
					Logging.errorPrint("  Error was encountered while parsing "
							+ getTokenName());
					return false;
				}
				CDOMGroupRef<ClassSkillList> aref = context.ref
						.getCDOMAllReference(CLASSSKILLLIST_CLASS);
				AssociatedPrereqObject edge = context.getListContext()
						.addToMasterList(getTokenName(), obj, aref, ref);
				edge.setAssociation(AssociationKey.SKILL_COST,
						SkillCost.CROSS_CLASS);
			}
			first = false;
		}
		if (foundAny && foundOther)
		{
			Logging.errorPrint("Non-sensical " + getTokenName()
					+ ": Contains ANY and a specific reference: " + value);
			return false;
		}
		return true;
	}

	public String[] unparse(LoadContext context, CDOMObject obj)
	{
		CDOMGroupRef<ClassSkillList> listRef = context.ref
				.getCDOMAllReference(CLASSSKILLLIST_CLASS);
		AssociatedChanges<LSTWriteable> changes = context.getListContext()
				.getChangesInMasterList(getTokenName(), obj, listRef);
		if (changes == null)
		{
			// Legal if no CSKILL was present
			return null;
		}
		List<String> list = new ArrayList<String>();
		Collection<LSTWriteable> removedItems = changes.getRemoved();
		if (removedItems != null && !removedItems.isEmpty())
		{
			if (changes.includesGlobalClear())
			{
				context.addWriteMessage("Non-sensical relationship in "
						+ getTokenName()
						+ ": global .CLEAR and local .CLEAR. performed");
				return null;
			}
			list.add(Constants.LST_DOT_CLEAR_DOT
					+ ReferenceUtilities.joinLstFormat(removedItems,
							",|.CLEAR."));
		}
		if (changes.includesGlobalClear())
		{
			list.add(Constants.LST_DOT_CLEAR);
		}
		MapToList<LSTWriteable, AssociatedPrereqObject> map = changes
				.getAddedAssociations();
		if (map != null && !map.isEmpty())
		{
			Set<LSTWriteable> addedList = map.getKeySet();
			for (LSTWriteable added : addedList)
			{
				for (AssociatedPrereqObject assoc : map.getListFor(added))
				{
					if (!SkillCost.CROSS_CLASS.equals(assoc
							.getAssociation(AssociationKey.SKILL_COST)))
					{
						context
								.addWriteMessage("Skill Cost must be CROSS_CLASS for Token "
										+ getTokenName());
						return null;
					}
				}
			}
			list.add(ReferenceUtilities
					.joinLstFormat(addedList, Constants.PIPE));
		}
		if (list.isEmpty())
		{
			return null;
		}
		return list.toArray(new String[list.size()]);
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}
}
