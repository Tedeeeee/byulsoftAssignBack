package project.assign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.MemberDTO;
import project.assign.repository.MemberMapper;

@SpringBootTest
class AssignApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MemberMapper memberMapper;

	@Test
	@DisplayName("회원가입 테스트")
	public void memberSaveTest() {
		memberMapper.save(new MemberDTO("첫번째 회원", 12));

		// then

	}

}
