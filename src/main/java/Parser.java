import com.squareup.moshi.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Parser {
    // Percorso del file JSON da aprire
    String filePath = "path_to_your_json_file.json";

    try (Reader reader = new FileReader(filePath)) {

        // Inizializza Moshi
        Moshi moshi = new Moshi.Builder().build();

        // Ottieni l'adattatore per il tuo tipo di dati
        JsonAdapter<YourClass> jsonAdapter = moshi.adapter(YourClass.class);

        // Deserializza il JSON nel tuo oggetto Java
        YourClass yourObject = jsonAdapter.fromJson(reader);

        // Ora puoi utilizzare l'oggetto Java
        System.out.println(yourObject);

    } catch (IOException e) {
        e.printStackTrace();
    }

    String json;

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<ResourceCard> jsonAdapter = moshi.adapter(ResourceCard.class);

    ResourceCard blackjackHand = jsonAdapter.fromJson(json);
    Moshi moshi = new Moshi.Builder()
            .add(new ResourceCardAdapter())
            .build();
}
