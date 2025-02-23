package com.loy124.myapp.core.handler;

import com.loy124.myapp.core.util.auth.MemberDetails;
import com.loy124.myapp.member.entity.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<Member> {

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            return Optional.empty();
        }

        if(!(authentication.getPrincipal() instanceof String)){

            if(authentication.getPrincipal() instanceof MemberDetails){
                MemberDetails principal = (MemberDetails) (authentication.getPrincipal());
                Member member = principal.getMember();
                return Optional.of(member);
            }


        }




//        if(authentication.getPrincipal() instanceof Member){
//
//
//            return Optional.of(userDetails.getMember().getId().toString());
//        }

        return Optional.empty();
//        if(authentication.getPrincipal() != null)




//        String email = JwtTokenUtil.getEmailFromToken((String) authentication.getCredentials());
//        return Optional.of("check");
    }
}
