package com.appliance.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil
{

  public static Logger logger = LogManager.getLogger(JwtTokenUtil.class.getName());

  private static final String CLAIM_KEY_USERNAME = "sub";
//  private static final String CLAIM_KEY_TOKENS = "tokens";
  private static final String CLAIM_KEY_AUDIENCE = "audience";
  private static final String CLAIM_KEY_CREATED = "created";

  private static final String AUDIENCE_WEB = "web";

  private static String secret = System.getProperty("secret","23232133ojldsfdsaf");
  private static Long  expiration = Long.parseLong(System.getProperty("expiration",String.valueOf(24*60*60)));

  public static String getUsernameFromToken(String token)
  {
    String username;
    try
    {
      final Claims claims = getClaimsFromToken(token);
      username = claims.getSubject();
    }
    catch (Exception e)
    {
      username = null;
    }
    return username;
  }

  public Date getCreatedDateFromToken(String token)
  {
    Date created;
    try
    {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
    }
    catch (Exception e)
    {
      created = null;
    }
    return created;
  }

  public static Date getExpirationDateFromToken(String token)
  {
    Date expiration;
    try
    {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    }
    catch (Exception e)
    {
      expiration = null;
    }
    return expiration;
  }

  public String getAudienceFromToken(String token)
  {
    String audience;
    try
    {
      final Claims claims = getClaimsFromToken(token);
      audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
    }
    catch (Exception e)
    {
      audience = null;
    }
    return audience;
  }

  private static Claims getClaimsFromToken(String token)
  {
    Claims claims;
    try
    {
      claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    catch (Exception e)
    {
      claims = null;
    }
    return claims;
  }

  private static Date generateExpirationDate()
  {
    return new Date(System.currentTimeMillis() + expiration * 1000);
  }

  public static Boolean isTokenExpired(String token)
  {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public static String generateToken(String username)
  {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_KEY_USERNAME, username);
    claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
    claims.put(CLAIM_KEY_CREATED, System.currentTimeMillis());
//    claims.put(CLAIM_KEY_TOKENS, tokens);
    return generateToken(claims);
  }
public static void main(String[] ar) {
    String token = generateToken("axbMachine");
    String userName = getUsernameFromToken(token);
    System.out.println(token);
    System.out.println(userName);
}
  private static String generateToken(Map<String, Object> claims)
  {
    return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public String refreshToken(String token)
  {
    String refreshedToken;
    try
    {
      final Claims claims = getClaimsFromToken(token);
      claims.put(CLAIM_KEY_CREATED, System.currentTimeMillis());
      refreshedToken = generateToken(claims);
    }
    catch (Exception e)
    {
      refreshedToken = null;
    }
    return refreshedToken;
  }


  /**
   * get token from request
   * @param request
   * @return
   */
  public static String getToken(HttpServletRequest request,String tokenHeader,String tokenName)
  {
    String authToken = request.getHeader(tokenHeader);
    if(StringUtils.isEmpty(authToken))
    {
      authToken = request.getParameter(tokenName);
    }

    return authToken;
  }

/*  public static String getUserIdFromContext() {
    return getUsernameFromToken((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }*/
}