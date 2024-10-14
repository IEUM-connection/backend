package com.springboot.memberHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberHistoryMapper {

    MemberHistoryDto memberHistoryToMemberHistoryDto(MemberHistory memberHistory);
}



