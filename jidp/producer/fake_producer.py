from kafka import KafkaProducer
from kafka.errors import KafkaError
import numpy as np
import pickle
import time
import json


if __name__=="__main__":
    producer = KafkaProducer(value_serializer=lambda v:json.dumps(v).encode('utf-8'),bootstrap_servers='localhost:9092', linger_ms=10)
    print("Successfully create producer");
    id = 0
    while id < 300000:
        newfilename = "/mnt/matrices/array_" + str(id) + ".npy"
        newrain = np.load(newfilename)
        newrain = np.nan_to_num(newrain)
        newrain = newrain.flatten()
        future = producer.send("weatherdata", key=b'key', value=newrain.astype(np.uint8).tolist())
        producer.flush()
        time.sleep(1)
        id += 1
