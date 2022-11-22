package directory.mastodoninstances.backend.integration.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId
import org.springframework.boot.jackson.JsonComponent
import java.io.IOException

@JsonComponent
class MyJsonComponent {
    class ObjectIdSerializer : JsonSerializer<ObjectId>() {

        @Throws(IOException::class)
        override fun serialize(value: ObjectId, jgen: JsonGenerator, serializers: SerializerProvider) {
            jgen.writeString(value.toString())
        }

    }

    class ObjectIdDeserializer : JsonDeserializer<ObjectId>() {

        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): ObjectId {
              return ObjectId(jsonParser.readValueAs(String::class.java))
        }

    }
}
