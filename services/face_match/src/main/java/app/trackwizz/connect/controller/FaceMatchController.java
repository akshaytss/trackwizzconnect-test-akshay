package app.trackwizz.connect.controller;

import app.trackwizz.connect.dto.photomatch.FaceMatchRequestDto;
import app.trackwizz.connect.dto.photomatch.FaceMatchResponseDto;
import app.trackwizz.connect.service.face_match.FaceMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facematch")
public class FaceMatchController {
    private final FaceMatchService faceMatchService;

    public FaceMatchController(FaceMatchService faceMatchService) {
        this.faceMatchService = faceMatchService;
    }

    @Operation(description = "Gets image match details application/json", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FaceMatchResponseDto> matchFromImageViaFormData(@RequestHeader(required = false) String correlationId, @RequestBody FaceMatchRequestDto faceMatchRequestDto) {
        return match(faceMatchRequestDto, correlationId);
    }

    private ResponseEntity<FaceMatchResponseDto> match(FaceMatchRequestDto request, String correlationId) {
        FaceMatchResponseDto response = faceMatchService.faceMatch(request, correlationId);
        return response == null ? ResponseEntity.badRequest().body(null) : ResponseEntity.ok(response);
    }
}

