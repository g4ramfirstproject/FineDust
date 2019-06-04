# <img src=logo2.png width=40> 미세그램
공공데이터 Api를 활용하여 미세먼지의 정보를 알려주는 어플

### 구글 플레이 주소 
https://play.google.com/store/apps/details?id=com.g4ram.ju.finedust

## 목차
- [참여인원](#참여인원)
- [API정보](#API정보)
- [기능소개](#기능소개)

## 참여인원

- 강래민 [(깃허브)](https://github.com/kangraemin)
- 박정호 [(깃허브)](https://github.com/Jpumpkin93)
- 손유정 [(깃허브)](https://github.com/handnew04)
- 윤희중 [(깃허브)](https://github.com/HuijungYoon)

## API정보
- **대기 정보**
  - 공공데이터 api
- **위치(좌표) 정보**
  - google map·location api, Kakao REST api(좌표계 변환)
- **장소 검색**
  - google place api (Auto CompleteSupportFragment)
- **공유 기능**
  - Kakao link api
 
## 기능소개
  1. [메인화면](#메인화면)
  2. [전국미세먼지](#전국미세먼지)
  3. [알람](#알람)
  4. [장소검색](#장소검색)
  5. [대기정보공유](#대기정보공유)
  6. [위젯](#)

### 메인화면
  - GPS 사용 시 현재 위치와 대기정보, 현재시간을 보여줍니다.<br>
  - GPS 미사용 시 마지막 위치의 정보나 검색한 위치의 정보를 보여줍니다.<br>
  - 시간별 미세먼지 상황을 보여줍니다.
  <br>
  <img src=images/main.jpg width=250>

### 전국미세먼지
  - 메인화면의 왼쪽 상단의 버튼을 눌러 전국 미세먼지를 볼 수 있습니다.<br>
  - 측정시간 및 전국의 각 미세먼지를 색상과 수치로 확인할 수 있습니다.
  <br>
  <img src=images/map.jpg width=250>

### 알람
  - 메인화면의 왼쪽 상단의 버튼을 눌러 설정에 들어갑니다.<br>
  - 타임피커로 시간을 설정하면 매일 같은 시간에 알람이 옵니다.<br>
  - 알람은 스위치 버튼으로 on/off 할 수 있습니다.
  
  <img src=images/timepicker.jpg width=250> <img src= images/alarm.jpg width=250>
  
### 장소검색  
  - 메인화면의 오른쪽 상단의 돋보기 버튼을 눌러 검색할 수 있습니다.<br>
  - 장소를 선택하면 해당 장소의 대기정보를 메인화면에서 보여줍니다.
  
  <img src=images/search.jpg width=250> <img src=images/searchdone.jpg width=250>
  
### 대기정보공유
  - 메인화면에서 오른쪽 상단의 공유 버튼을 눌러 카카오톡을 통해 공유할 수 있습니다.<br>
  - 현재 메인화면에 표시되는 대기정보를 공유합니다.
  <br>
  <img src=images/share.jpg width=250>
  
### 위젯
  - 어플의 위젯을 사용할 수 있습니다.<br>
  - 위젯을 누르면 어플로 이동하고, 새로고침 버튼을 누르면 대기정보만 새로 받아옵니다.
  
  <img src=images/widget1.jpg width=250> <img src=images/widget2.jpg width=250>
