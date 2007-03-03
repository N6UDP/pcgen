package plugin.lsttokens.spell;

import org.junit.Test;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.spell.Spell;
import pcgen.persistence.lst.CDOMToken;
import pcgen.persistence.lst.LstObjectFileLoader;
import pcgen.persistence.lst.SpellLoader;
import plugin.lsttokens.AbstractStringTokenTestCase;

public class DurationTokenTest extends AbstractStringTokenTestCase<Spell>
{

	static DurationToken token = new DurationToken();
	static SpellLoader loader = new SpellLoader();

	@Override
	public Class<Spell> getCDOMClass()
	{
		return Spell.class;
	}

	@Override
	public LstObjectFileLoader<Spell> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMToken<Spell> getToken()
	{
		return token;
	}

	@Override
	public StringKey getStringKey()
	{
		return StringKey.DURATION;
	}

	@Test
	public void dummyTest()
	{
		//Just to get Eclipse to recognize this as a JUnit 4.0 Test Case
	}
}
