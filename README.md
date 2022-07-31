# spring_JWT_demo

- [Profile 적용하는 법](https://velog.io/@be_have98/IntelliJ-Spring-Boot%EC%9D%98-Active-Profile-%EC%84%A4%EC%A0%95)

## Profile : bearer
### 요청

- POST : "/token" 
  - Header에 Authorization 탭에 codestates를 추가하여 요청 보내면, 응답에 "token"이 돌아옴 
  - Header에 Authorization 탭에 codestates가 아니면, 응답에 "인증 실패"를 넣어 보냄

### 파일

- filter.FirstFilter
  - 특정 헤더 ("Authorization")에 특정 값("codestates")이 있을 때만 다음 필터를 실행
  - 그렇지 않을 경우, "인증 실패"를 응답함

- config.SecurityConfigBearer
  - FirstFilter 적용 등

- controller.RestApiControllerBearer
  - GET "/home" : 어떤 상황에서든 요청 가능
  - POST "/token" : filter를 통과한 요청은 정상적으로 "token"을 응답함

## Profile : login

### 요청

- POST "/join"
  - body 에 {"username" : "lee", "password" : 123} 을 넣음
  - 응답으로 "회원 가입 완료" 가 나옴
- POST "/login"
  - username과 password 처리를 하지 않았기 때문에 오류 발생 (500 ERROR)
  - 로그에 "login 시도" 확인 가능

### 파일

- filter.JwtAuthenticationFilterLogin
  - 로그인 시도 시, "login 시도" 출력
  - 로그인 시도 시, body에 들어오는 내용을 console 창에 print함 

- config.SecurityConfigLogin
  - class CustomDsl 을 통해 JwtAuthenticationFilterLogin를 추가함
  - BCryptPasswordEncoder를 Spring Bean에 추가

- controller.RestApiControllerBearer
  - GET "/home" : 어떤 상황에서든 요청 가능
  - POST "/join" : 회원 가입 (비밀번호 암호화, Member 객체 저장)

## Profile : Token

### 요청

- POST "/join"
  - body 에 {"username" : "lee", "password" : 123} 을 넣음
  - 응답으로 "회원 가입 완료" 가 나옴
- POST "/login"
  - body 응답은 비어 있음
  - 응답 Authorization 탭에 발급한 Token 값이 있음
- GET "/api/v1/user"
  - 발급받은 Token 값을 header Authorization 탭에 넣어서 요청
  - 응답으로 "user"를 받음
- GET "api/v1/admin"
  - 발급받은 Token 값을 header Authorization 탭에 넣어서 요청
  - 접근 권한이 허용되지 않아 403 ERROR가 발생

### 파일

- filter.JwtAuthorizationFilterToken
  - 해당 필터 실행 시, "인증이나 권한이 필요한 주소 요청 됨." 로그에 출력
  - 요청 header Authorization 탭에서 값을 가져와 유효한 토큰인지 확인

- filter.JwtAuthenticationFilterToken
  - attemptAuthentication
    - PrincipalDetailsService의 loadUserByUsername() 메서드가 실행된 후 정상 작동된다면 authentication이 return
  - successfulAuthentication
    - attemptAuthentication 메서드가 정상적으로 작동하게 되면 successfulAuthentication 메서드를 실행
    - 해당 메서드에서 JWT 토큰을 만들어서 요청한 사용자에게 JWT 토큰을 응답으로 돌려줌

- config.SecurityConfigToken
  - class CustomDsl 을 통해 JwtAuthorizationFilterToken, JwtAuthenticationFilterToken 를 추가
  - BCryptPasswordEncoder를 Spring Bean에 추가

- controller.RestApiControllerBearer
  - GET "/home" : 어떤 상황에서든 요청 가능
  - POST "/join" : 회원 가입 (비밀번호 암호화, Member 객체 저장)
  - GET : "/api/v1/user" : 권한이 USER가 있는 경우, 접근 가능 
  - GET : "/api/v1/admin" : 권한이 ADMIN이 있는 경우, 접근 가능

## 공통 내용

- config.CorsConfig
  - 특정 조건, 특정 상황에서만 응답을 허용

- config.SecurityConfig 공통
  - csrf 요청 막기
  - h2 연결 허용시 필요한 옵션 설정
  - CorsFilter 추가
  - formLogin 사용 안함
  - 접근 제한할 URI 설정

### Profile : login,token
- oauth.PrincipalDetails
  - POST "/login" 주소에 요청이 오면 대신 로그인을 진행
  - Security Session ⇒ Authentication ⇒ UserDetails
  - Member 객체와 생성자를 추가, implements UserDetails

- oauth.PrincipalDetailsService
  - Security 설정에서 loginProcessingUrl(”/login”);으로 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 메서드 실행
  - implements UserDetailsService

- model.Member : 회원 Entity
- repository.MemberRepository : 회원 저장하는 repository