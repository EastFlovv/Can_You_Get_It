import React from "react";
import style from "./Header.module.css";
import { useNavigate } from "react-router-dom";
import logo from "img/logo.png";
import { useRecoilState } from "recoil";
import { userId, userRole, userNick } from "../../util/store";
import { $_user } from "util/axios";
import { useMutation } from "@tanstack/react-query";

export default function Header() {
  const navigate = useNavigate();
  const [id, setId] = useRecoilState(userId);
  const [role, setRole] = useRecoilState(userRole);
  const [nickName, setNickName] = useRecoilState(userNick);

  // API_POST 함수
  const res_put = () => {
    return $_user.put("/user/lgout", {
      accessToken: sessionStorage.getItem("accessToken"),
      refreshToken: sessionStorage.getItem("refreshToken"),
    });
  };

  // 로그아웃 함수
  const { mutate: onLogout } = useMutation(res_put, {
    onSuccess: () => {
      sessionStorage.clear();
      setId("");
      setRole("");
      setNickName("");
      navigate("/");
    },
  });

  return (
    <>
      {userId !== "kjskjs356@naver.com" && (
        <div className={style.header_admin}>
          <div>
            <div
              onClick={() => {
                navigate("../");
              }}
              className={style.header_left_admin}
            >
              <img className={style.logo} src={logo} alt="" />{" "}
              <p className={style.logo_name}>CAN YOU GET IT</p>
            </div>
          </div>
          <div className={style.header_right_admin}>
            <div
              className={style.nologin}
              onClick={() => {
                navigate("../login");
              }}
            >
              비로그인 메인페이지
            </div>
            <div
              className={style.login}
              onClick={() => {
                navigate("../home");
              }}
            >
              로그인 메인페이지
            </div>
            <div
              className={style.mypage}
              onClick={() => {
                navigate("../mypage");
              }}
            >
              유저 마이페이지
            </div>
            <div
              className={style.admin}
              onClick={() => {
                navigate("../admin/total");
              }}
            >
              관리자페이지
            </div>
            <div
              className={style.loading}
              onClick={() => {
                navigate("../loading");
              }}
            >
              로딩스피너 확인용
            </div>
          </div>
        </div>
      )}
      {userId === "kjskjs356@naver.com" && (
        <div className={style.header_user}>
          <div>
            <div
              onClick={() => {
                navigate("../");
              }}
              className={style.header_left_user}
            >
              <img className={style.logo} src={logo} alt="" />{" "}
              <p className={style.logo_name}>CAN YOU GET IT</p>
            </div>
          </div>
          <div className={style.header_right_user}>
            <div className={style.user_name}>{nickName}님 환영합니다.</div>
            <div className={style.user_point}>500,000원</div>
            <div className={style.user_logout} onClick={() => onLogout()}>
              로그아웃
            </div>
          </div>
        </div>
      )}
    </>
  );
}
