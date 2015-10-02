# Gcm
Sample project using gcm

<b>O que devo alterar antes de testar o projeto?</b>

1 - No arquivo string.xml você irá alterar os atributos "sender_id" e "sender url". 

<string name="sender_id">Project number - Google API console</string> 
<string name="sender url">http://[your URL]/gcmserver.php</string>

2 - Na classe "GcmHelper.java" você irá criar a sua rotina de registro no método "toSendRegistrationIdServer"

3 - Caso não tenho um back-end pronto, você pode utilizar o arquivo "gcmserver.php" para fazer os seus testes. Lebrando que você deve informar sua Chave de API criada no Console Google Api Ex: "AIzaSyB-yNd_TuJWh1urQo45w_9pziIMyO360n9". 
Será precisar criar um arquivo chamado "dispositivos.txt" na mesmo local do "gcmserver.php" pois, ele irá guardos todos os Ids registrados para o envio da mensagem.

4 - Depois de efetuar o registro do usuário você pode utilizar o arquivo "index.html" para fazer o envio das mensagens.

Espero que esse projeto ajude você e muitos outros :D
