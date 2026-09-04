import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.gov.rj.rio.iplanrio.aceitacao.prefrio.test.HomeTest;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.test.LoginTest;

@RunWith(Suite.class)
@SuiteClasses({
HomeTest.class,
LoginTest.class
 
})
public class SuiteDeTeste {

}
