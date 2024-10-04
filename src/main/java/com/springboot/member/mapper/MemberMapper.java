package com.springboot.member.mapper;

import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    @Mapping(target = "status", ignore = true) // 상태는 기본값이므로 무시
    @Mapping(target = "memberCode", ignore = true) // 회원 코드는 서비스에서 생성
    @Mapping(target = "guardian", ignore = true) // guardian 엔티티는 별도 처리
    Member memberPostDtoToMember(MemberDto.Post postDto);

    // MemberDto.Patch -> Member 엔티티로 업데이트
    @Mapping(target = "status", ignore = true) // 상태는 수정하지 않음
    @Mapping(target = "memberCode", ignore = true) // 회원 코드는 수정하지 않음
    @Mapping(target = "guardian", ignore = true) // guardian 엔티티는 수정하지 않음
    void updateMemberFromPatchDto(MemberDto.Patch patchDto, Member member);

    // Member 엔티티 -> MemberDto.Response로 변환
    @Mapping(source = "guardian.guardianId", target = "guardianId") // Guardian의 ID만 응답에 포함
    MemberDto.Response memberToResponseDto(Member member);
}
