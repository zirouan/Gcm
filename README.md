# Gcm
Sample project using gcm

<b>O que devo alterar antes de testar o projeto?</b>

1 - No arquivo string.xml você irá alterar os atributos "sender_id" e "sender url". 

<string name="sender_id">Project number - Google API console</string> 
<string name="sender url">http://[your URL]/gcmserver.php</string>

2 - Na classe "GcmHelper.java" você irá criar a sua rotina de registro no método "toSendRegistrationIdServer"

3 - Caso não tenho um back-end pronto, você pode utilizar o arquivo <a href="https://gist.github.com/rudsonlive/3ab8d4693dc82dedd000" target="_blank">"gcmserver.php"</a> para fazer os seus testes. Lebrando que você deve informar sua Chave de API criada no Console Google Api Ex: "AIzaSyB-yNd_TuJWh1urQo45w_9pziIMyO360n9". 
Será precisar criar um arquivo chamado "dispositivos.txt" na mesmo local do href="https://gist.github.com/rudsonlive/3ab8d4693dc82dedd000" target="_blank">"gcmserver.php"</a> pois, ele irá guardos todos os Ids registrados para o envio da mensagem.

4 - Depois de efetuar o registro do usuário você pode utilizar o arquivo https://gist.github.com/rudsonlive/912a2a2b4d3cf1bb2f3d" target="_blank">"index.html"</a> para fazer o envio das mensagens.

Espero que esse projeto ajude você e muitos outros :D
