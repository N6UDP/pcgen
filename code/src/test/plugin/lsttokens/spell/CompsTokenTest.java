package plugin.lsttokens.spell;

import org.junit.Test;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.spell.Spell;
import pcgen.persistence.lst.CDOMToken;
import pcgen.persistence.lst.LstObjectFileLoader;
import pcgen.persistence.lst.SpellLoader;
import plugin.lsttokens.AbstractStringTokenTestCase;

public class CompsTokenTest extends AbstractStringTokenTestCase<Spell>
{

	static CompsToken token = new CompsToken();
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
		return StringKey.COMPONENTS;
	}

	@Test
	public void dummyTest()
	{
		//Just to get Eclipse to recognize this as a JUnit 4.0 Test Case
	}
}
