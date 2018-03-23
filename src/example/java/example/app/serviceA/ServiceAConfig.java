package example.app.serviceA;

import com.k2.MetaModel.annotations.MetaService;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaService(
		alias="serviceA",
		title="Service A",
		description="This is test service A",
		version=@MetaVersion(major=0, minor=1, point=2), 
		modelPackageNames = { "example.app.serviceA.model" }
		)
public class ServiceAConfig {

}
