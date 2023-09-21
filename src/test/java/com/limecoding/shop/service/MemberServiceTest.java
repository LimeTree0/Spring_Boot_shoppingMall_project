package com.limecoding.shop.service;

import com.limecoding.shop.dto.MemberFormDto;
import com.limecoding.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member createMember() {

        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        // given
        Member member = createMember();

        //when
        Member saveMember = memberService.saveMember(member);

        //then
        assertThat(saveMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(saveMember.getName()).isEqualTo(member.getName());
        assertThat(saveMember.getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("중복된 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        // given
        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        assertThat("이미 가입된 회원입니다.").isEqualTo(exception.getMessage());
    }
}