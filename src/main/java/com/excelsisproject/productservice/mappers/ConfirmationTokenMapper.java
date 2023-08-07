package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.entities.ConfirmationToken;

public class ConfirmationTokenMapper {

    public static ConfirmationTokenDto mapToConfirmationTokenDto(ConfirmationToken confirmationToken){
        return new ConfirmationTokenDto(
                confirmationToken.getId(),
                confirmationToken.getConfirmationToken(),
                confirmationToken.getTimeCreated(),
                confirmationToken.getTimeExpired(),
                confirmationToken.getTimeConfirmed(),
                confirmationToken.getUser()
        );
    }

    public static ConfirmationToken mapToConfirmationToken(ConfirmationTokenDto confirmationTokenDto){
        return new ConfirmationToken(
                confirmationTokenDto.getId(),
                confirmationTokenDto.getConfirmationToken(),
                confirmationTokenDto.getTimeCreated(),
                confirmationTokenDto.getTimeExpired(),
                confirmationTokenDto.getTimeConfirmed(),
                confirmationTokenDto.getUser()
        );
    }
}
