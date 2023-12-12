package springshop.springshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springshop.springshop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
}
