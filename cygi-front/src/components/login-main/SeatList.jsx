import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import style from "./SeatList.module.css";
import { useQuery, useMutation } from "@tanstack/react-query";
import { history } from "./history";
import { $_concert } from "util/axios";
import axios from "axios";
import { userNick, reservation } from "util/store";
import { useRecoilValue, useRecoilState } from "recoil";

export default function SeatList() {
  const navigate = useNavigate();
  const location = useLocation();
  let nowTime = new Date();

  const [seat, setSeat] = useState();
  const [cols, setCols] = useState();
  const [rows, setRows] = useState();

  const [isPointBtn, setIsPointBtn] = useState(false);
  const [isKakaoBtn, setIsKakaoBtn] = useState(false);

  const nickName = useRecoilValue(userNick);
  const [reservationInfo, setReservationInfo] = useRecoilState(reservation);

  // 좌석선택 페이지 or 예매화면 페이지 변환용 변수
  const [isreserve, setIsreserve] = useState(false);

  // 결제 준비를 위한 함수
  const paymentData = {
    cid: "TC0ONETIME",
    partner_order_id: reservationInfo.reservationId,
    partner_user_id: nickName,
    item_name: location.state.title,
    quantity: 1,
    total_amount: location.state.price,
    tax_free_amount: 0,
    approval_url: "http://localhost:3000/success",
    cancel_url: "http://localhost:3000/home",
    fail_url: "http://localhost:3000/home",
  };

  const preparePayment = async (paymentData) => {
    const headers = {
      "Content-Type": "application/json; charset=UTF-8",
    };
    const response = await axios.post(
      "http://localhost:8001/api/v1/reservation/charge",
      paymentData,
      { headers }
    );

    if (response.status === 200) {
      // PC에서 결제 진행
      const tid = response.data.result.tid;
      localStorage.setItem("tid", tid);
      window.location.href = response.data.result.next_redirect_pc_url;
    } else {
      alert("문제가 발생하였습니다.");
    }
    return response.data;
  };

  // 해당 공연장의 좌석 조회
  const { isLoading, data, refetch } = useQuery(
    [`seat-list_${location.state.concertId}`],
    () => $_concert.get(`/seat/${location.state.concertId}`)
  );

  const newData = {
    concertId: location.state.concertId,
    seat: seat,
  };

  // API_DELETE 함수
  const res_delete = () => {
    return $_concert.delete(
      `/seat/delete/${reservationInfo.reservationId}`,
      reservationInfo.reservationId
    );
  };

  // 페이지 벗어날 시 이벤트 발생
  const { mutate: onDelete } = useMutation(res_delete);

  const handleBeforeUnload = (e) => {
    e.preventDefault();
    e.returnValue = "";
    setReservationInfo({
      title: "",
      reservationId: "",
      seat: "",
      price: 0,
      date: "",
    });
  };

  // 뒤로가기 이벤트 감지
  useEffect(() => {
    const listenBackEvent = () => {
      if (window.confirm("페이지를 나가시겠습니까?")) {
        setReservationInfo({
          title: "",
          reservationId: "",
          seat: "",
          price: 0,
          date: "",
        });
        onDelete();
        navigate("/");
      }
    };
    const historyEvent = history.listen(({ action }) => {
      if (action === "POP") {
        listenBackEvent();
      }
    });
    return historyEvent;
  }, []);

  useEffect(() => {
    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  //API_POST 함수
  const res_post = () => {
    return $_concert.post(`/seat`, newData);
  };

  // 예매하기 클릭 시 이벤트 발생
  const { mutate: onSelect } = useMutation(res_post, {
    onSuccess: (res) => {
      setIsreserve(true);
      setReservationInfo({
        title: location.state.title,
        reservationId: res.data,
        seat: seat,
        price: location.state.price,
        date: location.state.endDate,
      });
    },
  });

  const rowNo = [
    "",
    "A",
    "B",
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
  ];

  const onClickBtn = (e) => {
    if (e === "point") {
      if (isKakaoBtn) setIsKakaoBtn(false);
      setIsPointBtn(!isPointBtn);
    } else {
      if (isPointBtn) setIsPointBtn(false);
      setIsKakaoBtn(!isKakaoBtn);
    }
  };

  const onClicked = (e) => {
    if (e === seat) setSeat();
    else setSeat(e);
  };
  const getKeyByValue = (obj, value) => {
    return Object.keys(obj).find((key) => obj[key] === value);
  };

  //각 좌석의 btn 태그 생성
  const renderSeat = (row, col) => {
    const value = `${rowNo[row]}${col}`;
    const key = getKeyByValue(data.data.seatList, value);
    return (
      <button
        key={value}
        value={value}
        className={
          key !== undefined
            ? style.seat_reserved
            : value === seat
            ? style.seat_selected
            : style.seat
        }
        onClick={() => onClicked(value)}
        disabled={key !== undefined ? true : false}
      ></button>
    );
  };

  // 각 행의 좌석 정보 호출
  const renderRow = (row) => {
    const seats = [];
    seats.push(
      <div key={row} className={style.seat_row_name}>
        <span>{rowNo[row]}행</span>
      </div>
    );
    for (let col = 1; col <= cols; ++col) {
      seats.push(renderSeat(row, col));
    }
    return (
      <div key={row} className={style.seat_row}>
        {seats}
      </div>
    );
  };

  // 전체 좌석정보 저장하는 배열
  const rowsElements = [];
  for (let row = 1; row <= rows; ++row) {
    rowsElements.push(renderRow(row));
  }

  const onCheck = () => {
    if (seat === undefined || seat === null) {
      alert("좌석을 선택하여주세요.");
      return;
    }
    if (nowTime >= new Date(data.data.endDate)) {
      alert("마감된 공연입니다.");
      navigate("/home");
    }
    // 좌석 한번 더 갱신
    refetch();
    const key = getKeyByValue(data.data.seatList, seat);
    if (key === undefined) {
      onSelect();
    } else {
      alert("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
      setSeat();
    }
  };

  useEffect(() => {
    if (!isLoading) {
      setCols(data.data.col);
      setRows(data.data.row);
    }
  }, [isLoading]);

  return (
    <>
      {!isLoading && data && !isreserve && (
        <div className={style.container}>
          <div className={style.header}>
            <div className={style.title}>{location.state.title}</div>
            <div className={style.information}>
              <div className={style.name}>공연일</div>
              <div className={style.content}>
                {location.state.endDate.slice(0, 4)}년&nbsp;
                {location.state.endDate.slice(5, 7)}월&nbsp;
                {location.state.endDate.slice(8, 10)}일&nbsp;
                {location.state.endDate.slice(11, 13)}시&nbsp;
                {location.state.endDate.slice(14, 16)}분&nbsp;
              </div>
              <div className={style.name}>공연장소</div>
              <div className={style.content}>{location.state.location}</div>
              <div className={style.name}>선택 좌석</div>
              <div className={style.content}>{seat}</div>
            </div>
            <div className={style.btn_list}>
              <div className={style.reserve_btn}>
                <button
                  onClick={() => {
                    onCheck();
                  }}
                >
                  예매하기
                </button>
              </div>
              <div className={style.cancel_btn}>
                <button
                  onClick={() => {
                    navigate("/home");
                  }}
                >
                  메인으로
                </button>
              </div>
            </div>
          </div>
          <div className={style.main}>
            <div className={style.blank} />
            <div className={style.stage_div}>STAGE</div>
            <div className={style.seat_total}>{rowsElements}</div>
          </div>
        </div>
      )}
      {!isLoading && data && isreserve && (
        <div className={style.total}>
          <div className={style.left_div}>
            <div>
              <img
                className={style.poster_img}
                src={location.state.image}
                alt=""
              />
            </div>
            <div>
              <button
                className={style.btn}
                onClick={() => {
                  preparePayment(paymentData);
                }}
              >
                결제하기
              </button>
              <button
                className={style.btn_cancel}
                onClick={() => {
                  onDelete();
                  navigate("/../../../");
                }}
              >
                예매취소
              </button>
            </div>
          </div>
          <div className={style.right_div}>
            <div className={style.pay_type}>결제 수단</div>
            <div className={style.btn_type}>
              <div>
                <input
                  className={style.radio_btn}
                  type="radio"
                  name="pay_type"
                  id="point"
                  value="point"
                  onClick={(e) => {
                    onClickBtn(e.target.value);
                  }}
                />
                <label className={style.radio_btn} htmlFor="point">
                  포인트
                </label>
              </div>
              <div>
                <input
                  className={style.radio_btn}
                  type="radio"
                  name="pay_type"
                  id="kakao"
                  value="kakao"
                  onClick={(e) => {
                    onClickBtn(e.target.value);
                  }}
                />
                <label className={style.radio_btn} htmlFor="kakao">
                  카카오페이
                </label>
              </div>
            </div>
            <div className={style.contents}>
              <div className={style.name2}>
                <div className={style.title2}>공연명</div>
                <div className={style.content2}>{location.state.title}</div>
              </div>
              <div className={style.seat2}>
                <div className={style.title2}>좌석 번호</div>
                <div className={style.content2}>
                  {seat[0]}-{seat.slice(1)}
                </div>
              </div>
              <div className={style.location2}>
                <div className={style.title2}>공연장</div>
                <div className={style.content2}>{location.state.location}</div>
              </div>
              <div className={style.date2}>
                <div className={style.title2}>공연일</div>
                <div className={style.content2}>
                  {location.state.endDate.slice(2, 4)}년&nbsp;
                  {location.state.endDate.slice(5, 7)}월&nbsp;
                  {location.state.endDate.slice(8, 10)}일&nbsp;
                  {location.state.endDate.slice(11, 13)}시&nbsp;
                </div>
              </div>
            </div>
            <div className={style.pay}>
              <div className={style.title2}>총 결제 금액</div>
              <div className={style.content2}>{location.state.price}원</div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
