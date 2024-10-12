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
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {


    Member memberPostDtoToMember(MemberDto.Post postDto);

    @Mapping(source = "guardian.name", target = "guardianName")
    @Mapping(source = "guardian.phone", target = "guardianPhone")
    MemberDto.Response memberToResponseDto(Member member);

    List<MemberDto.Response> membersToResponseDtos(List<Member> members);
    void updateMemberFromPatchDto(MemberDto.Patch patchDto, @MappingTarget Member member);
}
