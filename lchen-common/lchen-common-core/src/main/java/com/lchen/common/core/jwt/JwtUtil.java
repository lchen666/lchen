package com.lchen.common.core.jwt;

import com.alibaba.fastjson.JSONObject;
import com.lchen.common.core.utils.UUIDUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    /**
     * jwt的唯一身份标识
     */
    public static final String JWT_ID = UUIDUtils.simpleUUID();

    /**
     * jwt 加密的key
     */
    public static final String JWT_KEY = "KJHChenJYgYUllVbXhKDHXhkSyHjlNiVkYzWTBac1LXunChen";


    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(JWT_KEY);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public static String createJWT(JwtModel jwtModel, long expirationDate){
        // 设置头部信息
//		Map<String, Object> header = new HashMap<String, Object>();
//		header.put("typ", "JWT");
//		header.put("alg", "HS256");
        // 或
        // 指定header那部分签名的时候使用的签名算法，jjwt已经将这部分内容封装好了，只有{"alg":"HS256"}
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证的方式）
        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", "lchen");
        // 生成JWT的时间
        long nowTime = System.currentTimeMillis();
        Date issuedAt = new Date(nowTime);
        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露，是你服务端的私钥，在任何场景都不应该流露出去，一旦客户端得知这个secret，那就意味着客户端是可以自我签发jwt的
        SecretKey key = generalKey();
        // 为payload添加各种标准声明和私有声明
        JwtBuilder builder = Jwts.builder() // 表示new一个JwtBuilder，设置jwt的body
//				.setHeader(header) // 设置头部信息
                .setClaims(claims) // 如果有私有声明，一定要先设置自己创建的这个私有声明，这是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明
                .setId(JWT_ID) // jti(JWT ID)：jwt的唯一身份标识，根据业务需要，可以设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
                .setIssuedAt(issuedAt) // iat(issuedAt)：jwt的签发时间
                .setSubject(JSONObject.toJSONString(jwtModel)) // sub(subject)：jwt所面向的用户，放登录的用户名，一个json格式的字符串，可存放userid，roldid之类，作为用户的唯一标志
                .signWith(signatureAlgorithm, key); // 设置签名，使用的是签名算法和签名使用的秘钥
        // 设置过期时间
        if (expirationDate >= 0) {
            long exp = nowTime + expirationDate;
            builder.setExpiration(new Date(exp));
        }
        // 设置jwt接收者
//        if (audience == null || "".equals(audience)) {
//            builder.setAudience("Tom");
//        } else {
//            builder.setAudience(audience);
//        }
        return builder.compact();
    }

    // 解密jwt
    public static Claims parseJWT(String jwt) {
        SecretKey key = generalKey(); // 签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser() // 得到DefaultJwtParser
                .setSigningKey(key) // 设置签名的秘钥
                .parseClaimsJws(jwt).getBody(); // 设置需要解析的jwt
        return claims;
    }



    public static void main(String[] args) {
        JwtModel jwtModel = new JwtModel();
        jwtModel.setUserId(100L);
        jwtModel.setPhone("17682489388");
        jwtModel.setUserName("176****9388");
        String token = createJWT(jwtModel,1000*30);
        System.out.println("token: "+token);
        Claims claims = parseJWT(token);
        System.out.println("解析jwt："+claims.getSubject());
        JSONObject tem = JSONObject.parseObject(claims.getSubject());
        System.out.println("获取json对象内容："+tem.getString("userName"));
        System.out.println(claims.getExpiration()+"///"+claims.getExpiration().getTime());
    }
}
