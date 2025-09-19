package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ClubUser;
import com.example.demo.repo.ClubUserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class ClubUserDetailsService implements UserDetailsService{

	private final ClubUserRepository userRepository;
	
    public ClubUserDetailsService(ClubUserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("사용자 인증 시도 확인");
		
		ClubUser c_user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("사용자 찾을수 없음" +username));
			
		c_user.setLastLoginAt(LocalDateTime.now());
		userRepository.save(c_user);
		
		System.out.println("사용자 확인 : " + c_user.getUsername() +
						   "권한 : " + c_user.getRoles());
		
		return User.builder()
                .username(c_user.getUsername())
                .password(c_user.getPassword())
                .authorities(mapRolesToAuthorities(c_user)) 
                // -> c_user 객체의 역할(즉 권한)을 시큐리티에 맞게 매핑해주는 메서드.
                
                // 아래의 accountExpired, accountLocked, credentialsExpired는
                // 계정 만료, 잠겨있지 않은지, 혹은 증명되지 않은지를 물어보는 메서드.
                // 근데 왜 ! 붙임? 
                //  -> 기존 계정이 잠겨있지 않았다면 새 계정은 잠겨있지 않음으로
                //  -> 잠겨있다면 잠김으로 설정하기 위해.
                .accountExpired(!c_user.isAccountNonExpired())
                .accountLocked(!c_user.isAccountNonLocked())
                .credentialsExpired(!c_user.isCredentialsNonExpired())
                .disabled(!c_user.isEnabled())
                .build();
		

	}

	//권한 뭔지 리턴받아올거임 
	// 이경우는 권한 컬럼에 한 유저가 여러개의 권한을 가질수도 있는경우에 처리하는 방식.
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(ClubUser user) {
		// TODO Auto-generated method stub
		 return user.getRoles().stream()
				 // 사용자 역할정보를 SimpleGrantedAuthority 타입으로 변환하여 리턴하는 역할
	                .map(role -> new SimpleGrantedAuthority(role.name()))
	                // 모든 요소 처리후 결과를 List 타입으로 수집.
	                .collect(Collectors.toList());
			// 근데 너무 어려우면 다음과 같이 처리해도 문제x
//			List<SimpleGrantedAuthority> authorities = [
//			                                            new SimpleGrantedAuthority("ADMIN"),
//			                                            new SimpleGrantedAuthority("USER")
//			                                        ];
		 
		 
	}

	
	
	
	
	
	
	
}
