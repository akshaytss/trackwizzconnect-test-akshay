package app.trackwizz.connect.service;

import app.trackwizz.connect.dto.kycmatch.KycMatchRequestDto;
import app.trackwizz.connect.dto.kycmatch.KycMatchResponseDto;

public interface IKycMatchService {

    KycMatchResponseDto matchData(KycMatchRequestDto requestData);
}
