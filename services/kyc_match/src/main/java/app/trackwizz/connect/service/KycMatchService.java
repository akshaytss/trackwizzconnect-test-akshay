package app.trackwizz.connect.service;

import app.trackwizz.connect.dto.kycmatch.KycMatchRequestDto;
import app.trackwizz.connect.dto.kycmatch.KycMatchResponseDto;

import org.springframework.stereotype.Service;

@Service
public class KycMatchService implements IKycMatchService {
    private final KycMatchHelperService kycMatchHelperService;

    public KycMatchService(KycMatchHelperService kycMatchHelperService) {
        this.kycMatchHelperService = kycMatchHelperService;
    }
    @Override
    public KycMatchResponseDto matchData(KycMatchRequestDto requestData) {
        KycMatchResponseDto response = null;
        try {
            response = new KycMatchResponseDto();
            kycMatchHelperService.generateAPIResponseData(response, requestData);
            return response;
        }
        catch (Exception ex) {
            return response;
        }
    }
}
