package app.trackwizz.connect.service.face_match;

import app.trackwizz.connect.dto.photomatch.FaceMatchRequestDto;
import app.trackwizz.connect.dto.photomatch.FaceMatchResponseDto;

import java.util.concurrent.ExecutionException;

public interface IFaceMatchService {
    /**
     * To call external api to read given image
     *
     * @param faceMatchRequestDto instance of {@link FaceMatchRequestDto}
     * @return instance of {@link FaceMatchResponseDto}
     */
    FaceMatchResponseDto faceMatch(FaceMatchRequestDto faceMatchRequestDto, String correlationId);
}
