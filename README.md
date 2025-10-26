# 🌤️ ClimaNow (Android)

ClimaNow é um app Android simples para consultar e visualizar o clima de uma cidade. Interface baseada em Material Design, com lista de dados do tempo, mapa com marcador da cidade e troca rápida de cidade por QR Code.

Funcionalidades
- 3 Activities:
  - SplashActivity (SplashScreen de 3 segundos via Handler)
  - MainActivity (host das abas)
  - AboutActivity (tela "Sobre", acessível pelo menu da AppBar)
- Lista de dados do tempo usando RecyclerView + CardView.
- Menu na AppBar com opção "Sobre".
- TabLayout com 2 abas:
  - Aba 1 — Lista: exibe dados da API de clima (HG Brasil).
  - Aba 2 — Mapa: mostra a cidade com marcador (Google Maps).
- FloatingActionButton para trocar cidade via leitura de QR Code (biblioteca: zxing-android-embedded).
- Material Design aplicado nos componentes e estilos.

Tecnologias e bibliotecas
- Android Studio (Kotlin ou Java)
- AndroidX, Material Components
- RecyclerView, CardView
- Google Maps SDK para Android
- zxing-android-embedded (scanner de QR)
- Retrofit + OkHttp + Gson (consumo da API HG Brasil)
- Handler (Splash), ConstraintLayout

Pré-requisitos
- Android Studio instalado
- Android SDK compatível
- Chaves de API:
  - HG Brasil (Weather API): https://console.hgbrasil.com/documentation/weather
  - Google Maps API Key
- Conexão com Internet
- (Em dispositivo) Google Play Services para Maps

Instalação e execução
1. Clone o repositório:
   git clone https://github.com/acduranti/projeto-app-climanow.git
2. Abra o projeto no Android Studio.
3. Adicione as chaves de API (não versionar):
   - Exemplo em `local.properties` (recomendado):
     HG_BRASIL_API_KEY=SUACHAVE_HGBRASIL
     MAPS_API_KEY=SUACHAVE_GOOGLE_MAPS
   - Ou crie `app/src/main/res/values/strings.xml` com:
     ```xml
     <string name="hg_br_api_key">SUA_CHAVE_HGBRASIL</string>
     <string name="google_maps_key">SUA_CHAVE_GOOGLE_MAPS</string>
     ```
   - Se usar `strings.xml`, adicione no AndroidManifest a meta-data do Maps:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="@string/google_maps_key" />
     ```
4. Verifique e adicione dependências no Gradle. Exemplo (app/build.gradle):
   - implementation 'com.google.code.gson:gson:2.8.9'
   - implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   - implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
   - implementation 'com.google.android.gms:play-services-maps:18.1.0'
5. Conceda permissões em tempo de execução quando solicitado:
   - INTERNET (manifest)
   - CAMERA (para scanner)
   - ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION (se usar localização)
6. Rode o app em emulador ou dispositivo físico.

Formato de QR sugerido
- city:<nome_da_cidade> (ou só o nome da cidade)
  - Ex.: city:São Paulo
- ou coordenadas: lat,lng
  - Ex.: -23.550520,-46.633308

Autor
- Anna Clara Duranti de Moura (acduranti)

Licença
- Uso educacional, pode ser adaptado para fins pessoais.    
