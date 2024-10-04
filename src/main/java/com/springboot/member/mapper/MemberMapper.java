package com.springboot.member.mapper;

import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    // authorities 매핑 부분을 제거하고, 불필요한 매핑 제거
    Member memberPostDtoToMember(MemberDto.Post postDto);

    @Mapping(source = "member.memberId", target = "memberId")
    MemberDto.Response memberToResponseDto(Member member);

    void updateMemberFromPatchDto(MemberDto.Patch patchDto, @MappingTarget Member member);
}
