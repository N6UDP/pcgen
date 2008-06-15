package plugin.lsttokens.template;

import java.util.Collection;
import java.util.StringTokenizer;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.reference.ReferenceUtilities;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.PCTemplate;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Class deals with FEAT Token
 */
public class FeatToken extends AbstractToken implements
		CDOMPrimaryToken<PCTemplate>
{
	public static final Class<Ability> ABILITY_CLASS = Ability.class;

	@Override
	public String getTokenName()
	{
		return "FEAT";
	}

	public boolean parse(LoadContext context, PCTemplate pct, String value)
	{
		return parseFeat(context, pct, value);
	}

	public boolean parseFeat(LoadContext context, CDOMObject obj, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}

		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);

		boolean first = true;

		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			if (Constants.LST_DOT_CLEAR.equals(token))
			{
				if (!first)
				{
					Logging.errorPrint("  Non-sensical " + getTokenName()
							+ ": .CLEAR was not the first list item: " + value);
					return false;
				}
				context.getObjectContext().removeList(obj, ListKey.FEAT);
			}
			else
			{
				CDOMReference<Ability> ability = TokenUtilities
						.getTypeOrPrimitive(context, ABILITY_CLASS,
								AbilityCategory.FEAT, token);
				if (ability == null)
				{
					return false;
				}
				context.obj.addToList(obj, ListKey.FEAT, ability);
			}
			first = false;
		}
		return true;
	}

	public String[] unparse(LoadContext context, PCTemplate pct)
	{
		Changes<CDOMReference<Ability>> changes = context.obj.getListChanges(
				pct, ListKey.FEAT);
		Collection<CDOMReference<Ability>> added = changes.getAdded();
		Collection<CDOMReference<Ability>> removedItems = changes.getRemoved();
		StringBuilder sb = new StringBuilder();
		if (changes.includesGlobalClear())
		{
			if (removedItems != null && !removedItems.isEmpty())
			{
				context.addWriteMessage("Non-sensical relationship in "
						+ getTokenName()
						+ ": global .CLEAR and local .CLEAR. performed");
				return null;
			}
			sb.append(Constants.LST_DOT_CLEAR);
		}
		else if (removedItems != null && !removedItems.isEmpty())
		{
			context.addWriteMessage(getTokenName() + " does not support "
					+ Constants.LST_DOT_CLEAR_DOT);
			return null;
		}
		if (added != null && !added.isEmpty())
		{
			if (sb.length() != 0)
			{
				sb.append(Constants.PIPE);
			}
			sb.append(ReferenceUtilities.joinLstFormat(added, Constants.PIPE));
		}
		if (sb.length() == 0)
		{
			return null;
		}
		return new String[] { sb.toString() };
	}

	public Class<PCTemplate> getTokenClass()
	{
		return PCTemplate.class;
	}
}
