# 🌆 미세그램
공공데이터 Api를 활용하여 미세먼지의 정보를 알려주는 어플

## 목차
- [참여인원](#참여인원)
- [API정보](#API정보)
- [기능소개](#기능소개)

## 참여인원

- 강래민 [깃허브](https://github.com/kangraemin)
- 박정호 [깃허브](https://github.com/Jpumpkin93)
- 손유정 [깃허브](https://github.com/handnew04)
- 윤희중 [깃허브](https://github.com/HuijungYoon)

## API정보
- **대기 정보**
  - 공공데이터 api
- **위치(좌표) 정보**
  - google map·location api
- **장소 검색**
  - google place api (Auto CompleteSupportFragment)
- **공유 기능**
  - Kakao link api
 
## 기능소개

- **메인화면**
  - GPS 사용 시 현재 위치와 대기정보, 현재시간을 보여줍니다.<br>
  - GPS 미사용 시 마지막 위치의 정보나 검색한 위치의 정보를 보여줍니다.<br>
  - 시간별 미세먼지 상황을 보여줍니다.<br>
  <img src=images/main.jpg width=250>

- **전국 미세먼지**
  - 메인화면의 왼쪽 상단의 버튼을 눌러 전국 미세먼지를 볼 수 있습니다.<br>
  - 측정시간 및 전국의 각 미세먼지를 색상과 수치로 확인할 수 있습니다.<br>
  <img src=images/map.jpg width=250>

- **알람**
  - 메인화면의 왼쪽 상단의 버튼을 눌러 설정에 들어갑니다.<br>
  - 타임피커로 시간을 설정하면 매일 같은 시간에 알람이 옵니다.<br>
  - 알람은 스위치 버튼으로 on/off 할 수 있습니다.<br>
  <img src=images/timepicker.jpg width=250><img src= images/alarm.jpg width=250>
