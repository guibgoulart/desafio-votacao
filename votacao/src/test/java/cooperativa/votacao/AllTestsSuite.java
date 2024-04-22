package cooperativa.votacao;

import cooperativa.votacao.service.AssociadoServiceTest;
import cooperativa.votacao.service.PautaServiceTest;
import cooperativa.votacao.service.SessaoVotacaoServiceTest;
import cooperativa.votacao.service.VotoServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({
        AssociadoServiceTest.class,
        PautaServiceTest.class,
        SessaoVotacaoServiceTest.class,
        VotoServiceTest.class,
})
public class AllTestsSuite {

}