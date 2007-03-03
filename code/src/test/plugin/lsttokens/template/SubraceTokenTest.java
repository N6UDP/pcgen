package plugin.lsttokens.template;

import org.junit.Test;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SubRace;
import pcgen.core.PCTemplate;
import pcgen.persistence.lst.CDOMToken;
import pcgen.persistence.lst.LstObjectFileLoader;
import pcgen.persistence.lst.PCTemplateLoader;
import plugin.lsttokens.AbstractTypeSafeTokenTestCase;

public class SubraceTokenTest extends AbstractTypeSafeTokenTestCase<PCTemplate>
{

	static SubraceToken token = new SubraceToken();
	static PCTemplateLoader loader = new PCTemplateLoader();

	@Override
	public Class<PCTemplate> getCDOMClass()
	{
		return PCTemplate.class;
	}

	@Override
	public LstObjectFileLoader<PCTemplate> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMToken<PCTemplate> getToken()
	{
		return token;
	}

	@Override
	public Object getConstant(String string)
	{
		return SubRace.getConstant(string);
	}

	@Override
	public ObjectKey<?> getObjectKey()
	{
		return ObjectKey.SUBRACE;
	}

	@Test
	public void dummyTest()
	{
		//Just to get Eclipse to recognize this as a JUnit 4.0 Test Case
	}
}
