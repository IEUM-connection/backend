package com.springboot.member.mapper;

import com.springboot.member.dto.GuardianDto;
import com.springboot.member.dto.GuardianDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Guardian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuardianMapper {


    Guardian guardianPostDtoToguardian(GuardianDto.Post postDto);

    @Mapping(source = "guardian.guardianId", target = "guardianId")
    GuardianDto.Response guardianToResponseDto(Guardian guardian);

    Guardian patchDtoToGuardian(GuardianDto.Patch patchDto);


    GuardianDto.Response guardianToGuardianResponseDto(Guardian updatedguardian);
}
