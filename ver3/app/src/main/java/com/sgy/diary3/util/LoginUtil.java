package com.sgy.diary3.util;

import android.content.Intent;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.AuthErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.sgy.diary3.R;
import com.sgy.diary3.base.MyApplication;
import com.sgy.diary3.ui.MainActivity;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
// TODO 데이터 삭제해도 자동로그인 되는 거 문제 해결
public class LoginUtil {

    public static LoginUtil instance;
    public Function2<OAuthToken, Throwable, Unit> loginCallback;

    public LoginUtil() {
        loginKakao();
    }

//    public static LoginUtil getInstance() { // 한번만 생성
//        if (instance == null) {
//            instance = new LoginUtil();
//        }
//        return instance;
//    }

    /**
     * 카카오톡 로그인
     * - 로그인 시 발생할 수 있는 에러들 예외 및 로그 처리
     */
    public void loginKakao() {
        this.loginCallback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable error) {
                if (error != null) { // 에러
                    Utils.mLog(MyApplication.context.getString(R.string.kakao_login_fail) + " : " + error.toString());
                    if (error.toString().equals(AuthErrorCause.AccessDenied.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_access_denied) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.InvalidClient.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_invalid_client) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.InvalidGrant.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_invalid_grant) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.InvalidRequest.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_invalid_request) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.InvalidScope.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_invalid_scope) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.Misconfigured.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_misconfigured) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.ServerError.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_server) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.Unauthorized.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_unauthorized) + " : " + error.toString());
                    } else if (error.toString().equals(AuthErrorCause.Unknown.toString())) {
                        Utils.mLog(MyApplication.context.getString(R.string.kakao_err_unknown) + " : " + error.toString());
                    }
                } else if (oAuthToken != null) { // 성공
                    Utils.mLog(MyApplication.context.getString(R.string.kakao_login_success) + " : " + oAuthToken.getAccessToken());
                    Intent intent = new Intent(MyApplication.context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.context.startActivity(intent);
                }
                return null;
            }
        };
    }

    public void logoutKakao() {
        UserApiClient.getInstance().logout(error -> {
            if (error != null) {
                Utils.mLog(MyApplication.context.getString(R.string.kakao_logout_fail) + " : " + error.toString());
            } else {
                Utils.mLog(MyApplication.context.getString(R.string.kakao_logout_success));
            }
            return null;
        });
    }

    /**
     * 카카오톡 사용자 정보 가져오기
     */
    public void getKakaoUserInfo() {
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Utils.mLog(MyApplication.context.getString(R.string.kakao_get_user_info_fail) + meError.toString());
            } else {
                Utils.mLog(MyApplication.context.getString(R.string.kakao_get_user_info_success) + "\n회원번호 : " + user.getId() + "\n이메일 : " + Objects.requireNonNull(user.getKakaoAccount()).getEmail());
                Utils.mLog("getAccount : " + Objects.requireNonNull(user.getKakaoAccount()).toString());
                // getKakaoAccount : Profile(nickname=⭐, profileImageUrl=https://k.kakaocdn.net/dn/stanZ/btriCFkIJkd/bSrfKIdX2N4oxfuKXksu4k/img_640x640.jpg, thumbnailImageUrl=https://k.kakaocdn.net/dn/stanZ/btriCFkIJkd/bSrfKIdX2N4oxfuKXksu4k/img_110x110.jpg, isDefaultImage=false), nameNeedsAgreement=null, name=null, emailNeedsAgreement=false, isEmailValid=true, isEmailVerified=true, email=sgyoung96@naver.com, ageRangeNeedsAgreement=null, ageRange=null, birthyearNeedsAgreement=null, birthyear=null, birthdayNeedsAgreement=null, birthday=null, birthdayType=null, genderNeedsAgreement=null, gender=null, ciNeedsAgreement=null, ci=null, ciAuthenticatedAt=null, legalNameNeedsAgreement=null, legalName=null, legalBirthDateNeedsAgreement=null, legalBirthDate=null, legalGenderNeedsAgreement=null, legalGender=null, phoneNumberNeedsAgreement=null, phoneNumber=null, isKoreanNeedsAgreement=null, isKorean=null)
            }
            return null;
        });
    }
}