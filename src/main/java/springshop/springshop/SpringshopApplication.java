package springshop.springshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import springshop.springshop.constant.Role;
import springshop.springshop.dto.MemberFormDto;
import springshop.springshop.entity.Member;
import springshop.springshop.service.MemberService;

@SpringBootApplication
public class SpringshopApplication {

	private MemberService memberService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public SpringshopApplication(MemberService memberService, PasswordEncoder passwordEncoder) {
		this.memberService = memberService;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringshopApplication.class, args);
	}

//	// 스프링 부트 시작 시 어드민 계정 생성
//	@EventListener(ApplicationReadyEvent.class)
//	public void init() {
//		MemberFormDto memberFormDto = new MemberFormDto();
//		memberFormDto.setName("admin");
//		memberFormDto.setPassword("admin");
//		memberFormDto.setEmail("admin@gmail.com");
//		memberFormDto.setAddress("어드민 주소");
//
//		memberService.saveMember(Member.createMember(memberFormDto, passwordEncoder));
//	}

}
