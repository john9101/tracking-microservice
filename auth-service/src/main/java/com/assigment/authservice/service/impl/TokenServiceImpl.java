package com.assigment.authservice.service.impl;

import com.assigment.authservice.service.TokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
//    private final JwtEncoder jwtEncoder;

//    public String generateToken(String subject) {
//        Instant now = Instant.now();
//        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
//                .subject(subject)
//                .issuedAt(now)
//                .expiresAt(now.plus(1, ChronoUnit.HOURS))
//                .claim("roles",  Collections.singletonList("ROLE_USER"))
//                .build();
//        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
//        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,jwtClaimsSet)).getTokenValue();
//    }

    private final RSAKey rsaKey;

    @Override
    public String generateToken(UserDetails userDetails) {
        JWSSigner signer;
        try {
            signer = new RSASSASigner(rsaKey);
            Date now = Date.from(Instant.now());
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issueTime(now)
                    .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                    .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader
                    .Builder(JWSAlgorithm.RS256)
                    .keyID(rsaKey.getKeyID())
                    .build(), claims);

            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Token generation failed", e);
        }
    }
}
