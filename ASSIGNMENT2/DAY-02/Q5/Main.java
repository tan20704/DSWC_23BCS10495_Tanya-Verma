// Abstract payload type representing any ETL input.
abstract class DataPayload {
    public abstract String getRawContent();
}

// JSON-specific payload implementation.
class JsonPayload extends DataPayload {
    private String rawContent;

    public JsonPayload(String rawContent) {
        this.rawContent = rawContent;
    }

    @Override
    public String getRawContent() {
        return rawContent;
    }
}

// XML-specific payload implementation.
class XmlPayload extends DataPayload {
    private String rawContent;

    public XmlPayload(String rawContent) {
        this.rawContent = rawContent;
    }

    @Override
    public String getRawContent() {
        return rawContent;
    }
}

// Generic pipeline processor constrained to DataPayload and its subclasses.
class PipelineProcessor<T extends DataPayload> {
    public void process(T payload) {
        System.out.println("Processing payload content: " + payload.getRawContent());
    }
}

public class Main {
    public static void main(String[] args) {
        PipelineProcessor<JsonPayload> jsonProcessor = new PipelineProcessor<>();
        jsonProcessor.process(new JsonPayload("{\"orderId\":12345, \"status\":\"ready\"}"));

        PipelineProcessor<XmlPayload> xmlProcessor = new PipelineProcessor<>();
        xmlProcessor.process(new XmlPayload("<order><id>12345</id><status>ready</status></order>"));
    }
}
