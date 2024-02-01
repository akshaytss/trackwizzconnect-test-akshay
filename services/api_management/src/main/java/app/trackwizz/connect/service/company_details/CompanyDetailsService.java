package app.trackwizz.connect.service.company_details;

import app.trackwizz.connect.constant.MessageConstants;
import app.trackwizz.connect.dto.ApiResponseDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsRequestDto;
import app.trackwizz.connect.dto.company_details.CompanyDetailsResponseDto;
import app.trackwizz.connect.factory.company_details.ICompanyDetailsFactory;
import app.trackwizz.connect.helper.log.LogHelper;
import app.trackwizz.connect.model.CompanyDetails;
import app.trackwizz.connect.service.company_details.db.ICompanyDetailsDbService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Service
public class CompanyDetailsService implements ICompanyDetailsService {
    private final ICompanyDetailsDbService ICompanyDetailsDbService;
    private final ICompanyDetailsFactory ICompanyDetailsFactory;
    private final Logger log4j = LogManager.getLogger(CompanyDetailsService.class);

    public CompanyDetailsService(ICompanyDetailsDbService ICompanyDetailsDbService, ICompanyDetailsFactory ICompanyDetailsFactory) {
        this.ICompanyDetailsDbService = ICompanyDetailsDbService;
        this.ICompanyDetailsFactory = ICompanyDetailsFactory;
    }

    @Override
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> getCompanyDetailsByCompanyIdOrCompanyName(Integer companyId, String companyName, boolean requestHasCompanyId) {
        String logDetailsString = requestHasCompanyId ? "companyId: " + companyId : "companyName: " + companyName;

        Optional<CompanyDetails> companyDetails;
        if (requestHasCompanyId) {
            companyDetails = ICompanyDetailsDbService.getCompanyDetails(companyId);
        } else {
            companyDetails = ICompanyDetailsDbService.getCompanyDetails(companyName);
        }
        if (companyDetails.isPresent()) {
            if (companyDetails.get().isCompanyDeleted()) {
                log4j.info(MessageConstants.COMPANY_NOT_FOUND + " " + logDetailsString + " is marked as deleted");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.COMPANY_NOT_FOUND));
            } else {
                log4j.info(MessageConstants.COMPANY_RETRIEVED_SUCCESSFULLY + " " + logDetailsString);
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(ICompanyDetailsFactory.getCompanyDetailsResponseDto(companyDetails.get()), MessageConstants.COMPANY_RETRIEVED_SUCCESSFULLY));
            }
        } else {
            log4j.info(MessageConstants.COMPANY_NOT_FOUND + " " + logDetailsString);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.COMPANY_NOT_FOUND));
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> updateCompanyDetailsByCompanyId(Integer companyId, CompanyDetailsRequestDto companyDetailsRequestDto) {
        CompanyDetails companyDetails = ICompanyDetailsFactory.getCompanyDetails(companyDetailsRequestDto);
        Optional<CompanyDetails> existingCompany = ICompanyDetailsDbService.getCompanyDetails(companyId);
        if (existingCompany.isPresent() && !existingCompany.get().isCompanyDeleted()) {
            CompanyDetails saveCompanyDetails = existingCompany.get();
            saveCompanyDetails.setCompanyName(companyDetails.getCompanyName());
            saveCompanyDetails.setCompanyEmail(companyDetails.getCompanyEmail());

            try {
                saveCompanyDetails = ICompanyDetailsDbService.saveCompanyDetails(saveCompanyDetails);
            } catch (DataIntegrityViolationException e) {
                log4j.info("New company name already exists companyName:" + companyDetails.getCompanyName() + " updated requested on companyId: " + companyId);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseDto.error(null, "New company name already exists"));
            } catch (ConstraintViolationException e) {
                log4j.info(MessageConstants.INVALID_EMAIL + companyDetails.getCompanyEmail() + " updated requested on companyId: " + companyId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, MessageConstants.INVALID_EMAIL + companyDetails.getCompanyEmail()));
            } catch (Exception e) {
                log4j.error("Error while updated company details for companyId: " + companyId + LogHelper.getExceptionDetails(e));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error(null, "Error while updated company details for companyId: " + companyId));
            }

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(ICompanyDetailsFactory.getCompanyDetailsResponseDto(saveCompanyDetails), MessageConstants.COMPANY_UPDATED_SUCCESSFULLY));
        }
        log4j.info(MessageConstants.COMPANY_NOT_FOUND + " companyId: " + companyId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.COMPANY_NOT_FOUND));
    }

    @Override
    public ResponseEntity<ApiResponseDto<Void>> markCompanyAsDeleted(Integer companyId) {
        try {
            ICompanyDetailsDbService.setIsCompanyDeletedToTrueByCompanyId(companyId);
        } catch (EntityNotFoundException e) {
            log4j.info(MessageConstants.COMPANY_NOT_FOUND + " companyId: " + companyId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(null, MessageConstants.COMPANY_NOT_FOUND));
        }
        log4j.info("Company Details marked as deleted for companyId:" + companyId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(null, "Company Details removed for companyId:" + companyId));
    }

    @Override
    public ResponseEntity<ApiResponseDto<CompanyDetailsResponseDto>> createCompany(CompanyDetailsRequestDto companyDetailsRequestDto) {
        try {
            CompanyDetails companyDetails = ICompanyDetailsFactory.getCompanyDetails(companyDetailsRequestDto);
            CompanyDetails savedCompanyDetails = ICompanyDetailsDbService.saveCompanyDetails(companyDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(ICompanyDetailsFactory.getCompanyDetailsResponseDto(savedCompanyDetails), MessageConstants.COMPANY_CREATED_SUCCESSFULLY));
        } catch (DataIntegrityViolationException e) {
            log4j.info("Company name already exists companyName:" + companyDetailsRequestDto.getCompanyName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseDto.error(null, "Company name: " + companyDetailsRequestDto.getCompanyName() + " already exists"));
        } catch (ConstraintViolationException e) {
            log4j.info(MessageConstants.INVALID_EMAIL + companyDetailsRequestDto.getCompanyEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error(null, MessageConstants.INVALID_EMAIL + companyDetailsRequestDto.getCompanyEmail()));
        }
    }
}
