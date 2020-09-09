package ma.gcom.testspringjpa;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

@RewriteConfiguration
public class RewriteConfig extends HttpConfigurationProvider {
	@Override
	public int priority() {
		return 10;
	}

	@Override
	public Configuration getConfiguration(final ServletContext context) {
		return ConfigurationBuilder.begin().//
				addRule(Join.path("/jsf/user").to("/userList.xhtml")).//
				addRule(Join.path("/jsf/user/insert").to("/userForm.xhtml")).//
				addRule(Join.path("/jsf/user/update/{id}").to("/userForm.xhtml")).//
				addRule(Join.path("/jsf/user/{id}").to("/user.xhtml"));

	}
}