# 개발 환경
1. IntelliJ 2023.2
2. springboot 3.2.0
3. gradle 8.4
4. jdk 17
5. window 10

# 사용시 수정 사항
src/resources/application.properties에 들어가서
driver, url, username, password를 자신의 DB 설정에 맞게 수정해주면 됩니다.

# 사용 방법
git clone하여 src/main/java/com/limecoding/shop/ShopApplication을
실행하면 됩니다.

만약 QClass와 관련된 에러가 나오는 경우 QueryDsl을 사용하기 위한 QClass 생성을 해야 합니다.

인텔리제이의 경우 현재 2023.2 버전을 기준으로 Build 메뉴 > Build Project를 누르면 됩니다.