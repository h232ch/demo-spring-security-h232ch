package com.h232ch.demospringsecurityh232ch.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void index_annoymous() throws Exception{
        mockMvc.perform(get("/").with(anonymous())) // anonymous가 / 페이지를 호출 요청, 미로그인 사용자로 테스트
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_user() throws Exception{
        mockMvc.perform(get("/admin").with(user("sh").roles("ADMIN"))) // user(sh, USER)가 로그인된 상태로 / 페이지 호출 요청
                .andDo(print()) // 위 요청은 Forbidden 에러가 발생함 403 (Cause User is not allowed)
                .andExpect(status().isOk());
    }

    @Test
    public void index_admin() throws Exception{
        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN"))) // user(sh, USER)가 로그인된 상태로 / 페이지 호출 요청
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser // anonymous가 / 페이지를 호출 요청, 미로그인 사용자로 테스트 2번째 방법
    public void index_annoymous2() throws Exception{
        mockMvc.perform(get("/")) 
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="sh", roles = "USER")
    public void index_user2() throws Exception{
        mockMvc.perform(get("/")) // user(sh, USER)가 로그인된 상태로 / 페이지 호출 요청 2번째 방법
                .andDo(print()) // 위 요청은 Forbidden 에러가 발생함 403 (Cause User is not allowed)
                .andExpect(status().isOk());
    }

    @Test
    @WithUser // 해당 어노테이션 인터페이스 내에 @WithMockUser(username="sh", roles = "USER") 정보가 삽입되어 있음
    public void index_user3() throws Exception{
        mockMvc.perform(get("/")) // user(sh, USER)가 로그인된 상태로 / 페이지 호출 요청 3번째 방법 // WithUser 어노테이션 생성후 불러오기
                .andDo(print()) // 위 요청은 Forbidden 에러가 발생함 403 (Cause User is not allowed)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="sh", roles = "ADMIN")
    public void index_admin2() throws Exception{
        mockMvc.perform(get("/admin")) // user(sh, USER)가 로그인된 상태로 / 페이지 호출 요청 2번째 방법
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Autowired
    AccountService accountService;

    private Account createUser() {
        Account account = new Account();
        account.setUsername("sh");
        account.setPassword("123");
        account.setRole("USER");
        return accountService.createNew(account);
    }

    @Test
    @Transactional // 각각의 테스트가 종료되면 롤백이 되도록 설정함 (Username은 유니크하여 중복 생성불가능함 -> 아래 테스트에서 생성된 username은 테스트 종료시 삭제되어야 다음 테스트 가능
    public void login_succeed() throws Exception{

        Account user = createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("123")) // 패스워드는 직접 입력해줘야함 (로그인시에는 평문값으로)
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    @Transactional // 각각의 테스트가 종료되면 롤백이 되도록 설정함 (Username은 유니크하여 중복 생성불가능함 -> 아래 테스트에서 생성된 username은 테스트 종료시 삭제되어야 다음 테스트 가능
    public void login_unauthentificated() throws Exception{

        Account user = createUser();
        mockMvc.perform(formLogin().user(user.getUsername()).password("12")) // 패스워드는 직접 입력해줘야함 (로그인시에는 평문값으로)
                .andDo(print())
                .andExpect(unauthenticated());
    }
}