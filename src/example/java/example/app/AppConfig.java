package example.app;

import com.k2.MetaModel.annotations.MetaApplication;
import com.k2.MetaModel.annotations.MetaVersion;

import example.app.serviceA.ServiceAConfig;
import example.app.serviceB.ServiceBConfig;

@MetaApplication(
		alias="testApplication",
		title="Test Application",
		description="This is a dummy application for testing the generation of the metamodel",
		version=@MetaVersion(major=1, minor=2, point=3, build=1234),
		organisation="k2.com",
		website="http://www.k2.com",
		services= {ServiceAConfig.class, ServiceBConfig.class}
		)
public class AppConfig {

}
