package app.trackwizz.connect.controller;

import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsRequestDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsResponseDto;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.service.company_details.ICompanyDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyDetailsController {
    private final ICompanyDetailsService ICompanyDetailsService;
    private final Logger log4j = LogManager.getLogger(CompanyDetailsController.class);

    public CompanyDetailsController(ICompanyDetailsService ICompanyDetailsService) {
        this.ICompanyDetailsService = ICompanyDetailsService;
    }

    @GetMapping()
    @Operation(description = "Gets company details by company id or company name")
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> getCompanyDetailsByCompanyIdOrByCompanyName(@RequestParam(required = false) Integer companyId, @RequestParam(required = false) String companyName) {
        boolean requestHasCompanyId = !ObjectUtils.isEmpty(companyId);
        boolean requestHasCompanyName = !ObjectUtils.isEmpty(companyName);
        String logDetailsString = requestHasCompanyId ? "companyId: " + companyId : "companyName: " + companyName;

        if (!requestHasCompanyId && !requestHasCompanyName)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, "Invalid input companyId and companyName both are empty"));
        if (requestHasCompanyId && requestHasCompanyName)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, "Invalid input companyId and companyName both cannot be used"));
        try {
            return ICompanyDetailsService.getCompanyDetailsByCompanyIdOrCompanyName(companyId, companyName, requestHasCompanyId);
        } catch (Exception e) {
            log4j.error("Error while processing fetch request " + logDetailsString + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while processing fetch request"));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creates company")
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> createCompanyDetailsViaJsonBody(@RequestBody CompanyDetailsRequestDto companyDetailsRequestDto) {
        return createCompanyDetails(companyDetailsRequestDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Creates company")
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> createCompanyDetailsViaFormData(@ModelAttribute CompanyDetailsRequestDto companyDetailsRequestDto) {
        return createCompanyDetails(companyDetailsRequestDto);
    }

    private ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> createCompanyDetails(CompanyDetailsRequestDto companyDetailsRequestDto) {
        try {
            return ICompanyDetailsService.createCompany(companyDetailsRequestDto);
        } catch (Exception e) {
            log4j.error("Error while creating company" + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while processing entity"));
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update company by company id")
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> updateCompanyDetailsViaJsonBody(@RequestParam Integer companyId, @RequestBody CompanyDetailsRequestDto companyDetailsRequestDto) {
        return updateCompanyDetails(companyId, companyDetailsRequestDto);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Update company by company id")
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> updateCompanyDetailsViaFormData(@RequestParam Integer companyId, @ModelAttribute CompanyDetailsRequestDto companyDetailsRequestDto) {
        return updateCompanyDetails(companyId, companyDetailsRequestDto);
    }

    private ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> updateCompanyDetails(Integer companyId, CompanyDetailsRequestDto companyDetailsRequestDto) {
        try {
            return ICompanyDetailsService.updateCompanyDetailsByCompanyId(companyId, companyDetailsRequestDto);
        } catch (Exception e) {
            log4j.error("Error while updated company details for companyId: " + companyId + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while updated company details for companyId: " + companyId));
        }
    }

    @DeleteMapping()
    @Operation(description = "Delete company by company id")
    public ResponseEntity<ApiResponseDto<Void>> markCompanyAsDeleted(@RequestParam Integer companyId) {
        try {
            return ICompanyDetailsService.markCompanyAsDeleted(companyId);
        } catch (Exception e) {
            log4j.error("Error while removing company for companyId: " + companyId + LogHelper.getExceptionDetails(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while removing company for companyId: " + companyId));
        }
    }
}