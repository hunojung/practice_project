package com.buying.back.application.inquiry.controller;

import static com.buying.back.util.response.CommonResponseCode.SUCCESS;

import com.buying.back.application.common.dto.PagingDTO;
import com.buying.back.application.inquiry.controller.dto.CreateInquiryDTO;
import com.buying.back.application.inquiry.controller.dto.UpdateInquiryDTO;
import com.buying.back.application.inquiry.service.InquiryService;
import com.buying.back.application.inquiry.service.vo.InquiryVO;
import com.buying.back.infra.config.security.loginuser.LoginUser;
import com.buying.back.util.response.CommonResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pub/inquiries")
@RequiredArgsConstructor
public class NormalInquiryController {

  private final InquiryService inquiryService;

  @PostMapping
  public CommonResponse<InquiryVO> createInquiry(@AuthenticationPrincipal LoginUser loginUser,
    @Valid @RequestBody CreateInquiryDTO dto) {
    InquiryVO vo = inquiryService.createInquiry(loginUser.getId(), dto);
    return new CommonResponse<>(vo, SUCCESS);
  }

  @GetMapping
  public CommonResponse<Page<InquiryVO>> getInquiryByAccount(
    @AuthenticationPrincipal LoginUser loginUser,
    @Valid @RequestBody PagingDTO dto) {
    Page<InquiryVO> vo = inquiryService.getInquiryByAccount(loginUser.getId(), dto);
    return new CommonResponse<>(vo, SUCCESS);
  }

  @PutMapping("/{inquiry-id}")
  public CommonResponse<InquiryVO> updateInquiry(@AuthenticationPrincipal LoginUser loginUser,
    @PathVariable(value = "inquiry-id") Long inquiryId,
    @Valid @RequestBody UpdateInquiryDTO dto) {
    InquiryVO vo = inquiryService.updateInquiry(loginUser.getId(), inquiryId, dto);
    return new CommonResponse<>(vo, SUCCESS);
  }

  @DeleteMapping("/{inquiry-id}")
  public CommonResponse<Void> deleteInquiry(@AuthenticationPrincipal LoginUser loginUser,
    @PathVariable(value = "inquiry-id") Long inquiryId) {
    inquiryService.deleteInquiry(loginUser.getId(), inquiryId);
    return new CommonResponse<>(SUCCESS);
  }

}
