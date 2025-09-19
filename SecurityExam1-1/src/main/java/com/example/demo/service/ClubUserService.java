package com.example.demo.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ClubUser;
import com.example.demo.domain.ClubUser.ClubRole;
import com.example.demo.domain.ClubUserRegistrationDto;
import com.example.demo.repo.ClubUserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubUserService {
	
    private final ClubUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

	public ClubUser registerNewUser(ClubUserRegistrationDto registrationDto) {
		// TODO Auto-generated method stub
		
		// 
		// 중복검사.
		if(userRepository.existsByUsername(registrationDto.getUsername())) {
			throw new RuntimeException("이미 존재하는 사용자명임: " + registrationDto.getUsername());
		}
		
        // 새 사용자 생성
        ClubUser user = new ClubUser();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setNickname(registrationDto.getNickname());
        if (userRepository.count() == 0) {
            user.setRoles(Set.of(ClubRole.ROLE_ADMIN, ClubRole.ROLE_VIP, ClubRole.ROLE_USER));
            System.out.println("첫 번째 사용자에게 ADMIN 권한을 부여했습니다!");
        }else {
            user.setRoles(Set.of(ClubRole.ROLE_USER));
        }

        
        ClubUser saveUser = userRepository.save(user);
        return saveUser;
		
	}
	
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
	
	// VIP 승급 메서드(promoteToVip)
    // 1. VIP로 승급시킬 사람의 정보(ID)가 필요
    // 2. VIP 권한만 업데이트(저장)시키면됨.
    
    // VIP 승급 메서드
    public void promoteToVip(String username) {
        ClubUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        
        user.getRoles().add(ClubRole.ROLE_VIP);
        userRepository.save(user);
        System.out.println("🌟 " + username + "님이 VIP로 승급되었습니다!");
    }
   
    
    
    
    
    
    
    
	
	

}
