def power_bitwise(base, exponent):
        if exponent == 0:
        return 1
        if exponent < 0:
base = 1 / base
        exponent = -exponent
result = 1
        while exponent > 0:
        if exponent % 2 == 1:  # If exponent is odd
result *= base
base *= base
        exponent //= 2  # Divide exponent by 2
    return result

# Kafka Transactional Producer-Consumer Example
def kafka_transactional_example():
    producer_conf = {
        'bootstrap.servers': 'localhost:9092',
        'transactional.id': 'txn-python',
        'enable.idempotence': True
    }
    producer = Producer(producer_conf)
    producer.init_transactions()

    consumer_conf = {
        'bootstrap.servers': 'localhost:9092',
        'group.id': 'exactly-once-python-group',
        'auto.offset.reset': 'earliest',
        'isolation.level': 'read_committed'
    }
    consumer = Consumer(consumer_conf)
    consumer.subscribe(['input-topic'])

    try:
        while True:
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    continue
                else:
                    print(f"Error: {msg.error()}")
                    break

            producer.begin_transaction()
            try:
                transformed_value = msg.value().decode('utf-8').upper()
                producer.produce('output-topic', key=msg.key(), value=transformed_value)
                producer.commit_transaction()
            except Exception as e:
                producer.abort_transaction()
                print(f"Transaction aborted: {e}")
    finally:
        consumer.close()

# AES Encryption/Decryption Example
def encrypt(data, key):
    iv = os.urandom(16)
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
    encryptor = cipher.encryptor()
    padder = sym_padding.PKCS7(algorithms.AES.block_size).padder()
    padded_data = padder.update(data.encode()) + padder.finalize()
    return iv + encryptor.update(padded_data) + encryptor.finalize()

def decrypt(encrypted_data, key):
    iv = encrypted_data[:16]
    cipher = Cipher(algorithms.AES(key), modes.CBC(iv), backend=default_backend())
    decryptor = cipher.decryptor()
    unpadder = sym_padding.PKCS7(algorithms.AES.block_size).unpadder()
    decrypted_data = unpadder.update(decryptor.update(encrypted_data[16:]) + decryptor.finalize())
    return decrypted_data.decode()
# Example Usage
print(power_bitwise(2, 3))  # Output: 8
print(power_bitwise(2, -3)) # Output: 0.125