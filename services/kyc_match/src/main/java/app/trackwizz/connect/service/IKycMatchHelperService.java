package app.trackwizz.connect.service;

import app.trackwizz.connect.dto.kycmatch.KycMatchRequestDto;
import app.trackwizz.connect.dto.kycmatch.KycMatchResponseDto;
import app.trackwizz.connect.model.ApiCallLog;

public interface IKycMatchHelperService {
    public void generateAPIResponseData(KycMatchResponseDto responseData, KycMatchRequestDto requestData);

}
