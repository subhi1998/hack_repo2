
//Using a Loop to calculate power
public class JavaCodeFile2 {

    public static double powerLoop(double base, int exponent) {
        double result = 1;
        int absExponent = Math.abs(exponent);

        for (int i = 0; i < absExponent; i++) {
            result *= base;
        }

        return (exponent >= 0) ? result : 1 / result;
    }

    // RSA Example
    public static void rsaExample() throws Exception {
        // Generate KeyPair for RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Encrypt
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String plainText = "Hello, RSA!";
        byte[] encryptedText = cipher.doFinal(plainText.getBytes());
        System.out.println("RSA Encrypted: " + Base64.getEncoder().encodeToString(encryptedText));

        // Decrypt
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedText = cipher.doFinal(encryptedText);
        System.out.println("RSA Decrypted: " + new String(decryptedText));
    }

    // Kafka Idempotent Producer Consumer Example
    public static void kafkaIdempotentExample() {
        String inputTopic = "input-topic";
        String outputTopic = "output-topic";
        String bootstrapServers = "localhost:9092";

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "idempotent-group");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        consumer.subscribe(Collections.singletonList(inputTopic));
        Set<String> processedOffsets = new ConcurrentSkipListSet<>();

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String offsetKey = record.topic() + "-" + record.partition() + "-" + record.offset();
                    if (!processedOffsets.contains(offsetKey)) {
                        String transformedValue = record.value().toUpperCase();
                        producer.send(new ProducerRecord<>(outputTopic, record.key(), transformedValue));
                        processedOffsets.add(offsetKey);
                        System.out.println("Processed offset: " + offsetKey);
                    }
                }
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }

    // Kafka Transactional Producer Consumer Example
    public static void kafkaTransactionalExample() {
        String inputTopic = "input-topic";
        String outputTopic = "output-topic";
        String bootstrapServers = "localhost:9092";

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "exactly-once-group");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction-id-1");
        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        producer.initTransactions();
        consumer.subscribe(Collections.singletonList(inputTopic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty()) {
                    producer.beginTransaction();
                    try {
                        for (ConsumerRecord<String, String> record : records) {
                            String transformedValue = record.value().toUpperCase();
                            producer.send(new ProducerRecord<>(outputTopic, record.key(), transformedValue));
                        }
                        producer.sendOffsetsToTransaction(
                                consumer.assignment().stream()
                                        .collect(Collectors.toMap(
                                                tp -> tp,
                                                tp -> new OffsetAndMetadata(consumer.position(tp)))),
                                consumer.groupMetadata());
                        producer.commitTransaction();
                    } catch (Exception e) {
                        producer.abortTransaction();
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }


    public static void main(String[] args) {
        System.out.println(powerLoop(2, 3));   // Output: 8.0
        System.out.println(powerLoop(2, -3));  // Output: 0.125
    }
}
