package saessak.log.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JwtTokenTest {

    @Autowired
    TokenProvider tokenProvider;

//    @Test
//    public void token() throws Exception {
//        //given
//        String abcToken = jwtTokenProvider.createToken("abc");
//        String qwerToken = jwtTokenProvider.createToken("qwer");
//        System.out.println("abc = " + abcToken);
//        System.out.println("hobin = " + qwerToken);
//
//        //when
//        String abcPayload = null;
//        if (jwtTokenProvider.validateAbleToken(abcToken)) {
//            abcPayload = jwtTokenProvider.getPayload(abcToken);
//            System.out.println("payload = " + abcPayload);
//        }
//
//        //then
//        assertThat(abcPayload).isEqualTo("abc");
//        assertThat(abcPayload).isNotEqualTo("qwer");
//    }

}
