# Gcm

<b>O que devo alterar antes de testar o projeto?</b>

1 - No arquivo string.xml você irá alterar os atributos "sender_id" e "sender url". 
```xml
    <string name="sender_id">Project number - Google API console</string>
    <string name="sender_url">http://[your URL]/gcmserver.php</string>
````    

2 - Na classe "GcmHelper.java" você irá criar a sua rotina de registro no método "toSendRegistrationIdServer"
```java    
    private static void toSendRegistrationIdServer(Context context, String key) throws IOException {

        try {
            URL url = new URL(context.getString(R.string.sender_url));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            os.write(("acao=registrar&regId=" + key).getBytes());
            os.flush();
            os.close();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Error saving server");
            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }
````
3 - Caso não tenho um back-end pronto, você pode utilizar o arquivo <a href="https://gist.github.com/rudsonlive/3ab8d4693dc82dedd000" target="_blank">gcmserver.php</a> para fazer os seus testes. Lebrando que você deve informar sua Chave de API criada no Console Google Api Ex: "AIzaSyB-yNd_TuJWh1urQo45w_9pziIMyO360n9". <a href="https://developers.google.com/mobile/add" target="_blank">Criar chave de Api</a> <br> <br>
Será preciso criar um arquivo chamado "dispositivos.txt" no mesmo local do <a href="https://gist.github.com/rudsonlive/3ab8d4693dc82dedd000" target="_blank">gcmserver.php</a> pois, ele irá guardar todos os Ids registrados para o envio da mensagem já que não foi utilizando um banco de dados.

4 - Depois de efetuar o registro do usuário, você pode utilizar o arquivo <a href="https://gist.github.com/rudsonlive/912a2a2b4d3cf1bb2f3d" target="_blank">index.html</a> para fazer o envio das mensagens.

<b>Documentação:</b> <a href="https://developers.google.com/cloud-messaging/gcm" target="_blank">Google Cloud Messaging</a><br>
<b>Crédito especial:</b> <a href="https://plus.google.com/u/0/+EduardoGorio/about" target="_blank">Eduardo Gorio</a>

Espero que esse projeto ajude você e muitos outros :D
